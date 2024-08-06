package com.zhang.context.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.support.DefaultListableBeanFactory;
import com.zhang.beans.factory.xml.XmlBeanDefinitionReader;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public abstract class AbstractXmlApplicationContext extends AbstractRefreshableApplicationContext{


    // 使用DefaultListableBeanFactory作为Bean定义注册的目标工厂，加载Bean定义
    protected void loadBeanDefinitions(DefaultListableBeanFactory beanFactory) throws BeansException {
        // 创建一个读取XML Bean定义的读取器，并将工厂和资源加载器(this)传入用于注册定义
        XmlBeanDefinitionReader beanDefinitionReader = new XmlBeanDefinitionReader(beanFactory, this);
        // 获取所有配置文件位置的数组
        String[] configLocations = getConfigLocations();
        if (configLocations != null) {
            //调用xmlBeanDefinitionReader实现的的loadBeanDefinitions方法，加载配置文件
            beanDefinitionReader.loadBeanDefinitions(configLocations);
        }
    }

    protected abstract String[] getConfigLocations();
}
