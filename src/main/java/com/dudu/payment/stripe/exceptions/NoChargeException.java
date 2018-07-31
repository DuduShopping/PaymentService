package com.dudu.payment.stripe.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoChargeException extends DuduStripeException {
    private String chargeId;
}
