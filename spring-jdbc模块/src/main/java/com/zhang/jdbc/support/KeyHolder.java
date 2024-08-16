package com.zhang.jdbc.support;

import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public interface KeyHolder {

    Number getKey();

    <T> T getKeyAs(Class<T> keyType);

    Map<String, Object> getKeys();


    List<Map<String, Object>> getKeyList();


}
