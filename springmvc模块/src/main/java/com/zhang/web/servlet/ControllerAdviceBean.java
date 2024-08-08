package com.zhang.web.servlet;


import org.springframework.beans.factory.BeanFactory;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.util.Assert;
import org.springframework.web.bind.annotation.ControllerAdvice;

import java.util.ArrayList;
import java.util.List;


/**
 * 注解ControllerAdvice的映射Bean
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class ControllerAdviceBean implements Ordered {
    private final Object beanOrName;

    private final boolean isSingleton;

    private Object resolvedBean;

    private final BeanFactory beanFactory;

    private Integer order;


    public static List<ControllerAdviceBean> findAnnotatedBeans(ApplicationContext applicationContext) {
        List<ControllerAdviceBean> beans = new ArrayList();
        String[] var2 = BeanFactoryUtils.beanNamesForTypeIncludingAncestors(applicationContext, Object.class);

        for (String name : var2) {
            if (applicationContext.findAnnotationOnBean(name, ControllerAdvice.class) != null) {
                beans.add(new ControllerAdviceBean(name, applicationContext));
            }
        }

        return beans;
    }
    public ControllerAdviceBean(Object bean) {
        Assert.notNull(bean, "Bean must not be null");
        this.beanOrName = bean;
        this.isSingleton = true;
        this.resolvedBean = bean;
        this.beanFactory = null;
    }
    public ControllerAdviceBean(String beanName, BeanFactory beanFactory) {
        this(beanName, beanFactory, null);
    }

    public ControllerAdviceBean(String beanName, BeanFactory beanFactory, ControllerAdvice controllerAdvice) {
        this.beanOrName = beanName;
        this.isSingleton = beanFactory.isSingleton(beanName);
        this.beanFactory = beanFactory;
    }


    private BeanFactory obtainBeanFactory() {
        return this.beanFactory;
    }


    @Override
    public int getOrder() {
        return 0;
    }
}