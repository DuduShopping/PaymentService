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
        charge.userId = databaseRow.getLong("user_id");
        charge.orderId = databaseRow.getLong("order_id");
        charge.amount = databaseRow.getLong("amount");
        charge.currency = databaseRow.getString("currency");
        charge.stripeChargeToken = databaseRow.getString("stripe_charge_token");
        charge.status = databaseRow.getInt("status");
        charge.chargedAt = databaseRow.getDate("charged_at");

        return charge;
    }
}
