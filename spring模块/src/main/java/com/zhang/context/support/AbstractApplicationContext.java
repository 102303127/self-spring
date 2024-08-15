package com.zhang.context.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanFactoryPostProcessor;
import com.zhang.beans.factory.config.BeanPostProcessor;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;
import com.zhang.context.ApplicationListener;
import com.zhang.context.ConfigurableApplicationContext;
import com.zhang.context.event.ApplicationEventMulticaster;
import com.zhang.context.event.ContextClosedEvent;
import com.zhang.context.event.ContextRefreshedEvent;
import com.zhang.context.event.SimpleApplicationEventMulticaster;
import com.zhang.core.convert.ConversionService;
import com.zhang.core.io.DefaultResourceLoader;
import com.zhang.context.ApplicationEvent;

import java.lang.reflect.InvocationTargetException;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Set;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public abstract class AbstractApplicationContext extends DefaultResourceLoader
        implements ConfigurableApplicationContext {

    private ApplicationEventMulticaster applicationEventMulticaster;

    /**
     * 工厂中 ApplicationEventMulticaster bean 的名称。
     */
    public static final String APPLICATION_EVENT_MULTICASTER_BEAN_NAME = "applicationEventMulticaster";

    /**
     * 如果未提供任何内容，则应用默认转换规则。
     */
    public static final String CONVERSION_SERVICE_BEAN_NAME = "conversionService";


    /**
     * 静态指定的侦听器。
     */
    private final Set<ApplicationListener<?>> applicationListeners = new LinkedHashSet<>();


    /**
     * 添加应用程序监听器。
     * 由 EventListenerMethodProcessor调用
     * 解析 @EventListener 注释（这里没实现）
     *
     * @param listener 要添加的应用程序监听器
     */
    @Override
    public void addApplicationListener(ApplicationListener<?> listener) {
        if (this.applicationEventMulticaster != null) {
            this.applicationEventMulticaster.addApplicationListener(listener);
        }
        this.applicationListeners.add(listener);
    }


    @Override
    public void refresh() throws BeansException, Exception {

        // 1. 准备刷新过程，设置开始时间，状态标志等
        // prepareRefresh();

        // 2.在AbstractRefreshableApplicationContext中
        // 创建BeanFactory的具体实现DefaultListableBeanFactory
        // 并加载BeanDefinition
        ConfigurableListableBeanFactory beanFactory = obtainFreshBeanFactory();

        // 3.准备 Bean Factory 以在此上下文中使用。
        // 配置BeanFactory，注册忽略的依赖接口等
        prepareBeanFactory(beanFactory);

        // 4.允许在上下文子类中对 Bean 工厂进行后处理。
        postProcessBeanFactory(beanFactory);

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
        initApplicationEventMulticaster();

        // 10.在特定上下文子类中初始化其他特殊 bean。
        // 留给子类覆盖的定制方法
        // onRefresh();

        // 11.注册事件监听器
        registerListeners();

        // 12.注册类型转换器和提前实例化单例bean
        finishBeanFactoryInitialization(beanFactory);

        // 13.发布容器刷新完成事件
        finishRefresh();


    }


    /**
     * 配置工厂的标准上下文特征
     *
     * @param beanFactory
     */
    private void prepareBeanFactory(ConfigurableListableBeanFactory beanFactory) {

        //添加ApplicationContextAwareProcessor，让继承自ApplicationContextAware的bean能感知bean
        beanFactory.addBeanPostProcessor(new ApplicationContextAwareProcessor(this));

        //设置忽略自动装配的接口，这些接口的实现是容器是需要 beanPostProcessor 注入的
        //在后面进行统一处理
        //所以在使用@Autowired进行注入的时候需要对这些接口进行忽略
        //beanFactory.ignoreDependencyInterface(EnvironmentAware.class);
        //beanFactory.ignoreDependencyInterface(EmbeddedValueResolverAware.class);
        //beanFactory.ignoreDependencyInterface(ResourceLoaderAware.class);
        //beanFactory.ignoreDependencyInterface(ApplicationEventPublisherAware.class);
        //beanFactory.ignoreDependencyInterface(MessageSourceAware.class);
        //beanFactory.ignoreDependencyInterface(ApplicationContextAware.class);
        //beanFactory.ignoreDependencyInterface(ApplicationStartupAware.class);
        // ...
    }


    /**
     * 模板方法，留给子类覆盖
     * Bean已加载，但尚未实例化时
     * 这允许在某些 ApplicationContext 实现中注册特殊的 BeanPostProcessors 等
     * @param beanFactory
     */
    protected void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) {
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
     * 初始化事件发布者
     */
    protected void initApplicationEventMulticaster() throws BeansException {
        ConfigurableListableBeanFactory beanFactory = getBeanFactory();
        applicationEventMulticaster = new SimpleApplicationEventMulticaster(beanFactory);
        beanFactory.registerSingleton(APPLICATION_EVENT_MULTICASTER_BEAN_NAME, applicationEventMulticaster);
    }


    /**
     * 注册事件监听器
     */
    protected void registerListeners() throws BeansException {
        // 首先注册静态事件监听器
        // 主要有 EventListenerMethodProcessor 来解析 @EventListener 注解（这里没实现）
        for (ApplicationListener<?> listener : applicationListeners) {
            getApplicationEventMulticaster().addApplicationListener(listener);
        }

        Collection<ApplicationListener> applicationListeners = getBeansOfType(ApplicationListener.class).values();
        for (ApplicationListener applicationListener : applicationListeners) {
            applicationEventMulticaster.addApplicationListener(applicationListener);
        }

        // 发布早期事件
        // ...
    }

    protected void finishBeanFactoryInitialization(ConfigurableListableBeanFactory beanFactory) throws Exception, BeansException {
        //设置类型转换器
        if (beanFactory.containsBean(CONVERSION_SERVICE_BEAN_NAME)) {
            Object conversionService = beanFactory.getBean(CONVERSION_SERVICE_BEAN_NAME);
            if (conversionService instanceof ConversionService) {
                beanFactory.setConversionService((ConversionService) conversionService);
            }
        }

        //提前实例化单例bean
        beanFactory.preInstantiateSingletons();
    }


    protected void finishRefresh() throws BeansException {
        // 清除上下文级资源缓存（例如来自扫描的 ASM 元数据）。
        // clearResourceCaches();

        // 为此上下文初始化生命周期处理器。
        // initLifecycleProcessor();

        // 首先将刷新传播到生命周期处理器。
        // getLifecycleProcessor().onRefresh();

        // Publish the final event.
        publishEvent(new ContextRefreshedEvent(this));

    }

    /**
     * 发布事件
     *
     * @param event 要发布的事件
     */
    @Override
    public void publishEvent(ApplicationEvent event) throws BeansException {
        applicationEventMulticaster.multicastEvent(event);
    }

    /**
     * 获取应用事件广播器
     *
     * @return 应用事件广播器
     * @throws IllegalStateException 如果应用事件广播器未初始化，则抛出异常
     */
    ApplicationEventMulticaster getApplicationEventMulticaster() throws IllegalStateException {
        if (this.applicationEventMulticaster == null) {
            throw new IllegalStateException("ApplicationEventMulticaster 未初始化 - " +
                    "在通过上下文多播事件之前调用“刷新”： " + this);
        }
        return this.applicationEventMulticaster;
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

    public <T> T getBean(Class<T> requiredType) throws BeansException, Exception {
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

    public void registerShutdownHook() {
        Thread shutdownHook = new Thread() {
            public void run() {
                try {
                    doClose();
                } catch (BeansException e) {
                    throw new RuntimeException(e);
                }
            }
        };
        Runtime.getRuntime().addShutdownHook(shutdownHook);

    }


    protected void doClose() throws BeansException {
        //发布容器关闭事件
        publishEvent(new ContextClosedEvent(this));

        // TODO 执行单例bean的销毁方法
        // destroyBeans();
    }



}
