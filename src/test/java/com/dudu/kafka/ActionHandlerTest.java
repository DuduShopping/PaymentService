package com.dudu.kafka;

import com.dudu.database.DatabaseConfiguration;
import org.json.JSONObject;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@TestPropertySource("/com/dudu/kafka/actionHandler.properties")
public class ActionHandlerTest {

    @Autowired
    private DataSource dataSource;
    private ActionHandler actionHandler;

    @Before
    public void setup() {
        actionHandler = new ActionHandler(dataSource);
    }

    @Test
    public void handleOrderNew() throws Exception {
        JSONObject orderNew = new JSONObject();
        orderNew.put("orderId", 1);
        orderNew.put("amount", 10*100);
        actionHandler.handle(ActionHandler.ACTION_NEW_ORDER, orderNew.toString());
    }

    @Test
    public void handleOrderUpdate() throws Exception {
        JSONObject orderUpdate = new JSONObject();
        orderUpdate.put("orderId", 1);
        orderUpdate.put("amount", 11*100);
        actionHandler.handle(ActionHandler.ACTION_UPDATE_ORDER, orderUpdate.toString());
    }
}
