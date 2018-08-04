package com.dudu.payment.stripe;

import org.springframework.boot.context.properties.ConfigurationProperties;
import javax.validation.constraints.NotEmpty;

@ConfigurationProperties("stripe")
public class StripeConfigProperties {

    @NotEmpty
    private String apiKey;

    public String getApiKey() {
        return apiKey;
    }

    public void setApiKey(String apiKey) {
        this.apiKey = apiKey;
    }
}
