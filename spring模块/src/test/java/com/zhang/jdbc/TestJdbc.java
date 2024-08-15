package com.zhang.jdbc;

import com.zhang.jdbc.core.JdbcTemplate;
import com.zhang.jdbc.datasource.DriverManagerDataSource;
import org.junit.Test;

import javax.sql.DataSource;
import java.sql.SQLException;

/**
 * @author zhang
 * @date 2024/8/7
 * @Description
 */
public class TestJdbc {



    public void test() throws SQLException {


        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());
        String sql = "insert into `people`(`id`,`name`) values('1','123456')";
        jdbcTemplate.execute(sql);
    }

    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();
        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/school?useUnicode=true&characterEncoding=utf8&useSSL=true");
        ds.setUsername("root");
        ds.setPassword("zy112500");
        return ds;

    }
}
