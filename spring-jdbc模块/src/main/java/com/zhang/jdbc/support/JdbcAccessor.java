package com.zhang.jdbc.support;


import com.zhang.jdbc.JdbcException;
import org.springframework.beans.factory.InitializingBean;

import javax.sql.DataSource;

/**
 * 定义 DataSource 和异常转换器等常见属性。
 *
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public abstract class JdbcAccessor implements InitializingBean {

    private DataSource dataSource;

    public DataSource getDataSource() {
        return dataSource;
    }

    public void setDataSource(DataSource dataSource) {
        this.dataSource = dataSource;
    }

    /**
     * 获取DataSource以供实际使用。
     *
     * @return
     */
    protected DataSource obtainDataSource() {
        DataSource dataSource = getDataSource();
        return dataSource;
    }



    /**
     * 初始化方法，在Bean属性设置完成后调用。
     * 检验属性dataSource是否设置，如果没有设置则抛出IllegalArgumentException异常。
     *
     * @throws IllegalArgumentException 如果数据源（dataSource）属性未设置，则抛出IllegalArgumentException异常。
     */
    @Override
    public void afterPropertiesSet() {
        if (getDataSource() == null) {
            throw new JdbcException("dataSource资源没有设置");
        }
    }

}
