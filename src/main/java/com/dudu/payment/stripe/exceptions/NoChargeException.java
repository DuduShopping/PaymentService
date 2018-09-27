package com.dudu.payment.stripe.exceptions;


public class NoChargeException extends DuduStripeException {
    private String chargeId;

    public NoChargeException(String chargeId) {
        this.chargeId = chargeId;
    }

    public String getChargeId() {
        return chargeId;
    }

    public void setChargeId(String chargeId) {
        this.chargeId = chargeId;
    }
}
