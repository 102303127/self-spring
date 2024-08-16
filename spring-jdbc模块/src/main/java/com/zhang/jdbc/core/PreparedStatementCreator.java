package com.zhang.jdbc.core;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 *JdbcTemplate 类使用的两个中心回调接口之一。
 * 此接口在给定连接的情况下创建一个 PreparedStatement，
 * 该连接由 JdbcTemplate 类提供。
 * 实现负责提供 SQL 和任何必要的参数。
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface PreparedStatementCreator {

    PreparedStatement createPreparedStatement(Connection con) throws SQLException;
}
