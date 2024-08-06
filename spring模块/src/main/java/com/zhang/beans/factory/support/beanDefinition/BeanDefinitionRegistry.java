package com.zhang.beans.factory.support.beanDefinition;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanDefinition;

/**
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface BeanDefinitionRegistry {

    void registerBeanDefinition(String beanName, BeanDefinition beanDefinition);
    void removeBeanDefinition(String beanName);

    BeanDefinition getBeanDefinition(String beanName) throws BeansException;
    boolean containsBeanDefinition(String beanName);
    int getBeanDefinitionCount();

}
