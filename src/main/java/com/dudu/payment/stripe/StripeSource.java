package com.dudu.payment.stripe;

import com.dudu.database.DatabaseRow;

import java.util.Date;
import java.util.Objects;

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

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }

    public String getSourceId() {
        return sourceId;
    }

    public void setSourceId(String sourceId) {
        this.sourceId = sourceId;
    }

    public boolean isDefault() {
        return isDefault;
    }

    public void setDefault(boolean aDefault) {
        isDefault = aDefault;
    }

    public Date getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(Date createdAt) {
        this.createdAt = createdAt;
    }

    public String getLastFour() {
        return lastFour;
    }

    public void setLastFour(String lastFour) {
        this.lastFour = lastFour;
    }

    public int getExpMonth() {
        return expMonth;
    }

    public void setExpMonth(int expMonth) {
        this.expMonth = expMonth;
    }

    public int getExpYear() {
        return expYear;
    }

    public void setExpYear(int expYear) {
        this.expYear = expYear;
    }

    public String getFunding() {
        return funding;
    }

    public void setFunding(String funding) {
        this.funding = funding;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        StripeSource that = (StripeSource) o;
        return Objects.equals(sourceId, that.sourceId);
    }

    @Override
    public int hashCode() {

        return Objects.hash(sourceId);
    }
}
