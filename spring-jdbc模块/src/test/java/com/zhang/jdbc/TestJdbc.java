package com.zhang.jdbc;

import com.zhang.jdbc.core.BeanPropertyRowMapper;
import com.zhang.jdbc.core.JdbcTemplate;
import com.zhang.jdbc.core.RowMapper;
import com.zhang.jdbc.datasource.DriverManagerDataSource;
import org.junit.Test;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

import javax.sql.DataSource;
import java.sql.SQLException;
import java.util.List;

/**
 * @author zhang
 * @date 2024/8/7
 * @Description
 */
public class TestJdbc {


    /**
     * 基于容器获取数据源
     * @throws Exception
     */
    @Test
    public void insert() throws Exception {

        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:jdbc.xml");

        JdbcTemplate jdbcTemplate1 = applicationContext.getBean("jdbcTemplate", JdbcTemplate.class);

        System.out.println(jdbcTemplate1);

        String sql = "insert into `people`(`id`,`name`) values('1','123456')";

        jdbcTemplate1.execute(sql);
    }


    /**
     * 代码方式获取数据源
     * @throws SQLException
     */
    @Test
    public void execute() throws SQLException {
        JdbcTemplate jdbcTemplate = new JdbcTemplate(dataSource());

        RowMapper<People> rowMapper = new BeanPropertyRowMapper<>(People.class);

        String sql = "select * from people";

        List<People> query = jdbcTemplate.query(sql, rowMapper);

        for (People people : query) {
            System.out.println(people);
        }
    }


    /**
     * 代码方式获取数据源
     * @return
     */
    public DataSource dataSource() {
        DriverManagerDataSource ds = new DriverManagerDataSource();

        ds.setDriverClassName("com.mysql.cj.jdbc.Driver");
        ds.setUrl("jdbc:mysql://localhost:3306/school?useUnicode=true&characterEncoding=utf8&useSSL=true");
        ds.setUsername("root");
        ds.setPassword("zy112500");

        return ds;
    }
}
