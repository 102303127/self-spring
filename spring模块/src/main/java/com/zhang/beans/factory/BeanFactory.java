package com.zhang.beans.factory;

import com.zhang.beans.BeansException;

import java.lang.reflect.InvocationTargetException;

/**
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface BeanFactory {

    /**
     * 根据名称获取Bean
     * @param name
     * @return
     */
    Object getBean(String name) throws BeansException, Exception;

    /**
     * 返回指定 Bean 的实例，该实例可以是共享的，也可以是独立的
     * @param name
     * @param requiredType
     * @return
     * @param <T>
     */
    <T> T getBean(String name, Class<T> requiredType) throws BeansException, Exception;

    /**
     * 返回唯一匹配给定对象类型的 Bean 实例（如果有）。
     * 此方法进入 ListableBeanFactory 按类型查找区域，但也可以转换为基于给定类型名称的常规按名称查找。要跨 Bean 集进行更广泛的检索操作，请使用 ListableBeanFactory 和/或 BeanFactoryUtils.
     * @param <T>requiredType – Bean 必须匹配的类型;可以是接口或超类
     * @return Bean实例
     */
    <T> T getBean(Class<T> requiredType) throws BeansException, Exception;

    /**
     * 此 Bean 工厂是否包含具有给定名称的 Bean 定义或外部注册的单例实例
     * @param name
     * @return
     */
    boolean containsBean(String name);

    /**
     * 这个 Bean 是不是共享的单例
     * @param name
     * @return
     */
   //    boolean isSingleton(String name);
}
