package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;
import lombok.*;

import java.util.Date;
import java.util.List;

/**
 * Created by chaojiewang on 5/27/18.
 */

@NoArgsConstructor
public class StripeCustomer {

    @Getter @Setter
    private long userId;

    @Getter @Setter
    private String customerId;

    @Getter @Setter
    private int lockedReasonCode;

    @Getter @Setter
    private Date createdAt;

    @Getter @Setter
    private StripeSource defaultSource;

    @Getter
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
}
