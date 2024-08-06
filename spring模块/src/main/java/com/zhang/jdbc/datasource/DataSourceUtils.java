package com.zhang.jdbc.datasource;

import com.zhang.jdbc.JdbcException;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public abstract class DataSourceUtils {

    //==================获取连接资源==================
    public static Connection getConnection(DataSource dataSource) throws SQLException {
        return doGetConnection(dataSource);
    }

    private static Connection doGetConnection(DataSource dataSource) throws SQLException {
        // .....
        Connection con = fetchConnection(dataSource);
        // .....
        return con;
    }

    private static Connection fetchConnection(DataSource dataSource) throws SQLException {
        Connection con = dataSource.getConnection();

        if (con == null){
            throw new JdbcException("获取数据连接异常");
        }

        return con;
    }


    //==================释放连接资源==================
    public static void releaseConnection(Connection con, DataSource dataSource) throws SQLException {
        doReleaseConnection(con, dataSource);
    }

    private static void doReleaseConnection(Connection con, DataSource dataSource) throws SQLException {
        if (con == null) {
            return;
        }
        // doCloseConnection(con, dataSource);
        con.close();
    }

    //==================设置超时时间==================
    public static void applyTimeout(Statement stmt, DataSource dataSource, int timeout) throws SQLException {
        stmt.setQueryTimeout(timeout);
    }
}
