package com.zhang.beans.factory;

import com.zhang.beans.BeansException;
import com.zhang.beans.PropertyValue;
import com.zhang.beans.PropertyValues;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.config.BeanFactoryPostProcessor;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.core.io.DefaultResourceLoader;
import com.zhang.core.io.Resource;
import com.zhang.util.StringValueResolver;

import java.io.IOException;
import java.util.Properties;


/*
  该类实现方法已弃用
  使用 org.springframework.context.support.PropertySourcesPlaceholderConfigurer ，
  通过利用 org.springframework.core.env.Environment
  AND org.springframework.core.env.PropertySource
  机制来更灵活。
 */

/**
 * 属性文件占位符替换
 *
 * @author zhang
 * @date 2024/7/11
 * @Description 源码中继承自PropertiesLoaderSupport
 * 这里把抽象类的加载配置方法全部简化实现在此类中
 */
public class PropertyResourceConfigurer implements BeanFactoryPostProcessor {

    /**默认占位符前缀: {@value}. */
    public static final String DEFAULT_PLACEHOLDER_PREFIX = "${";

    /** 默认占位符后缀: {@value}. */
    public static final String DEFAULT_PLACEHOLDER_SUFFIX = "}";

    /** 默认值分隔符: {@value}. */
    public static final String DEFAULT_VALUE_SEPARATOR = ":";

    private String location;
    @Override
    public void postProcessBeanFactory(ConfigurableListableBeanFactory beanFactory) throws BeansException {
        //加载属性配置文件
        Properties properties = loadProperties();

        //属性值替换占位符
        processProperties(beanFactory, properties);
    }


    /**
     * 加载属性配置文件
     * 源码中是在抽象类PropertiesLoaderSupport中的mergeProperties调用方法loadProperties
     * 这里简化实现,省去判断
     * @return
     */
    private Properties loadProperties() throws BeansException {
        try {
            DefaultResourceLoader resourceLoader = new DefaultResourceLoader();
            Resource resource = resourceLoader.getResource(location);
            Properties properties = new Properties();
            properties.load(resource.getInputStream());
            return properties;
        } catch (IOException e) {
            throw new BeansException("Could not load properties", e);
        }
    }

    /**
     * 源码中是实现类
     * @param beanFactory
     * @param properties
     */
    private void processProperties(ConfigurableListableBeanFactory beanFactory, Properties properties) throws BeansException {

        //往容器中添加字符解析器，供解析@Value注解使用
        StringValueResolver valueResolver = new PlaceholderResolvingStringValueResolver(properties);
        doProcessProperties(beanFactory, valueResolver, properties);
    }

    private void doProcessProperties(ConfigurableListableBeanFactory beanFactory, StringValueResolver valueResolver, Properties properties) throws BeansException {

        String[] beanDefinitionNames = beanFactory.getBeanDefinitionNames();
        for (String beanName : beanDefinitionNames) {
            BeanDefinition beanDefinition = beanFactory.getBeanDefinition(beanName);
            resolvePropertyValues(beanDefinition, properties);
        }

        //往容器中添加字符解析器，供解析@Value注解使用
        beanFactory.addEmbeddedValueResolver(valueResolver);

    }

    /**
     * 解析属性值
     *
     * @param beanDefinition
     * @param properties
     */
    private void resolvePropertyValues(BeanDefinition beanDefinition, Properties properties) {
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        for (PropertyValue propertyValue : propertyValues.getPropertyValues()) {
            Object value = propertyValue.getValue();
            if (value instanceof String) {
                value = resolvePlaceholder((String) value, properties);
                propertyValues.addPropertyValue(new PropertyValue(propertyValue.getName(), value));
            }
        }
    }


    /**
     * 解析占位符
     * @param strVal
     * @param properties
     * @return
     */
    private String resolvePlaceholder(String strVal, Properties properties) {
        StringBuffer stringBuffer = new StringBuffer(strVal);
        return stringBuffer.toString();
    }





    private class PlaceholderResolvingStringValueResolver implements StringValueResolver {

        private final Properties properties;

        public PlaceholderResolvingStringValueResolver(Properties properties) {
            this.properties = properties;
        }

        public String resolveStringValue(String strVal) {
            return PropertyResourceConfigurer.this.resolvePlaceholder(strVal, properties);
        }
    }


}
