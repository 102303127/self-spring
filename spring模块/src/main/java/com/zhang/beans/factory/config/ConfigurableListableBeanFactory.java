package com.zhang.beans.factory.config;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.ListableBeanFactory;

/**
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface ConfigurableListableBeanFactory extends ListableBeanFactory, AutowireCapableBeanFactory, ConfigurableBeanFactory {

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;

    void preInstantiateSingletons();
}
