package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;
import java.util.Date;

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

    ////////////////////////////////////
    public long getUserId() {
        return userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public long getAmount() {
        return amount;
    }

    public String getCurrency() {
        return currency;
    }

    public String getStripeChargeToken() {
        return stripeChargeToken;
    }

    public int getStatus() {
        return status;
    }

    public Date getChargedAt() {
        return chargedAt;
    }
}
