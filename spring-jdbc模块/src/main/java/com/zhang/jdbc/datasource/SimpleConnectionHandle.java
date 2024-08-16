package com.zhang.jdbc.datasource;

import java.sql.Connection;
import org.springframework.util.Assert;

public class SimpleConnectionHandle implements ConnectionHandle {
    private final Connection connection;

    public SimpleConnectionHandle(Connection connection) {
        Assert.notNull(connection, "Connection must not be null");
        this.connection = connection;
    }

    public Connection getConnection() {
        return this.connection;
    }

    public String toString() {
        return "SimpleConnectionHandle: " + this.connection;
    }
}
