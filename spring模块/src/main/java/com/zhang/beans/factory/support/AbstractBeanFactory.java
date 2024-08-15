package com.zhang.beans.factory.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.config.BeanPostProcessor;
import com.zhang.beans.factory.config.ConfigurableBeanFactory;
import com.zhang.beans.factory.support.beanDefinition.DefaultSingletonBeanRegistry;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.core.convert.ConversionService;
import com.zhang.util.StringValueResolver;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;

/**
 * ioc容器的抽象实现类
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public abstract class AbstractBeanFactory extends DefaultSingletonBeanRegistry
        implements ConfigurableBeanFactory {


    /**
     * 字符串解析器，例如应用于注释属性值。
     */
    private final List<StringValueResolver> embeddedValueResolvers = new CopyOnWriteArrayList<>();

    /**
     *  BeanPostProcessors to apply.
     */
    private final List<BeanPostProcessor> beanPostProcessors = new ArrayList<>();


    private ConversionService conversionService;

    public AbstractBeanFactory() {
    }

    /**
     * 源码中使用的getBean调用doGetBean，这里简化实现
     * @param name
     * @return
     */
    @Override
    public Object getBean(String name) throws BeansException, Exception {
        Object sharedInstance = getSingleton(name);
        if (sharedInstance != null) {
            //如果是FactoryBean，从FactoryBean#getObject中创建bean
            return getObjectForBeanInstance(sharedInstance, name);
        }
        RootBeanDefinition beanDefinition = (RootBeanDefinition) getBeanDefinition(name);
        Object bean = createBean(name, beanDefinition);
        return bean;
    }


    protected abstract BeanDefinition getBeanDefinition(String name) throws BeansException;

    protected abstract Object createBean(String name, RootBeanDefinition beanDefinition) throws BeansException, Exception;

    protected abstract boolean containsBeanDefinition(String beanName);

    /**
     * 获取给定 Bean 实例的对象，可以是 Bean 实例本身，也可以是 FactoryBean 创建的对象。
     * @pram sharedInstance
     * @param name
     * @return
     */
    // TODO 这里暂时没有实现FactoryBean相关的
    protected Object getObjectForBeanInstance(Object sharedInstance, String name) {
        return sharedInstance;
    }


    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException, Exception {
        return ((T) getBean(name));
    }

    @Override
    public boolean containsBean(String name) {
        return containsBeanDefinition(name);
    }

    /**
     * 添加BeanPostProcessor
     * @param beanPostProcessor
     */
    @Override
    public void addBeanPostProcessor(BeanPostProcessor beanPostProcessor) {
        this.beanPostProcessors.remove(beanPostProcessor);
        this.beanPostProcessors.add(beanPostProcessor);
    }

    public List<BeanPostProcessor> getBeanPostProcessors() {
        return this.beanPostProcessors;
    }

    @Override
    public void addEmbeddedValueResolver(StringValueResolver valueResolver) {
        this.embeddedValueResolvers.add(valueResolver);
    }

    /**
     * 解析字符串，例如：${name}
     *
     * @param value
     * @return
     */
    @Override
    public String resolveEmbeddedValue(String value) {
        String result = value;
        for (StringValueResolver resolver : this.embeddedValueResolvers) {
            result = resolver.resolveStringValue(result);
        }
        return result;
    }

    @Override
    public ConversionService getConversionService() {
        return this.conversionService;
    }

    @Override
    public void setConversionService(ConversionService conversionService) {
        this.conversionService = conversionService;
    }
}
