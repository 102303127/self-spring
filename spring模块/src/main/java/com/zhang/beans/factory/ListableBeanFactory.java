package com.zhang.beans.factory;

import com.zhang.beans.BeansException;

import java.util.Map;

/**
 * BeanFactory扩展要由 Bean 工厂实现的接口，
 * 这些工厂可以枚举其所有 Bean 实例，
 * 而不是根据客户端的请求逐个尝试按名称查找 Bean。
 * 预加载所有 Bean 定义的 BeanFactory 实现（例如基于 XML 的工厂）可以实现此接口。
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface ListableBeanFactory extends BeanFactory {


    boolean containsBeanDefinition(String beanName);


    /**
     * 返回此工厂中定义的所有 Bean 的名称。
     * @return
     */
    String[] getBeanDefinitionNames();

    /**
     * 返回与给定对象类型（包括子类）匹配的 Bean 实例
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException;
}
