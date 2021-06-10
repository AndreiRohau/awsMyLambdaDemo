package me.ras.api.awsClient;

import software.amazon.awssdk.http.apache.ApacheHttpClient;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sns.SnsClient;

import java.time.Duration;
import java.util.logging.Logger;

import static me.ras.api.Constant.getAwsRegion;
import static me.ras.api.Constant.getCredentialProvider;

public class SnsClientProvider {
    private static Logger log = Logger.getLogger(SnsClientProvider.class.getName());

    private SnsClient snsClient;

    public SnsClientProvider() {
        final Region region = Region.of(getAwsRegion());

        log.info("INIT snsClient");
        snsClient = SnsClient.builder()
                .credentialsProvider(getCredentialProvider())
                .region(region)
//                .httpClientBuilder(NettyNioAsyncHttpClient.builder()
//                        .maxConcurrency(10)
//                        .maxPendingConnectionAcquires(1000))
                .httpClientBuilder(ApacheHttpClient.builder()
                        .maxConnections(10)
                        .connectionMaxIdleTime(Duration.ofMillis(2000)))
                .build();

        log.info("SnsClient is built. Region=" + region);
    }

    public static SnsClientProvider init() {
        return new SnsClientProvider();
    }

    public SnsClient getSnsClient() {
        return snsClient;
    }
}
