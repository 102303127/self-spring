package com.zhang.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public interface RowMapper<T> {

    T mapRow(ResultSet rs, int rowNum) throws SQLException;
}
