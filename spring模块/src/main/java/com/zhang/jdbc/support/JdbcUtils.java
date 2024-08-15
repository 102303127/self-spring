package com.zhang.jdbc.support;

import com.zhang.jdbc.JdbcException;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static com.mysql.cj.conf.PropertyKey.logger;

/**
 * @author zhang
 * @date 2024/8/7
 * @Description
 */
public class JdbcUtils {


    public static void closeConnection( Connection con) {
        if (con != null) {
            try {
                con.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }


    public static void closeStatement( Statement stmt) {
        if (stmt != null) {
            try {
                stmt.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }

    /**
     * Close the given JDBC ResultSet and ignore any thrown exception.
     * This is useful for typical finally blocks in manual JDBC code.
     * @param rs the JDBC ResultSet to close (may be {@code null})
     */
    public static void closeResultSet( ResultSet rs) {
        if (rs != null) {
            try {
                rs.close();
            }
            catch (Throwable ex) {
                throw new JdbcException(ex.getMessage());
            }
        }
    }
}
