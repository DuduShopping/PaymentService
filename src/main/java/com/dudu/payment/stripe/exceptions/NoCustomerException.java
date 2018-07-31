package com.dudu.payment.stripe.exceptions;


import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class NoCustomerException extends DuduStripeException {
    private long userId;
}
