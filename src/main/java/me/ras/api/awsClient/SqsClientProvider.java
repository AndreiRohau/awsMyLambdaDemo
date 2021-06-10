package me.ras.api.awsClient;

import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;

import java.util.logging.Logger;

import static me.ras.api.Constant.getAwsRegion;
import static me.ras.api.Constant.getCredentialProvider;

public class SqsClientProvider {
    private static Logger log = Logger.getLogger(SqsClientProvider.class.getName());

    private SqsClient sqsClient;

    public SqsClientProvider() {
        final Region region = Region.of(getAwsRegion());

        log.info("INIT sqsClient");
        sqsClient = SqsClient.builder()
                .credentialsProvider(getCredentialProvider())
                .region(region)
                .build();

        log.info("SqsClient is built. Region=" + region);
    }

    public static SqsClientProvider init() {
        return new SqsClientProvider();
    }

    public SqsClient getSqsClient() {
        return sqsClient;
    }
}
