package com.dudu.database;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.TestPropertySource;
import org.springframework.test.context.junit4.SpringRunner;

import javax.sql.DataSource;
import java.sql.Connection;

@RunWith(SpringRunner.class)
@ContextConfiguration(classes = {DatabaseConfiguration.class})
@TestPropertySource(value = "/com/dudu/database/database.properties")
public class GenericTest {

    @Autowired
    DataSource dataSource;

    @Test
    public void test() throws Exception {
        try (Connection con = dataSource.getConnection()) {
            var sql = "SELECT * FROM stripe_sources";
            var result = DatabaseHelper.getHelper().query(con, sql);
            System.out.println(result.size());
        }
    }
}
