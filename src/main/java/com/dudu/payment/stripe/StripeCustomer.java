package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;

import java.util.Date;
import java.util.List;

/**
 * Created by chaojiewang on 5/27/18.
 */
public class StripeCustomer {
    private long userId;
    private String customerId;
    private int lockedReasonCode;
    private Date createdAt;
    private StripeSource defaultSource;
    private List<StripeSource> sources;

    public static StripeCustomer from(DatabaseRow databaseRow) {
        StripeCustomer stripeCustomer = new StripeCustomer();
        stripeCustomer.userId = databaseRow.getLong("user_id");
        stripeCustomer.customerId = databaseRow.getString("customer_id");
        stripeCustomer.lockedReasonCode = databaseRow.getInt("locked_reason_code");
        stripeCustomer.createdAt = databaseRow.getDate("created_at");

        return stripeCustomer;
    }

    ///////////////////////////////////////
    public long getUserId() {
        return userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public int isLockedReasonCode() {
        return lockedReasonCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public StripeSource getDefaultSource() {
        return defaultSource;
    }

    public List<StripeSource> getSources() {
        return sources;
    }

    public void setSources(List<StripeSource> sources) {
        this.sources = sources;

        if (sources != null) {
            for (StripeSource source : sources) {
                if (source.getUserId() == userId && source.isDefault())
                    defaultSource = source;
            }
        }
    }

    public boolean isLocked() {
        return lockedReasonCode != 0;
    }

    public int getLockedReasonCode() {
        return lockedReasonCode;
    }

}
