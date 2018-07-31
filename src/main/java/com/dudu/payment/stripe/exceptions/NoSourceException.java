package com.dudu.payment.stripe.exceptions;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class NoSourceException extends DuduStripeException {
    private long userId;
    private String sourceId;
}
