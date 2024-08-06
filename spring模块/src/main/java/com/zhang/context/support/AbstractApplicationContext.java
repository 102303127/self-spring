package com.zhang.context.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanFactoryPostProcessor;
import com.zhang.beans.factory.config.BeanPostProcessor;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;
import com.zhang.context.ConfigurableApplicationContext;
import com.zhang.context.event.ApplicationEventMulticaster;
import com.zhang.core.io.DefaultResourceLoader;
import com.zhang.context.ApplicationEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    private ApplicationEventMulticaster applicationEventMulticaster;


    /**
     * 如果未提供任何内容，则应用默认转换规则。
     */
    String CONVERSION_SERVICE_BEAN_NAME = "conversionService";


    @Override
    public void refresh() throws BeansException {

        // 1. 准备刷新过程，设置开始时间，状态标志等
        // prepareRefresh();

        // 2.在AbstractRefreshableApplicationContext中
        // 创建BeanFactory的具体实现DefaultListableBeanFactory
        // 并加载BeanDefinition
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 3.准备 Bean Factory 以在此上下文中使用。
        // 配置BeanFactory，注册忽略的依赖接口等
        // prepareBeanFactory(beanFactory);

        //添加ApplicationContextAwareProcessor，让继承自ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));
        // 4.允许在上下文子类中对 Bean 工厂进行后处理。
        // postProcessBeanFactory(beanFactory);

        // 5.在bean实例化之前，调用BeanFactoryPostProcessors
        // 对BeanDefinition加强处理，填充属性
        invokeBeanFactoryPostProcessors(beanFactory);

        // 6.BeanPostProcessor需要提前与其他bean实例化之前注册
        registerBeanPostProcessors(beanFactory);

        // 7.Bean后置处理步骤结束
        //beanPostProcess.end();

        // 8.初始化MessageSource组件，用于国际化等功能
        // initMessageSource();

        // 9.初始化事件发布者
        // TODO initApplicationEventMulticaster();

        // 10.在特定上下文子类中初始化其他特殊 bean。
        // 留给子类覆盖的定制方法
        // onRefresh();

        // 11.注册事件监听器
        // TODO registerListeners();

        // 12.注册类型转换器和提前实例化单例bean
        finishBeanFactoryInitialization(beanFactory);

        // 13.发布容器刷新完成事件
        // TODO finishRefresh();


    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) {

        // 提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }


    /**
     * 在bean实例化之前，执行BeanFactoryPostProcessor
     *
     * @param beanFactory
     */
    protected void invokeBeanFactoryPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanFactoryPostProcessor> beanFactoryPostProcessorMap = beanFactory.getBeansOfType(BeanFactoryPostProcessor.class);
        for (BeanFactoryPostProcessor beanFactoryPostProcessor : beanFactoryPostProcessorMap.values()) {
            beanFactoryPostProcessor.postProcessBeanFactory(beanFactory);
        }
    }


    /**
     * 注册BeanPostProcessor
     *
     * @param beanFactory
     */
    protected void registerBeanPostProcessors(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        Map<String, BeanPostProcessor> beanPostProcessorMap = beanFactory.getBeansOfType(BeanPostProcessor.class);
        for (BeanPostProcessor beanPostProcessor : beanPostProcessorMap.values()) {
            beanFactory.addBeanPostProcessor(beanPostProcessor);
        }
    }

    /**
     * 创建并刷新容器
     *
     * @return
     * @throws BeansException
     */
    protected ConfigurableListableBeanFactory obtainFreshBeanFactory() throws BeansException {
        refreshBeanFactory();
        return getBeanFactory();
    }

    @Override
    public void publishEvent(ApplicationEvent event) {
        applicationEventMulticaster.multicastEvent(event);
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return getBeanFactory().containsBean(beanName);
    }

    @Override
    public boolean containsBean(String name) {
        return getBeanFactory().containsBean(name);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws BeansException, Exception {
        return getBeanFactory().getBean(name, requiredType);
    }

    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) throws BeansException {
        return getBeanFactory().getBeansOfType(type);
    }

    public <T> T getBean(Class<T> requiredType) throws BeansException, InvocationTargetException, IllegalAccessException {
        return getBeanFactory().getBean(requiredType);
    }

    @Override
    public Object getBean(String name) throws BeansException, Exception {
        return getBeanFactory().getBean(name);
    }

    public String[] getBeanDefinitionNames() {
        return getBeanFactory().getBeanDefinitionNames();
    }




    protected abstract void refreshBeanFactory() throws BeansException, IllegalStateException;

    public abstract ConfigurableListableBeanFactory getBeanFactory();


    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
    }


}
