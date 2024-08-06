package com.zhang.beans.factory;

/**
 * @author zhang
 * @date 2024/6/29
 * @Description
 */
public interface ObjectFactory<T> {

    // 返回一个对象
    T getObject();
}
