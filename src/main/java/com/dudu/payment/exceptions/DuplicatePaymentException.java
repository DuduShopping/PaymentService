package com.dudu.payment.exceptions;

public class DuplicatePaymentException extends Exception {
    private long orderId;

    public DuplicatePaymentException(long orderId) {
        super();
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
