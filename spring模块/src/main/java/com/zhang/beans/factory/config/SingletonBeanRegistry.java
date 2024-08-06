package com.zhang.beans.factory.config;

import com.zhang.beans.BeansException;

/**
 * 单例Bean的注册创建
 *
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface SingletonBeanRegistry {
    /**
     * 在 Bean 注册表中，在给定的 Bean 名称下，将给定的现有对象注册为单一实例
     * @param beanName
     * @param singletonObject
     */
    void registerSingleton(String beanName, Object singletonObject) throws BeansException;

    /**
     * 返回在给定名称下注册的（原始）单例对象。
     * @param beanName
     * @return
     */
    Object getSingleton(String beanName) throws BeansException;
}
