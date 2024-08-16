package com.zhang.jdbc.support;

import com.zhang.jdbc.JdbcException;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public class GeneratedKeyHolder implements KeyHolder{


    private final List<Map<String, Object>> keyList;

    public GeneratedKeyHolder() {
        this.keyList = new ArrayList<>(1);
    }

    /**
     * 使用给定的键值列表创建 GeneratedKeyHolder 实例
     *
     * @param keyList 包含键值对的列表，其中每个键值对表示一个生成的键
     */
    public GeneratedKeyHolder(List<Map<String, Object>> keyList) {
        this.keyList = keyList;
    }


    @Override
    public Number getKey() {
        return getKeyAs(Number.class);
    }

    @Override
    public <T> T getKeyAs(Class<T> keyType) {
        if (this.keyList.isEmpty()) {
            return null;
        }
        if (this.keyList.size() > 1 || this.keyList.get(0).size() > 1) {
            throw new JdbcException(
                    "仅当返回单个键时，才应使用 getKey 方法。 " +
                            "当前键条目包含多个键： " + this.keyList);
        }
        Iterator<Object> keyIter = this.keyList.get(0).values().iterator();
        if (keyIter.hasNext()) {
            Object key = keyIter.next();
            if (key == null || !(keyType.isAssignableFrom(key.getClass()))) {
                throw new JdbcException(
                        "不支持生成的密钥类型。 " +
                                "Unable to cast [" + (key != null ? key.getClass().getName() : null) +
                                "] to [" + keyType.getName() + "].");
            }
            return keyType.cast(key);
        }
        else {
            throw new JdbcException("无法检索生成的密钥。 " +
                    "Check that the table has an identity column enabled.");
        }
    }

    @Override
    public Map<String, Object> getKeys()  {
        if (this.keyList.isEmpty()) {
            return null;
        }
        if (this.keyList.size() > 1) {
            throw new JdbcException(
                    "仅当返回单行的键时，才应使用 getKeys 方法。 " +
                            "The current key list contains keys for multiple rows: " + this.keyList);
        }
        return this.keyList.get(0);
    }

    @Override
    public List<Map<String, Object>> getKeyList() {
        return this.keyList;
    }
}
