package com.dudu.database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.*;

/**
 * Created by chaojiewang on 1/30/18.
 */
public class StoredProcedure {
    private Connection con;
    private String spName;
    private Map<String, Object> parameters;

    public StoredProcedure(Connection con, String spName) {
        this.con = con;
        this.spName = spName;
        this.parameters = new LinkedHashMap<>();
    }

    public void addParameter(String name, Object value) {
        if (value instanceof Date)
            parameters.put(name, new java.sql.Timestamp(((Date) value).getTime()));
        else if (value instanceof Character)
            parameters.put(name, value.toString());
        else
            parameters.put(name, value);
    }

    public DatabaseResult query() throws SQLException {
        StringBuilder sql = new StringBuilder();
        List<String> keys = new ArrayList<>(parameters.keySet());
        sql.append("EXEC  ").append(spName).append(" ");
        if (parameters.size() != 0) {
            for (int i = 0; i < keys.size(); i++) {
                if (i == keys.size()-1)
                    sql.append("@").append(keys.get(i)).append(" = ?");
                else
                    sql.append("@").append(keys.get(i)).append(" = ?,");
            }
        }

        try (PreparedStatement sp = con.prepareStatement(sql.toString())) {
            for (int i = 0; i < keys.size(); i++) {
                String param = keys.get(i);
                Object val = parameters.get(param);
                if (val instanceof Character)
                    val = val.toString();
                sp.setObject(i+1, val);
            }

            return DatabaseHelper.getHelper().query(sp);
        }
    }
}
