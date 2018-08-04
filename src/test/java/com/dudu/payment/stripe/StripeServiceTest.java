package com.dudu.payment.stripe;

import com.dudu.database.DatabaseConfiguration;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class, StripeConfiguration.class})
@TestPropertySource("/com/dudu/payment/stripe/stripe.properties")
public class StripeServiceTest {

    @Autowired
    StripeService stripeService;

    @Test
    public void createCustomer() throws Exception {
        long userId = 1;
        String customerId = stripeService.createCustomer(userId);
        println(customerId);
    }

    @Test
    public void addSource() throws Exception {
        long userId = 1;
        String token = "tok_visa_debit";
        String last4 = "4242";
        int expMonth = 4;
        int expYear = 2020;
        String funding = "";
        String brand = "";

        stripeService.addSource(userId, token, last4, expMonth, expYear, funding, brand);
        println("done");
    }

    @Test
    public void isLocked() throws Exception {
        long userId = 1;
        println(stripeService.isLocked(userId));
    }


//    @Test
//    public void lock() throws Exception {
//        long userId = 1;
//        int reasonCode = 100;
//        stripeService.lock(userId, reasonCode);
//        println("done");
//    }

    @Test
    public void getCustomer() throws Exception {
        long userId = 1;
        StripeCustomer customer = stripeService.getCustomer(userId);
        println(customer.getUserId());
    }

    @Test
    public void setPaymentMethod() throws Exception {
        long userId = 1;
        String sourceId = "card_1CtObwGaN33MxmV1FK8yXRI6";
        stripeService.setPaymentMethod(userId, sourceId);
        println("done");
    }

    @Test
    public void charge() throws Exception {
        long userId = 1;
        long orderId = 1;
        long amount = 1000;

        String token = stripeService.charge(orderId, userId, amount);
        println(token);
    }

    @Test
    public void getCharge() throws Exception {
        long userId = 1;
        String stripeChargeToken = "ch_1CtOepGaN33MxmV1Ypzc7Cb2";

        StripeCharge charge = stripeService.getCharge(userId, stripeChargeToken);
        println(charge.getUserId());
    }

    @Test
    public void oneTimeCharge() throws Exception {
        long userId = 1;
        long orderId = 3;
        var sourceId = "tok_mastercard";
        var chargeId = stripeService.oneTimeCharge(orderId, userId, 1001, sourceId);
        println(chargeId);
    }

    private void println(Object o) {
        System.out.println(o.toString());
    }
}
