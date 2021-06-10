package me.ras.api.service;

import me.ras.api.awsClient.SnsClientProvider;
import me.ras.api.awsClient.SqsClientProvider;
import software.amazon.awssdk.services.sns.SnsClient;
import software.amazon.awssdk.services.sns.model.PublishRequest;
import software.amazon.awssdk.services.sns.model.PublishResponse;
import software.amazon.awssdk.services.sns.model.SnsException;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.*;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;

import static java.util.stream.Collectors.toList;
import static me.ras.api.Constant.getAwsSnsTopicArn;
import static me.ras.api.Constant.getAwsSqsQueueName;

public class NotificationServiceImpl implements NotificationService {
    private static Logger log = Logger.getLogger(NotificationServiceImpl.class.getName());

    private final SnsClient snsClient;
    private final SqsClient sqsClient;

    public NotificationServiceImpl(SnsClientProvider snsClientProvider, SqsClientProvider sqsClientProvider) {
        this.snsClient = snsClientProvider.getSnsClient();
        this.sqsClient = sqsClientProvider.getSqsClient();

    }

    public static NotificationService init(SnsClientProvider snsClientProvider, SqsClientProvider sqsClientProvider) {
        return new NotificationServiceImpl(snsClientProvider, sqsClientProvider);
    }

    @Override
    public void sendNotifications() {
        log.info("NotificationServiceImpl#sendNotifications()");
//        final String sqsUrl = getAwsSqsUrl();
        final String sqsUrl = getSqsUrl();
        log.info("NotificationServiceImpl#sendNotifications(). sqsUrl=[" + sqsUrl + "].");
        final List<Message> sqsMessages = receiveSqsMessages(sqsUrl);
        log.info("NotificationServiceImpl#sendNotifications(), messages=[" + sqsMessages + "]");
        final List<String> messageTexts = sqsMessages.stream().map(Message::body).collect(toList());
        publishSnsTopic(messageTexts);
        deleteProcessedSqsMessages(sqsMessages, sqsUrl);
    }

    @Override
    public void publishMessageToSnsDirectly(String message) {
        publishSnsTopic(Collections.singletonList(message));
    }

    private String getSqsUrl() {
        log.info("NotificationServiceImpl#getSqsUrl(). awsSqsQueueName=" + getAwsSqsQueueName() + ".");
        final GetQueueUrlRequest getQueueRequest = GetQueueUrlRequest.builder()
                .queueName(getAwsSqsQueueName())
                .build();
        log.info("NotificationServiceImpl#getSqsUrl(). getQueueRequest=" + getQueueRequest + ".");
        return sqsClient.getQueueUrl(getQueueRequest).queueUrl();
    }

    private List<Message> receiveSqsMessages(final String sqsUrl) {
        try {
            log.info("NotificationServiceImpl#receiveSqsMessages()");
            final ReceiveMessageRequest receiveMessageRequest = ReceiveMessageRequest.builder()
                    .queueUrl(sqsUrl)
                    .maxNumberOfMessages(10)
                    .waitTimeSeconds(20)
                    .build();
            return sqsClient.receiveMessage(receiveMessageRequest).messages();
        } catch (SqsException e) {
            log.warning("Exception during getting messages from SQS queue.");
            throw new RuntimeException("Exception during getting messages from SQS queue.", e);
        }
    }

    private void publishSnsTopic(final List<String> messages) {
        try {
            log.info("NotificationServiceImpl#publishSnsTopic()");
            for (final String message: messages) {
                final PublishRequest request = PublishRequest.builder()
                        .message(message)
                        .topicArn(getAwsSnsTopicArn())
                        .build();
                final PublishResponse result = snsClient.publish(request);
            }
        } catch (SnsException e) {
            log.warning("Exception during publishing SNS Topic.");
            throw new RuntimeException("Exception during publishing SNS Topic.", e);
        }
    }

    private void deleteProcessedSqsMessages(List<Message> sqsMessages, final String sqsUrl) {
        log.info("NotificationServiceImpl#deleteProcessedSqsMessages()");
        for (Message sqsMessage : sqsMessages) {
            DeleteMessageRequest deleteMessageRequest = DeleteMessageRequest.builder()
                    .queueUrl(sqsUrl)
                    .receiptHandle(sqsMessage.receiptHandle())
                    .build();
            sqsClient.deleteMessage(deleteMessageRequest);
        }
    }
}
