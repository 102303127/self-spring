package com.zhang.beans.factory.config;

import com.zhang.beans.PropertyValues;

/**
 * BeanDefinition 描述一个 Bean 实例，
 * 该实例具有属性值、构造函数参数值以及具体实现提供的其他信息
 * @author zhang
 * @date 2024/6/29
 * @Description
 */
public interface BeanDefinition {
    /**
     *标准单例作用域的作用域标识符：“singleton”。
     * 请注意，扩展的 Bean 工厂可能支持进一步的范围。
     */
    String SCOPE_SINGLETON = ConfigurableBeanFactory.SCOPE_SINGLETON;

    /**
     * 标准单例作用域的作用域标识符：“prototype”。
     * 请注意，扩展的 Bean 工厂可能支持进一步的范围。
     */
    String SCOPE_PROTOTYPE = ConfigurableBeanFactory.SCOPE_PROTOTYPE;

    /**
     * 返回此 Bean 定义的当前 Bean 类名。
     * @return
     */
    String getBeanClassName();

    void setScope(String scope);

    String getScope();

    void setLazyInit(boolean lazyInit);
    boolean isLazyInit();

    void setInitMethodName(String initMethodName);
    String getInitMethodName();

    void setDestroyMethodName(String destroyMethodName);
    String getDestroyMethodName();

    boolean isSingleton();

    boolean isPrototype();

    void setPropertyValues(PropertyValues propertyValues);
    PropertyValues getPropertyValues();

    Class getBeanClass();
}
