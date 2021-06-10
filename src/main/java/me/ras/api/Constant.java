package me.ras.api;

import software.amazon.awssdk.auth.credentials.AwsBasicCredentials;
import software.amazon.awssdk.auth.credentials.AwsCredentialsProvider;

public class Constant {
    private static AwsCredentialsProvider credentialProvider;
    private static String awsRegion;
    private static String awsSnsTopicArn;
    private static String awsSqsUrl;
    private static String awsSqsQueueName;

    public static void initConstants(LambdaSqsToSns.Request request) {
        credentialProvider = () -> AwsBasicCredentials.create(request.awsAccessKeyId, request.awsSecretKey);
        Constant.awsRegion = request.awsRegion;
        Constant.awsSnsTopicArn = request.awsSnsTopicArn;
        Constant.awsSqsUrl = request.awsSqsUrl;
        Constant.awsSqsQueueName = request.awsSqsQueueName;
    }

    public static AwsCredentialsProvider getCredentialProvider() {
        return credentialProvider;
    }

    public static String getAwsRegion() {
        return awsRegion;
    }

    public static String getAwsSnsTopicArn() {
        return awsSnsTopicArn;
    }

    public static String getAwsSqsUrl() {
        return awsSqsUrl;
    }

    public static String getAwsSqsQueueName() {
        return awsSqsQueueName;
    }

    public static String concatAllValues() {
        return "Constant{awsRegion=" + awsRegion + ", awsSnsTopicArn=" + awsSnsTopicArn + ", awsSqsQueueName=" + awsSqsQueueName + "}.";
    }
}
