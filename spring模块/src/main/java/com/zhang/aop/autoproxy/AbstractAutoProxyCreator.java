package com.zhang.aop.autoproxy;

import com.zhang.aop.Advisor;
import com.zhang.aop.ClassFilter;
import com.zhang.aop.Pointcut;
import com.zhang.aop.advised.ProxyFactory;
import com.zhang.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.zhang.aop.target.TargetSource;
import com.zhang.beans.BeansException;
import com.zhang.beans.PropertyValues;
import com.zhang.beans.factory.BeanFactory;
import com.zhang.beans.factory.BeanFactoryAware;

import com.zhang.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.zhang.beans.factory.support.DefaultListableBeanFactory;
import org.aopalliance.aop.Advice;

import java.lang.reflect.Proxy;
import java.sql.Connection;
import java.util.*;


/**
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public abstract class AbstractAutoProxyCreator implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {

    protected DefaultListableBeanFactory beanFactory;

    /**
     * 源码中使用map，用cacheKey作为key，这里简化实现用Set
     */
    private Set<Object> earlyProxyReferences = new HashSet<>();


    @Override
           public Object postProcessAfterInitialization(Object bean, String beanName) throws BeansException {
        if (!earlyProxyReferences.contains(beanName)) {
            return wrapIfNecessary(bean, beanName);
        }
        return bean;
    }

    /**
     * 源码中对实例化之前的Bean判断是否需要代理
     * 这里默认不实现
     * @param beanClass
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object postProcessBeforeInstantiation(Class<?> beanClass, String beanName) throws BeansException {
        // 返回null
        return null;
    }


    /**
     * 获取早期Bean引用。
     *
     * @param bean 原始Bean对象
     * @param beanName Bean的名称
     * @return
     */
    @Override
    public Object getEarlyBeanReference(Object bean, String beanName) throws BeansException {
        earlyProxyReferences.add(beanName);
        return wrapIfNecessary(bean, beanName);
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) {
        return null;
    }

    @Override
    public boolean postProcessAfterInstantiation(Object bean, String beanName)  {
        return false;
    }



    /**
     * 根据需要包装给定的bean，如果bean满足AOP切点条件，则返回代理对象，否则返回原bean。
     *
     * @param bean 需要包装的原始bean对象
     * @param beanName bean的名称
     * @return 包装后的bean对象，如果不需要包装则返回原始bean对象
     */
    private Object wrapIfNecessary(Object bean, String beanName) throws BeansException {

        // ....
        // 如果bean是基础设施类（如Advice、Pointcut等），则直接返回原bean，避免死循环
        if (isInfrastructureClass(bean.getClass())) {
            return bean;
        }

        // 获取bean的所有Advisor
        Object[] specificInterceptors  = getAdvicesAndAdvisorsForBean(bean.getClass(), beanName, null);
        if (specificInterceptors != null){
            // 创建代理
            Object proxy = createProxy(
                    bean.getClass(), beanName, specificInterceptors, new TargetSource(bean));
            return proxy;
        }

        return bean;
    }

    /**
     * 创建代理
     *
     * @param beanClass
     * @param beanName
     * @param specificInterceptors
     * @param targetSource
     * @return
     * @throws BeansException
     */
    protected Object createProxy(Class<?> beanClass, String beanName, Object[] specificInterceptors, TargetSource targetSource) throws BeansException {

        // 创建代理工厂
        ProxyFactory proxyFactory = new ProxyFactory();


        Collection<AspectJExpressionPointcutAdvisor> advisors = buildAdvisors(specificInterceptors);

        // 遍历所有AspectJExpressionPointcutAdvisor类型的bean
        for (AspectJExpressionPointcutAdvisor advisor : advisors) {
            // 设置代理工厂的目标源对象
            proxyFactory.setTargetSource(targetSource);
            // 向代理工厂中添加通知器
            proxyFactory.addAdvisor(advisor);
            // 设置代理工厂的方法匹配器
            proxyFactory.setMethodMatcher(advisor.getPointcut().getMethodMatcher());

        }
        // 如果代理工厂中有通知器，则创建代理对象并返回
        if (!proxyFactory.getAdvisors().isEmpty()) {
            return proxyFactory.getProxy();
        }
        return null;
    }

    /**
     * 这一步又把特殊拦截器转换成Advisor，没有设置普通拦截器的拓展
     *
     * @param specificInterceptors
     * @return
     */
    private Collection<AspectJExpressionPointcutAdvisor> buildAdvisors(Object[] specificInterceptors) {
        Collection<AspectJExpressionPointcutAdvisor> advisors = new ArrayList<>();
        for (Object interceptor : specificInterceptors) {
            if (interceptor instanceof AspectJExpressionPointcutAdvisor) {
                advisors.add((AspectJExpressionPointcutAdvisor) interceptor);
            }
        }
        return advisors;
    }


    /**
     * 判断给定的beanClass是否是基础设施类（Advice、Pointcut或Advisor）
     *
     * @param beanClass 要判断的类
     * @return 如果beanClass是Advice、Pointcut或Advisor类的子类或实现了这些接口，则返回true；否则返回false
     */
    private boolean isInfrastructureClass(Class<?> beanClass) {
        // 判断给定的beanClass是否是Advice类的子类或实现了Advice接口
        return Advice.class.isAssignableFrom(beanClass)
                // 判断给定的beanClass是否是Pointcut类的子类或实现了Pointcut接口
                || Pointcut.class.isAssignableFrom(beanClass)
                // 判断给定的beanClass是否是Advisor类的子类或实现了Advisor接口
                || Advisor.class.isAssignableFrom(beanClass);
    }



    public BeanFactory getBeanFactory() {
        return beanFactory;
    }


    /**
     * 由Aware实现对属性的填充
     *
     * @param beanFactory
     */
    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (DefaultListableBeanFactory) beanFactory;
    }

    public Set<Object> getEarlyProxyReferences() {
        return earlyProxyReferences;
    }

    public void setEarlyProxyReferences(Set<Object> earlyProxyReferences) {
        this.earlyProxyReferences = earlyProxyReferences;
    }

    /**
     * 获取所有适用于当前Bean 的 Advisors
     *
     * @param beanClass
     * @param beanName
     * @param customTargetSource
     * @return
     * @throws BeansException
     */
    protected abstract Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName,
                                                              TargetSource customTargetSource) throws BeansException;

}
