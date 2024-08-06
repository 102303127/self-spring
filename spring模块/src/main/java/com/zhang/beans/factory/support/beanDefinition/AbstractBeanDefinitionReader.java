package com.zhang.beans.factory.support.beanDefinition;

import com.zhang.beans.BeansException;
import com.zhang.core.io.DefaultResourceLoader;
import com.zhang.core.io.ResourceLoader;

/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public abstract class AbstractBeanDefinitionReader implements BeanDefinitionReader{

    protected final BeanDefinitionRegistry registry;

    protected ResourceLoader resourceLoader;

    public AbstractBeanDefinitionReader(BeanDefinitionRegistry registry) {
        this.registry = registry;
        this.resourceLoader = new DefaultResourceLoader();
    }

    protected AbstractBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        this.registry = registry;
        this.resourceLoader = resourceLoader;
    }

    @Override
    public void loadBeanDefinitions(String[] locations) throws BeansException {
        for (String location : locations) {
            loadBeanDefinitions(location);
        }
    }

    @Override
    public BeanDefinitionRegistry getRegistry() {
        return registry;
    }

    @Override
    public ResourceLoader getResourceLoader() {
        return resourceLoader;
    }

    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = resourceLoader;
    }
}
