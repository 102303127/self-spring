package com.zhang.jdbc.core;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * @author zhang
 * @date 2024/8/15
 * @Description
 */
public interface RowCallbackHandler {

    void processRow(ResultSet rs) throws SQLException;

}
