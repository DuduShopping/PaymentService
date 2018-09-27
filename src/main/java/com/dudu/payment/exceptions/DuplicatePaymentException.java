package com.dudu.payment.exceptions;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class DuplicatePaymentException extends Exception {
    private long orderId;
}
