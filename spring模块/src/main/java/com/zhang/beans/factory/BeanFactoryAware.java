package com.zhang.beans.factory;

import com.zhang.beans.factory.support.DefaultListableBeanFactory;

/**
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface BeanFactoryAware extends Aware{
    /**
     * 注入beanFactory
     * @param beanFactory
     */
    void setBeanFactory(BeanFactory beanFactory);
}
