package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.util.Date;

@NoArgsConstructor
@Data
@EqualsAndHashCode(of = {"stripeChargeToken"})
public class StripeCharge {
    private String stripeChargeToken;
    private long userId;
    private long orderId;
    private long amount;
    private String currency;
    private int status;
    private Date chargedAt;

    public static StripeCharge from(DatabaseRow databaseRow) {
        StripeCharge charge = new StripeCharge();
        charge.userId = databaseRow.getLong("UserId");
        charge.orderId = databaseRow.getLong("OrderId");
        charge.amount = databaseRow.getLong("Amount");
        charge.currency = databaseRow.getString("Currency");
        charge.stripeChargeToken = databaseRow.getString("StripeChargeToken");
        charge.status = databaseRow.getInt("Status");
        charge.chargedAt = databaseRow.getDate("ChargedAt");

        return charge;
    }
}
