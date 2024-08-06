package com.zhang.jdbc.core;

import com.zhang.jdbc.JdbcException;
import com.zhang.jdbc.datasource.DataSourceUtils;
import com.zhang.jdbc.support.JdbcAccessor;
import com.zhang.jdbc.support.KeyHolder;

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


    @Override
    public void execute(String sql) {
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
                    /* TODO RowMapperResultSetExtractor<Map<String, Object>> rse =
                            new RowMapperResultSetExtractor<>(getColumnMapRowMapper(), 1);
                   generatedKeys.addAll(result(rse.extractData(keys)));
                */}
                finally {
    //  TODo              JdbcUtils.closeResultSet(keys);
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
}
