package com.dudu.payment.stripe.exceptions;

public class UserLockedException extends DuduStripeException {
    private long userId;
    private String message;

    public UserLockedException(long userId, String message) {

        this.userId = userId;
        this.message = message;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
