package com.zhang.beans.factory.support.beanDefinition;

import com.zhang.beans.PropertyValues;

/**
 * @author zhang
 * @date 2024/6/29
 * @Description
 */
public class RootBeanDefinition extends AbstractBeanDefinition{


    public RootBeanDefinition(Class beanClass) {
        super(beanClass);
    }

    public RootBeanDefinition(Class beanClass, PropertyValues propertyValues) {
        super(beanClass, propertyValues);
    }
}
