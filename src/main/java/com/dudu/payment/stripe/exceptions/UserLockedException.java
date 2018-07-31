package com.dudu.payment.stripe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class UserLockedException extends DuduStripeException {
    private long userId;
    private String message;
}
