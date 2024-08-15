package com.zhang.beans.factory.config;

import com.zhang.beans.factory.HierarchicalBeanFactory;
import com.zhang.core.convert.ConversionService;
import com.zhang.util.StringValueResolver;

/**
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public interface ConfigurableBeanFactory extends HierarchicalBeanFactory, SingletonBeanRegistry {

    /**
     * 标准单例作用域的作用域标识符：“singleton”。
     * 可以通过 registerScope添加自定义范围。
     */
    String SCOPE_SINGLETON = "singleton";


    /**
     * 标准原型作用域的作用范围标识符：“prototype”。
     * 可以通过 registerScope添加自定义范围。
     */
    String SCOPE_PROTOTYPE = "prototype";


    /**
     * 添加bean加强处理
     * @param beanPostProcessor
     */
    void addBeanPostProcessor(BeanPostProcessor beanPostProcessor);



    /**
     * 添加嵌入值解析器
     * @param valueResolver
     */
    void addEmbeddedValueResolver(StringValueResolver valueResolver);

    /**
     * 解析嵌入值
     * @param value
     * @return
     */
    String resolveEmbeddedValue(String value);



    void setConversionService(ConversionService conversionService);

    ConversionService getConversionService();




}
