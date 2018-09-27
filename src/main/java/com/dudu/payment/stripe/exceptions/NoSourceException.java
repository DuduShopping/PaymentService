package com.dudu.payment.stripe.exceptions;

public class NoSourceException extends DuduStripeException {
    private long userId;
    private String sourceId;

    public NoSourceException(long userId, String sourceId) {
        this.userId = userId;
        this.sourceId = sourceId;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }
}
