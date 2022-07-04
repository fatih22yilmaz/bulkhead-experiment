package com.fatihyilmaz.client.configuration;

import io.github.resilience4j.bulkhead.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.time.Duration;

@Configuration
public class BulkHeadConfiguration {

    @Bean
    public BulkheadConfig bulkheadConfig() {
        return BulkheadConfig.custom()
                .maxConcurrentCalls(5)
                .maxWaitDuration(Duration.ofMillis(500))
                .build();
        // https://resilience4j.readme.io/docs/bulkhead
    }

    @Bean
    public ThreadPoolBulkheadConfig threadPoolBulkheadConfig() {
        return ThreadPoolBulkheadConfig.custom()
                .maxThreadPoolSize(10)
                .coreThreadPoolSize(2)
                .queueCapacity(20)
                .build();
    }

    @Bean
    public BulkheadRegistry bulkheadRegistry(BulkheadConfig bulkheadConfig) {
        return BulkheadRegistry.of(bulkheadConfig);
    }

    @Bean
    public ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry(ThreadPoolBulkheadConfig threadPoolBulkheadConfig) {
        return ThreadPoolBulkheadRegistry.of(threadPoolBulkheadConfig);
    }

    @Bean
    public Bulkhead customBulkhead(BulkheadRegistry bulkheadRegistry) {
        return bulkheadRegistry.bulkhead("customBulkhead");
    }

    @Bean
    public ThreadPoolBulkhead threadPoolBulkhead(ThreadPoolBulkheadRegistry threadPoolBulkheadRegistry) {
        return threadPoolBulkheadRegistry.bulkhead("threadPoolBulkhead");
    }
}
