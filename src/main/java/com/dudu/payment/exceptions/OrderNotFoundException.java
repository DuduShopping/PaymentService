package com.dudu.payment.exceptions;

public class OrderNotFoundException extends Exception {
    private long orderId;

    public OrderNotFoundException(long orderId) {
        this.orderId = orderId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }
}
