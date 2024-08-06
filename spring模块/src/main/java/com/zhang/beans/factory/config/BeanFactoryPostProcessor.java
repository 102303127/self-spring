package com.zhang.beans.factory.config;


import com.zhang.beans.BeansException;
/**
 * 允许我们在bean实例化之前修改bean的定义信息即BeanDefinition的信息
 *
 * @author zhang
 * @date 2024/7/11
 * @Description
 */
public interface BeanFactoryPostProcessor {

    /**
     * 修改BeanDefinition信息
     *
     * @param beanFactory
     * @throws BeansException
     */
    void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException;
}
