package com.dudu.database;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.sql.*;
import java.util.Date;

/**
 * Created by chaojiewang on 1/29/18.
 */
public class DatabaseHelper {
    private static final Logger logger = LogManager.getLogger(DatabaseHelper.class);

    public static DatabaseHelper getHelper() {
        return helper;
    }

    private static DatabaseHelper helper = new DatabaseHelper();

    private DatabaseHelper() {}

    public DatabaseResult query(PreparedStatement ps) throws SQLException {
        try (ResultSet rs = ps.executeQuery()) {
            DatabaseResult result = new DatabaseResult();
            while (rs.next())
                result.add(new DatabaseRow(rs));

            return result;
        }
    }

    public DatabaseResult query(Connection con, String sql, Object... parameters) throws SQLException {
        logger.info("query: " + sql);
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= parameters.length; i++) {
                Object param = parameters[i-1];
                if (param instanceof Character)
                    ps.setObject(i, param.toString());
                else if (param instanceof Date)
                    ps.setObject(i, new Timestamp(((Date) param).getTime()));
                else
                    ps.setObject(i, param);
            }

            return query(ps);
        }
    }

    public boolean notEmpty(Connection con, String sql, Object... parameters) throws SQLException {
        DatabaseResult result = query(con, sql, parameters);
        return !result.isEmpty();
    }

    public DatabaseResult query(Connection con, String sql) throws SQLException {
        return query(con, sql, new Object[]{});
    }

    /**
     * must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
     *
     * @param con
     * @param sql
     * @param parameters
     * @return either (1) the row count for SQL Data Manipulation Language (DML) statements
     *         or (2) 0 for SQL statements that return nothing
     * @throws SQLException
     */
    public int update(Connection con, String sql, Object... parameters) throws SQLException {
        logger.info("update: " + sql);
        try (PreparedStatement ps = con.prepareStatement(sql)) {
            for (int i = 1; i <= parameters.length; i++) {
                Object param = parameters[i-1];
                if (param instanceof Character)
                    ps.setObject(i, param.toString());
                else if (param instanceof Date)
                    ps.setObject(i, new Timestamp(((Date) param).getTime()));
                else
                    ps.setObject(i, param);
            }

            return ps.executeUpdate();
        }
    }

    /**
     * must be an SQL Data Manipulation Language (DML) statement, such as <code>INSERT</code>, <code>UPDATE</code> or
     * <code>DELETE</code>; or an SQL statement that returns nothing,
     * such as a DDL statement.
     *
     * @param ps
     * @return
     * @throws SQLException
     */
    public int update(PreparedStatement ps) throws SQLException {
        return ps.executeUpdate();
    }

    /**
     *
     * @param con
     * @param sql
     * @param generatedKeys
     * @param parameters
     * @return
     * @throws SQLException
     */
    public DatabaseResult updateAndGetKey(Connection con, String sql, String[] generatedKeys, Object... parameters) throws SQLException {
        logger.info("execUpdate: " + sql);
        try (PreparedStatement ps = con.prepareStatement(sql, generatedKeys)) {
            for (int i = 1; i <= parameters.length; i++) {
                Object param = parameters[i-1];
                if (param instanceof Character)
                    ps.setObject(i, param.toString());
                else if (param instanceof Date)
                    ps.setObject(i, new Timestamp(((Date) param).getTime()));
                else
                    ps.setObject(i, param);
            }

            ps.executeUpdate();
            ResultSet rs = ps.getGeneratedKeys();
            DatabaseResult result = new DatabaseResult();
            while (rs.next()) {
                DatabaseRow row = new DatabaseRow();
                for (int i = 1; i <= generatedKeys.length; i++)
                    row.put(generatedKeys[i-1], rs.getObject(i));
                result.add(row);
            }

            return result;
        }
    }
}
