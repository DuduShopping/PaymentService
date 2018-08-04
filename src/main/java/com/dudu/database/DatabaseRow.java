package com.dudu.database;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Date;
import java.util.LinkedHashMap;

/**
 * Created by chaojiewang on 1/29/18.
 */
public class DatabaseRow extends LinkedHashMap<String, Object> {
    private static final Logger logger = LoggerFactory.getLogger(DatabaseRow.class);

    public DatabaseRow() {}

    public DatabaseRow(ResultSet rs) throws SQLException {
        ResultSetMetaData meta = rs.getMetaData();
        for (int i = 1; i <= meta.getColumnCount(); i++) {
            String columnName = meta.getColumnName(i);
            put(columnName, rs.getObject(columnName));
        }
    }

    public String getString(String col, String defaultVal) {
        if (!containsKey(col) || !(get(col) instanceof String)) {
            logger.warn("column " + col + " of type " + String.class.getName() + " not found.");
            return defaultVal;
        }

        return (String) get(col);
    }

    public String getString(String col) {
        return getString(col, "");
    }

    public int getInt(String col, int defaultVal) {
        if (!containsKey(col) || !(get(col) instanceof Integer)) {
            logger.warn("column " + col + " of type " + Integer.class.getName() + " not found.");
            return defaultVal;
        }

        return (Integer) get(col);
    }

    public int getInt(String col) {
        return getInt(col, 0);
    }

    public long getLong(String col, long defaultVal) {
        if (!containsKey(col)) {
            logger.warn("column " + col + " of type " + Long.class.getName() + " not found.");
            return defaultVal;
        }

        if (get(col) instanceof Long)
            return (Long) get(col);
        else if (get(col) instanceof BigDecimal)
            return ((BigDecimal) get(col)).longValue();
        else {
            logger.warn("column " + col + " of type " + Long.class.getName() + " not found.");
            return defaultVal;
        }
    }

    public long getLong(String col) {
        return getLong(col, 0);
    }

    public double getDouble(String col, double defaultVal) {
        if (!containsKey(col) || (!(get(col) instanceof Double) && !(get(col) instanceof BigDecimal))) {
            logger.warn("column " + col + " of type " + Double.class.getName() + " not found.");
            return defaultVal;
        }

        Object number = get(col);
        if (number instanceof Double) {
            return (Double) get(col);
        } else if (number instanceof BigDecimal) {
            return ((BigDecimal) number).doubleValue();
        } else {
            return defaultVal;
        }
    }

    public double getDouble(String col) {
        return getDouble(col, 0);
    }

    public Date getDate(String col, Date defaultVal) {
        Object obj = get(col);
        if (obj == null || (!(obj instanceof Timestamp) && !(obj instanceof Date))) {
            logger.warn("column " + col + " of type " + Date.class.getName() + " not found.");
            return defaultVal;
        }

        return new Date(((Date) obj).getTime());
    }


    public Date getDate(String col) {
        return getDate(col, null);
    }

    public char getChar(String col, char defaultVal) {
        return getString(col, String.valueOf(defaultVal)).charAt(0);
    }

    public char getChar(String col) {
        return getChar(col, '\0');
    }

    public Object getObject(String col, Object defaultVal) {
        if (!containsKey(col)) {
            logger.warn("column " + col + " of type " + Object.class.getName() + " not found.");
            return defaultVal;
        }

        return get(col);
    }

    public Object getObject(String col) {
        return getObject(col, null);
    }

    public boolean getBool(String col, boolean defaultVal) {
        if (!containsKey(col) || !(get(col) instanceof Boolean)) {
            return defaultVal;
        }

        return (Boolean) get(col);
    }

    public boolean getBool(String col) {
        return getBool(col, false);
    }

}
