package com.zhang.jdbc.core;

import com.zhang.jdbc.JdbcException;
import com.zhang.jdbc.support.KeyHolder;

import java.sql.SQLException;

/**
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface JdbcOperations {

    <T> T execute(ConnectionCallback<T> action) throws SQLException;

    <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws SQLException;

    /**
     *
     * @param sql
     */
    void execute(String sql);


    /**
     * 使用 PreparedStatementCreator 发出更新语句，以提供 SQL 和任何必需的参数。
     * 生成的密钥将被放入给定的 KeyHolder 中。
     *
     * @param psc
     * @param generatedKeyHolder
     * @return
     * @throws JdbcException
     */
    int update(PreparedStatementCreator psc, KeyHolder generatedKeyHolder) throws JdbcException, SQLException;
    /**
     * 发出单个 SQL 更新操作（例如插入、更新或删除语句）。
     * @param sql
     * @return
     */
    int update(String sql);

}
