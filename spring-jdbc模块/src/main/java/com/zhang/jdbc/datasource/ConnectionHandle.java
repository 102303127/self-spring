package com.zhang.jdbc.datasource;

import java.sql.Connection;

/**
 * 函数式接口
 */
public interface ConnectionHandle {
    Connection getConnection();

    default void releaseConnection(Connection con) {
    }
}
