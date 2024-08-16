package com.zhang.jdbc.core;

import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * 函数式接口，初始化使用lambda表达式
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface ConnectionCallback <T>{

    T doInConnection(Connection con) throws SQLException;

}
