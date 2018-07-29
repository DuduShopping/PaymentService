package com.dudu.payment.stripe;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

/**
 * Created by chaojiewang on 5/21/18.
 */

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {StripeConfiguration.class})
@TestPropertySource("/com/dudu/payment/stripe/stripe.properties")
public class StripeProxyTest {

    StripeProxy stripeProxy;

    @Before
    public void setup() {
        stripeProxy = StripeProxy.getInstance();
    }


    @Test
    public void createCustomer() throws Exception {
        String description = "user 2";
        String customerId = stripeProxy.createCustomer(description);
        System.out.println(customerId);
    }

    @Test
    public void addSource() throws Exception {
        String customerId = "cus_DJXqKKUXfUYlOl";
        String token = "tok_visa_debit";

        String sourceId = stripeProxy.addSource(customerId, token);
        System.out.println(sourceId);
    }

    @Test
    public void setDefaultPaymentMethod() throws Exception {
        String customerId = "cus_DJXqKKUXfUYlOl";
        String sourceId = "card_1CsulrGaN33MxmV1odtnsJIH";
        stripeProxy.setDefaultPaymentMethod(customerId, sourceId);
    }


    @Test
    public void charge() throws Exception {
        String customerId = "cus_DJXqKKUXfUYlOl";
        int amount = 100*100;
        String chargeId = stripeProxy.charge(customerId, amount);
    }
}
