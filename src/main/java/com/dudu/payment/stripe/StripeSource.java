package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;
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
        stripeSource.userId = row.getLong("user_id");
        stripeSource.sourceId = row.getString("Source_id");
        stripeSource.isDefault = row.getInt("is_default") != 0;
        stripeSource.createdAt = row.getDate("created_at");
        stripeSource.lastFour = row.getString("last_four");
        stripeSource.expMonth = row.getInt("exp_month");
        stripeSource.expYear = row.getInt("exp_year");
        stripeSource.funding = row.getString("funding");
        stripeSource.brand = row.getString("brand");

        return stripeSource;
    }
}
