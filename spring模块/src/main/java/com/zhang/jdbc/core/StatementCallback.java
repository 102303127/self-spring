package com.zhang.jdbc.core;

import java.sql.SQLException;
import java.sql.Statement;

/**
 * 函数式接口，用于执行SQL语句
 *
 * @author zhang
 * @date 2024/8/6
 * @Description
 */
public interface StatementCallback<T> {


    T doInStatement(Statement stmt) throws SQLException;

}
