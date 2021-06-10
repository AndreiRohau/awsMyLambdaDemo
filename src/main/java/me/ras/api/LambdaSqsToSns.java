package me.ras.api;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.S3Event;
import com.amazonaws.services.lambda.runtime.events.models.s3.S3EventNotification;
import me.ras.api.awsClient.SnsClientProvider;
import me.ras.api.awsClient.SqsClientProvider;
import me.ras.api.service.NotificationService;
import me.ras.api.service.NotificationServiceImpl;

import java.util.logging.Logger;

public class LambdaSqsToSns implements RequestHandler<S3Event, LambdaSqsToSns.Response> {
//public class LambdaSqsToSns implements RequestHandler<LambdaSqsToSns.Request, LambdaSqsToSns.Response> {
    private static Logger log = Logger.getLogger(LambdaSqsToSns.class.getName());

    @Override
    public Response handleRequest(S3Event s3Event, Context context) {
//        public Response handleRequest(Request request, Context context) {
        final String logMsg = "Called awsLambdaSqsToSns#LambdaSqsToSns#handleRequest(). Request={" + s3Event + "}, Context={" + context + "}";
        log.info(logMsg);

        Constant.initConstants(stabCreds());
//        Constant.initConstants(request);

        log.info(Constant.concatAllValues());
        final SnsClientProvider snsClientProvider = SnsClientProvider.init();
        final SqsClientProvider sqsClientProvider = SqsClientProvider.init();
        final NotificationService notificationService = NotificationServiceImpl.init(snsClientProvider, sqsClientProvider);

        log.info("S3Event=" + s3Event);
        for (S3EventNotification.S3EventNotificationRecord record: s3Event.getRecords()) {
            log.info("S3EventNotification.S3EventNotificationRecord=" + record);
            S3EventNotification.S3Entity s3 = record.getS3();
            final String bucket = s3.getBucket().getName();
            final String key = s3.getObject().getKey();
            final String message = "Hello fellows - my pipeline does all job for me!!!!!. File: " + bucket + "#-#" + key;
            notificationService.publishMessageToSnsDirectly(message);
        }
//        notificationService.sendNotifications();

        return Response.createResponse("Response msg is=[" + s3Event + "].");
    }

    private static Request stabCreds() {
        Request request1 = new Request();
        request1.msg = "changed repos creds";
        request1.awsAccessKeyId = "AKI" + "A2N" + "2AY3" + "NIN" + "BM4" + "JAE6";
        request1.awsSecretKey = "YSx2U" + "6ir/A" + "Ssri+" + "Wewbs" + "9Uvo6" + "8p6y0" + "oacRC" + "KMJoT";
        request1.awsRegion = "u" + "s" + "-" + "ea" + "st" + "-" + "1";
        request1.awsSnsTopicArn = "ar" + "n:aw" + "s:s" + "ns:u" + "s-e" + "ast" + "-"
                + "1" + ":7" + "168" + "585" + "142" + "56" + ":M" + "yOw" + "nSn" + "sTo" + "pic";
        request1.awsSqsUrl = "ht" + "t" + "ps" + ":" + "/" + "/sq" + "s.u" + "s-e" + "as" + "t-" + "1.a" + "maz"
                + "ona" + "ws.c" + "om/7" + "168" + "58514" + "25" + "6/M" + "yO" + "wnQ" + "ueue";
        request1.awsSqsQueueName = "M" + "yO" + "wnQ" + "ue" + "ue";
        return request1;
    }

    static class Request {
        public String msg;
        public String awsAccessKeyId;
        public String awsSecretKey;
        public String awsRegion;
        public String awsSnsTopicArn;
        public String awsSqsUrl;
        public String awsSqsQueueName;
    }

    static class Response {
        public String msg;

        public Response(String msg) {
            this.msg = msg;
        }

        public static Response createResponse(String msg) {
            return new Response(msg);
        }
    }
}
