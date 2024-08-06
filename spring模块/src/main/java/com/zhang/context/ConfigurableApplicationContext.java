package com.zhang.context;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public interface ConfigurableApplicationContext extends ApplicationContext{

    /**
     * 刷新容器
     *
     * @throws BeansException
     */
    void refresh() throws BeansException;


    ConfigurableListableBeanFactory getBeanFactory();
}
