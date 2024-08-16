package com.zhang.jdbc.core;

import com.zhang.jdbc.JdbcException;
import com.zhang.jdbc.datasource.DataSourceUtils;
import com.zhang.jdbc.support.JdbcAccessor;
import com.zhang.jdbc.support.JdbcUtils;
import com.zhang.jdbc.support.KeyHolder;
import org.springframework.dao.DataAccessException;
import org.springframework.lang.Nullable;
import org.springframework.util.Assert;

import javax.sql.DataSource;
import java.sql.*;
import java.util.List;
import java.util.Map;

/**
 *
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public class JdbcTemplate extends JdbcAccessor implements JdbcOperations{

    /**
     * 该参数的目的是为了减少网络交互次数设计的。
     * 在访问 ResultSet时，如果它每次只从服务器上读取一行数据，会产生大量开销。
     * FetchSize 参数的作用是 在调用 rs.next时， ResultSet会一次性从服务器上取多少行数据回来。
     * 这样在下次 rs.next 时，他可以直接从内存中获取数据而不需要网络交互，提高了效率。
     * 但是这个设置可能会被某些jdbc驱动忽略。
     * 设置过大也会造成内存上升。
     */
    private int fetchSize = -1;

    /**
     * 作用是将此 Statement 对象生成的所有
     *  ResultSet 对象可以包含的最大行数限制设置为给定值。
     */
    private int maxRows = -1;



    private int queryTimeout = -1;
    public JdbcTemplate() {
    }

    public JdbcTemplate(DataSource dataSource) {
        setDataSource(dataSource);
        afterPropertiesSet();
    }


    @Override
    public <T> T execute(ConnectionCallback<T> action) throws SQLException {

        // 从数据源中获取数据连接
        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        try {
            // 创建关闭抑制 Connection 代理，同时准备返回的 Statements。
            Connection conToUse = createConnectionProxy(con);
            return action.doInConnection(conToUse);
        }
        catch (SQLException ex) {
            DataSourceUtils.releaseConnection(con, getDataSource());
            throw new JdbcException("ConnectionCallback", ex);
        }
        finally {
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }

    @Override
    public <T> T execute(PreparedStatementCreator psc, PreparedStatementCallback<T> action) throws SQLException {

        // 从数据源中获取数据连接
        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        PreparedStatement ps = null;
        try {
            // 调用回调函数 createPreparedStatement()
            // 创建Statement
            ps = psc.createPreparedStatement(con);
            // 应用一些设置
            applyStatementSettings(ps);

            // 调用回调函数 doInPreparedStatement()
            T result = action.doInPreparedStatement(ps);
            // 警告处理
            // handleWarnings(ps);
            return result;
        }
        catch (SQLException ex) {
            throw new JdbcException("预编译出现错误：", ex);
        }
        finally {
            DataSourceUtils.releaseConnection(con, getDataSource());
        }
    }


    //-------------------------------------------------------------------------
    // Methods dealing with static SQL (java.sql.Statement)
    //-------------------------------------------------------------------------

    private <T> T execute(StatementCallback<T> action, boolean closeResources) throws JdbcException, SQLException {

        Connection con = DataSourceUtils.getConnection(obtainDataSource());
        Statement stmt = null;

        Object var12;
        try {
            stmt = con.createStatement();
            applyStatementSettings(stmt);
            T result = action.doInStatement(stmt);

            var12 = result;
        }
        catch (SQLException ex) {
            // Release Connection early, to avoid potential connection pool deadlock
            // in the case when the exception translator hasn't been initialized yet.
            String sql = getSql(action);
            JdbcUtils.closeStatement(stmt);
            stmt = null;
            DataSourceUtils.releaseConnection(con, getDataSource());
            con = null;
            throw new JdbcException("StatementCallback", sql, ex);
        }
        finally {
            if (closeResources) {
                JdbcUtils.closeStatement(stmt);
                DataSourceUtils.releaseConnection(con, getDataSource());
            }
        }

        return (T) var12;
    }

    @Override
    public <T> T execute(StatementCallback<T> action) throws SQLException {
        return execute(action, true);
    }

    @Override
    public void execute(final String sql) throws SQLException {

        /**
         * Callback to execute the statement.
         */
        class ExecuteStatementCallback implements StatementCallback<Object>, SqlProvider {
            @Override
            public Object doInStatement(Statement stmt) throws SQLException {
                stmt.execute(sql);
                return null;
            }
            @Override
            public String getSql() {
                return sql;
            }
        }

        execute(new ExecuteStatementCallback(), true);
    }


    public <T> T query(final String sql, final ResultSetExtractor<T> rse) throws DataAccessException, SQLException {

        class QueryStatementCallback implements StatementCallback<T>, SqlProvider {
            QueryStatementCallback() {
            }

            @Nullable
            public T doInStatement(Statement stmt) throws SQLException {
                ResultSet rs = null;

                Object var3;
                try {
                    rs = stmt.executeQuery(sql);
                    var3 = rse.extractData(rs);
                } finally {
                    JdbcUtils.closeResultSet(rs);
                }

                return (T) var3;
            }

            public String getSql() {
                return sql;
            }
        }

        return this.execute(new QueryStatementCallback(), true);
    }


    public void query(String sql, RowCallbackHandler rch) throws DataAccessException, SQLException {
        this.query((String)sql, (ResultSetExtractor)(new RowCallbackHandlerResultSetExtractor(rch)));
    }


    public <T> List<T> query(String sql, RowMapper<T> rowMapper) throws DataAccessException, SQLException {
        return (List)this.query((String)sql, (ResultSetExtractor)(new RowMapperResultSetExtractor(rowMapper)));
    }

    @Override
    public int update(String sql) {
        return 0;
    }



    /**
     * 使用给定的PreparedStatementCreator和KeyHolder对象执行更新操作，并返回受影响的行数。
     *
     * @param psc PreparedStatement的创建器
     * @param generatedKeyHolder 用于存储生成键的KeyHolder对象
     * @return
     */
    @Override
    public int update(final PreparedStatementCreator psc, final KeyHolder generatedKeyHolder)
            throws JdbcException, SQLException {

        return execute(psc, ps -> {
            int rows = ps.executeUpdate();
            List<Map<String, Object>> generatedKeys = generatedKeyHolder.getKeyList();
            generatedKeys.clear();
            ResultSet keys = ps.getGeneratedKeys();
            if (keys != null) {
                try {
                      RowMapperResultSetExtractor<Map<String, Object>> rse =
                            new RowMapperResultSetExtractor<>(getColumnMapRowMapper(), 1);
                      generatedKeys.addAll(rse.extractData(keys));
                }
                finally {
                  JdbcUtils.closeResultSet(keys);
                }
            }
            return rows;
        });
    }





    protected Connection createConnectionProxy(Connection con) {
        return null;
    }


    /**
     * 应用用户设定的数据参数
     *
     * @param stmt
     * @throws SQLException
     */
    protected void applyStatementSettings(Statement stmt) throws SQLException {
        int fetchSize = getFetchSize();
        // 设置 fetchSize 属性
        if (fetchSize != -1) {
            stmt.setFetchSize(fetchSize);
        }
        // 设置 maxRows 属性
        int maxRows = getMaxRows();
        if (maxRows != -1) {
            stmt.setMaxRows(maxRows);
        }
        DataSourceUtils.applyTimeout(stmt, getDataSource(), getQueryTimeout());
    }

    private static String getSql(Object sqlProvider) {
        if (sqlProvider instanceof SqlProvider) {
            return ((SqlProvider) sqlProvider).getSql();
        }
        else {
            return null;
        }
    }

    public int getFetchSize() {
        return fetchSize;
    }

    public void setFetchSize(int fetchSize) {
        this.fetchSize = fetchSize;
    }

    public int getMaxRows() {
        return maxRows;
    }

    public void setMaxRows(int maxRows) {
        this.maxRows = maxRows;
    }

    public int getQueryTimeout() {
        return queryTimeout;
    }

    public void setQueryTimeout(int queryTimeout) {
        this.queryTimeout = queryTimeout;
    }


    protected RowMapper<Map<String, Object>> getColumnMapRowMapper() {
        return new ColumnMapRowMapper();
    }








    private static class RowCallbackHandlerResultSetExtractor implements ResultSetExtractor<Object> {
        private final RowCallbackHandler rch;

        public RowCallbackHandlerResultSetExtractor(RowCallbackHandler rch) {
            this.rch = rch;
        }

        public Object extractData(ResultSet rs) throws SQLException {
            while(rs.next()) {
                this.rch.processRow(rs);
            }

            return null;
        }
    }
}
