package com.zhang.jdbc.core;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * 对 PreparedStatement （预编译） 进行操作的代码的通用回调接口。
 * 函数式接口，通过lambda表达式进行初始化
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface PreparedStatementCallback<T> {


    /**
     * 函数会在调用execute的方法的里面定义
     *
     *
     *
     * @param ps
     * @return
     * @throws SQLException
     */
    T doInPreparedStatement(PreparedStatement ps) throws SQLException;
}
