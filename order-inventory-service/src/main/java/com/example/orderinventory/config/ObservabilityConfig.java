package com.example.orderinventory.config;

import io.micrometer.core.instrument.MeterRegistry;
import io.micrometer.core.instrument.Counter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Lightweight observability wiring: provides counters used by services.
 * Removed optional tracing interceptor dependency to avoid a hard classpath requirement.
 */
@Configuration
public class ObservabilityConfig {

    @Bean
    public Counter ordersCreatedSuccess(MeterRegistry registry) {
        return Counter.builder("orders.created.success")
                .description("Number of successfully created orders")
                .register(registry);
    }

    @Bean
    public Counter ordersCreatedFailure(MeterRegistry registry) {
        return Counter.builder("orders.created.failure")
                .description("Number of failed order creation attempts")
                .register(registry);
    }
}