package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@ToString
@EqualsAndHashCode(of = {"sourceId"})
public class StripeSource {
    private long userId;
    private String sourceId;
    private boolean isDefault;
    private Date createdAt;
    private String lastFour;
    private int expMonth;
    private int expYear;
    private String funding;
    private String brand;

    public static StripeSource from(DatabaseRow row) {
        StripeSource stripeSource = new StripeSource();
        stripeSource.userId = row.getLong("UserId");
        stripeSource.sourceId = row.getString("SourceId");
        stripeSource.isDefault = row.getInt("IsDefault") != 0;
        stripeSource.createdAt = row.getDate("CreatedAt");
        stripeSource.lastFour = row.getString("LastFour");
        stripeSource.expMonth = row.getInt("ExpMonth");
        stripeSource.expYear = row.getInt("ExpYear");
        stripeSource.funding = row.getString("Funding");
        stripeSource.brand = row.getString("Brand");

        return stripeSource;
    }
}
