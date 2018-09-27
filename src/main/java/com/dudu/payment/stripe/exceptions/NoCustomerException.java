package com.dudu.payment.stripe.exceptions;


public class NoCustomerException extends DuduStripeException {
    private long userId;

    public NoCustomerException(long userId) {
        this.userId = userId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}
