package com.dudu.payment.stripe;

import com.dudu.database.DatabaseConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

@Configuration
@EnableConfigurationProperties(StripeConfigProperties.class)
@Import(DatabaseConfiguration.class)
public class StripeConfiguration {

    public StripeConfiguration(StripeConfigProperties properties) {
        StripeProxy.configure(properties.getApiKey());
    }
}
