package com.zhang.context.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.support.DefaultListableBeanFactory;

import java.io.IOException;

/**
 * 赋予上下文刷新容器的能力
 *
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public abstract class AbstractRefreshableApplicationContext extends AbstractApplicationContext{

    /**
     * Bean factory for this context.
     */
    private DefaultListableBeanFactory beanFactory;

    /**
     * 创建beanFactory并加载BeanDefinition
     *
     * @throws BeansException
     */
    protected final void refreshBeanFactory() throws BeansException {
        DefaultListableBeanFactory beanFactory = createBeanFactory();
        loadBeanDefinitions(beanFactory);
        this.beanFactory = beanFactory;
    }


    protected abstract void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException;

    protected DefaultListableBeanFactory createBeanFactory() {
        return new DefaultListableBeanFactory();
    }

    public DefaultListableBeanFactory getBeanFactory() {
        return beanFactory;
    }
}
