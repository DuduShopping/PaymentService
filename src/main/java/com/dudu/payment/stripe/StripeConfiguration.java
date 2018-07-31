package com.dudu.payment.stripe;

import com.dudu.database.DatabaseConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;

import javax.sql.DataSource;

@Configuration
@EnableConfigurationProperties(StripeConfigProperties.class)
@Import(DatabaseConfiguration.class)
public class StripeConfiguration {

    public StripeConfiguration(StripeConfigProperties properties) {
        StripeProxy.configure(properties.getApiKey());
    }

    @Bean
    public StripeService getStripeSource(DataSource dataSource) {
        return new StripeService(dataSource);
    }
}
