package com.zhang.jdbc;

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
}
