package com.dudu.kafka;

import com.dudu.database.DatabaseHelper;
import org.json.JSONException;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

public class ActionHandler {
    private static final Logger logger = LoggerFactory.getLogger(ActionHandler.class);
    public static final String ACTION_NEW_ORDER = "orderNew";
    public static final String ACTION_UPDATE_ORDER = "orderUpdate";

    private DataSource dataSource;
    private DatabaseHelper databaseHelper;

    public ActionHandler(DataSource dataSource) {
        this.dataSource = dataSource;
        this.databaseHelper = DatabaseHelper.getHelper();
    }

    public void handle(String actionType, String data) {
        if (actionType.equals(ACTION_NEW_ORDER)) {
            handleOrderNew(data);
        } else if (actionType.equals(ACTION_UPDATE_ORDER)) {
            handleOrderUpdate(data);
        } else {
            logger.warn("Unknown actionType: " + actionType);
        }
    }

    private void handleOrderUpdate(String data) {
        try (Connection con = dataSource.getConnection()){
            JSONObject orderUpdate = new JSONObject(new JSONTokener(data));
            long orderId = orderUpdate.getLong("OrderId");
            long amount =  orderUpdate.getLong("Amount");

            String update = "UPDATE Orders SET PaymentDue = ? WHERE OrderId = ? ";
            int count = databaseHelper.update(con, update, amount, orderId);
            if (count != 1)
                throw new SQLException("Expect count = 1");
        } catch (SQLException | JSONException e) {
            logger.warn("Failed to handle order update.", e);
        }

    }

    private void handleOrderNew(String data) {
        try (Connection con = dataSource.getConnection()){
            JSONObject orderNew = new JSONObject(new JSONTokener(data));
            long orderId = orderNew.getLong("OrderId");
            long amount =  orderNew.getLong("Amount");

            String insert = "INSERT INTO Orders(OrderId, PaymentDue) VALUES (?,?) ";
            int count = databaseHelper.update(con, insert, orderId, amount);
            if (count != 1)
                throw new SQLException("Expect count = 1");
        } catch (SQLException | JSONException e) {
            logger.warn("Failed to handle new order.", e);
        }
    }
}
