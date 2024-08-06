package com.zhang.beans.factory.support.beanDefinition;

import com.zhang.beans.BeansException;
import com.zhang.core.io.Resource;
import com.zhang.core.io.ResourceLoader;
/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public interface BeanDefinitionReader {

    BeanDefinitionRegistry getRegistry();

    ResourceLoader getResourceLoader();

    void loadBeanDefinitions(Resource resource) throws BeansException;

    void loadBeanDefinitions(String location) throws BeansException;

    void loadBeanDefinitions(String[] locations) throws BeansException;
}

