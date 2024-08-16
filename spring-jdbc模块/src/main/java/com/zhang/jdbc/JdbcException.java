package com.zhang.jdbc;

import java.sql.SQLException;

/**
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public class JdbcException extends RuntimeException{

    public JdbcException(String message) {
        super(message);
    }

    public JdbcException(String message, Throwable cause) {
        super(message, cause);
    }

    public JdbcException(String statementCallback, String sql, SQLException ex) {
        super(statementCallback + " SQL [" + sql + "] cause: " + ex.getMessage(), ex);
    }
}
