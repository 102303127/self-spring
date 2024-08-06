package com.zhang.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 设置管理sql语句的参数
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface PreparedStatementSetter {

    void setValues(PreparedStatement ps) throws SQLException;
}
