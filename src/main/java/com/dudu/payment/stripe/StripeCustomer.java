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
        stripeCustomer.userId = databaseRow.getLong("UserId");
        stripeCustomer.customerId = databaseRow.getString("CustomerId");
        stripeCustomer.lockedReasonCode = databaseRow.getInt("LockedReasonCode");
        stripeCustomer.createdAt = databaseRow.getDate("CreatedAt");

        return stripeCustomer;
    }

    ///////////////////////////////////////
    public void setSources(List<StripeSource> sources) {
        this.sources = sources;

        if (sources != null) {
            for (StripeSource source : sources) {
                if (source.getUserId() == userId && source.isDefault())
                    defaultSource = source;
            }
        }
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public int getLockedReasonCode() {
        return lockedReasonCode;
    }

    public void setLockedReasonCode(int lockedReasonCode) {
        this.lockedReasonCode = lockedReasonCode;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public StripeSource getDefaultSource() {
        return defaultSource;
    }

    public void setDefaultSource(StripeSource defaultSource) {
        this.defaultSource = defaultSource;
    }

    public List<StripeSource> getSources() {
        return sources;
    }
}
