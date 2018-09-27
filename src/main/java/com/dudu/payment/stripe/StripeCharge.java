package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;

import java.util.Date;
import java.util.Objects;

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

    public String getStripeChargeToken() {
        return stripeChargeToken;
    }

    public void setStripeChargeToken(String stripeChargeToken) {
        this.stripeChargeToken = stripeChargeToken;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public long getOrderId() {
        return orderId;
    }

    public void setOrderId(long orderId) {
        this.orderId = orderId;
    }

    public long getAmount() {
        return amount;
    }

    public void setAmount(long amount) {
        this.amount = amount;
    }

    public String getCurrency() {
        return currency;
    }

    public void setCurrency(String currency) {
        this.currency = currency;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Date getChargedAt() {
        return chargedAt;
    }

    public void setChargedAt(Date chargedAt) {
        this.chargedAt = chargedAt;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StripeCharge that = (StripeCharge) o;
        return Objects.equals(stripeChargeToken, that.stripeChargeToken);
    }

    @Override
    public int hashCode() {

        return Objects.hash(stripeChargeToken);
    }
}
