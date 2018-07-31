package com.dudu.payment.stripe;

import com.dudu.database.DatabaseHelper;
import com.dudu.database.DatabaseResult;
import com.dudu.database.DatabaseRow;
import com.dudu.payment.stripe.exceptions.NoChargeException;
import com.dudu.payment.stripe.exceptions.NoCustomerException;
import com.dudu.payment.stripe.exceptions.NoSourceException;
import com.dudu.payment.stripe.exceptions.UserLockedException;
import com.stripe.exception.StripeException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.stereotype.Service;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by chaojiewang on 5/26/18.
 */
@Service
public class StripeService {
    private static final int LOCK_ADD_SOURCE = 100;
    private static final int LOCK_CREATE_CUSTOMER = 200;
    private static final int LOCK_SET_PAYMENT_METHOD = 300;
    private static final int LOCK_CHARGE = 400;
    private static Logger logger = LogManager.getLogger(StripeService.class);
    private DataSource source;
    private DatabaseHelper databaseHelper = DatabaseHelper.getHelper();

    public StripeService(DataSource source) {
        this.source = source;
    }

    /**
     * condition: @sourceId needs to be added by <Link>addSource</Link>
     *
     * @param userId
     * @param sourceId
     * @throws Exception
     */
    public void setPaymentMethod(long userId, String sourceId) throws StripeException, NoCustomerException,
            NoSourceException, UserLockedException, SQLException {
        // get customer ID
        String customerId = getCustomerId(userId);

        if (isLocked(userId))
            throw new UserLockedException(userId, "");

        // make sure database has a record of sourceId
        try (Connection conn = source.getConnection()) {
            String sql = "SELECT * FROM stripe_sources WHERE user_id = ? AND source_id = ?";
            DatabaseResult result = databaseHelper.query(conn, sql, userId, sourceId);
            if (result.isEmpty())
                throw new NoSourceException(userId, sourceId);
        }

        // update stripe, and then database
        StripeProxy.getInstance().setDefaultPaymentMethod(customerId, sourceId);
        try (Connection conn = source.getConnection()) {
            String sql = "UPDATE stripe_sources SET is_default = 1 WHERE user_id = ? AND source_id = ?";
            int count = databaseHelper.update(conn, sql, userId, sourceId);
            if (count != 1) {
                lock(userId, LOCK_SET_PAYMENT_METHOD);
                logger.error("Failed to set default payment method: userId = " + userId + ", sourceId = " + sourceId);
                throw new UserLockedException(userId, "Failed to set default payment method: userId = " + userId + ", sourceId = " + sourceId);
            }
        }
    }

    /**
     *
     * @param userId
     * @param token
     * @param last4
     * @param expMonth
     * @param expYear
     * @param funding
     * @param brand
     * @throws Exception
     */
    public void addSource(long userId, String token, String last4, int expMonth, int expYear, String funding, String brand)
            throws SQLException, NoCustomerException, StripeException, UserLockedException {
        // get customer ID
        String customerId = getCustomerId(userId);

        boolean newCustomer = customerId == null;
        if (newCustomer) {
            // need to create one
            customerId = createCustomer(userId);
        }

        // is it locked?
        if (isLocked(userId))
            throw new UserLockedException(userId, "");

        // create sourceId and save it
        String sourceId = StripeProxy.getInstance().addSource(customerId, token);
        try (Connection conn = source.getConnection()) {
            try (PreparedStatement ps = conn.prepareStatement("INSERT INTO stripe_sources (user_id, source_id, last_four, exp_month, exp_year, funding, brand, is_default) VALUES (?,?,?,?,?,?,?,?)")) {
                ps.setObject(1, userId);
                ps.setObject(2, sourceId);
                ps.setObject(3, last4);
                ps.setObject(4, expMonth != 0 ? expMonth : null);
                ps.setObject(5, expYear != 0 ? expYear : null);
                ps.setObject(6, funding);
                ps.setObject(7, brand);
                ps.setObject(8, newCustomer ? 1 : 0);
                int count = databaseHelper.update(ps);
                if (count != 1) {
                    lock(userId, LOCK_ADD_SOURCE);
                    logger.error("Failed to update StripeSources. user_id = " + userId + ", source_id = " + sourceId);
                    throw new UserLockedException(userId, "Failed to update StripeSources. user_id = " + userId + ", source_id = " + sourceId);
                }
            }
        }
    }

    /**
     *
     * @return could be null
     * @throws Exception
     */
    private String getCustomerId(long userId) throws SQLException, NoCustomerException {
        // get customer ID
        try (Connection conn = source.getConnection()) {
            String sql = "SELECT * FROM stripe_customers WHERE user_id = ? ";
            DatabaseResult result = databaseHelper.query(conn, sql, userId);
            if (result.isEmpty())
                throw new NoCustomerException(userId);

            return result.get(0).getString("customer_id");
        }
    }

    /**
     *
     * @param userId
     * @return customer ID
     * @throws Exception
     */
    synchronized public String createCustomer(long userId) throws SQLException, StripeException, UserLockedException {
        // need to create one
        String customerId = StripeProxy.getInstance().createCustomer("UserId = " + userId);
        try (Connection conn = source.getConnection()) {
            String sql = "INSERT INTO stripe_customers (user_id, customer_id) VALUES (?,?) ";

            int count = databaseHelper.update(conn, sql, userId, customerId);
            if (count != 1) {
                lock(userId, LOCK_CREATE_CUSTOMER);
                logger.error("Failed to update stripe_customers: user_id=" + userId + ", customer_id" + customerId);
                throw new UserLockedException(userId, "Failed to update stripe_customers: user_id=" + userId + ", customer_id" + customerId);
            }
        }

        return customerId;
    }

    /**
     * condition: userId exists in StripeCustomers table.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    protected boolean isLocked(long userId) throws NoCustomerException, SQLException {
        try (Connection conn = source.getConnection()) {
            String sql = "SELECT locked_reason_code FROM stripe_customers WHERE user_id = ?";
            DatabaseResult result = databaseHelper.query(conn, sql, userId);
            if (result.size() == 0)
                throw new NoCustomerException(userId);
            return result.get(0).getInt("locked_reason_code") != 0;
        }
    }

    /**
     * condition: userId exists in StripeCustomers table.
     *
     * @param userId
     * @return
     * @throws Exception
     */
    private void lock(long userId, int reasonCode) throws SQLException {
        try (Connection conn = source.getConnection()) {
            String sql = "UPDATE stripe_customers SET locked_reason_code = ? WHERE user_id = ?";
            int count = databaseHelper.update(conn, sql, reasonCode, userId);
            if (count != 1)
                throw new SQLException("Failed to lock User " + userId + " with reason code " + reasonCode);
        }
    }

    /**
     *
     * @param userId
     * @return
     * @throws Exception
     */
    public StripeCustomer getCustomer(long userId) throws NoCustomerException, SQLException {
        try (Connection conn = source.getConnection()) {
            String sql = "SELECT * FROM stripe_customers WHERE user_id = ?";
            DatabaseResult result = databaseHelper.query(conn, sql , userId);
            if (result.size() != 1)
                throw new NoCustomerException(userId);

            StripeCustomer customer = StripeCustomer.from(result.get(0));

            sql = "SELECT * FROM stripe_sources WHERE user_id = ?";
            result = databaseHelper.query(conn, sql, userId);
            List<StripeSource> sources = new ArrayList<>();
            for (DatabaseRow row : result) {
                StripeSource source = StripeSource.from(row);
                sources.add(source);
            }

            customer.setSources(sources);
            return customer;
        }
    }

    /**
     *
     * @param orderId
     * @param userId
     * @param amount
     * @return StripeChargeToken
     * @throws Exception
     */
    public String charge(long orderId, long userId, long amount) throws NoCustomerException, SQLException, StripeException, UserLockedException {
        StripeCustomer customer = getCustomer(userId);

        if (isLocked(userId))
            throw new IllegalStateException("User " + userId + " is locked");

        String chargeId = StripeProxy.getInstance().charge(customer.getCustomerId(), amount);
        try (Connection conn = source.getConnection()) {
            String sql = "INSERT INTO stripe_charges(user_id, order_id, amount, stripe_charge_token) VALUES (?,?,?,?)";
            int count = databaseHelper.update(conn, sql, userId, orderId, amount, chargeId);
            if (count != 1) {
                logger.warn("Failed to add stripe charge to database to user " + userId + ": stripe_charge_token=" +chargeId);
                lock(userId, LOCK_CHARGE);
                throw new UserLockedException(userId, "Failed to add stripe charge to database to user " + userId + ": stripe_charge_token=" +chargeId);
            }

            return chargeId;
        }
    }

    public StripeCharge getCharge(long userId, String stripeChargeToken) throws SQLException, NoChargeException {
        try (Connection conn = source.getConnection()) {
            String sql = "SELECT * FROM stripe_charges WHERE user_id = ? and stripe_charge_token = ?";
            DatabaseResult result = databaseHelper.query(conn, sql, userId, stripeChargeToken);
            if (result.size() != 1)
                throw new NoChargeException(stripeChargeToken);

            return StripeCharge.from(result.get(0));
        }
    }
}
