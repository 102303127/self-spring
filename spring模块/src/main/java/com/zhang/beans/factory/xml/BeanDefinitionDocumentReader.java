package com.zhang.beans.factory.xml;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.support.beanDefinition.BeanDefinitionRegistry;
import org.dom4j.Element;

/**
 * 解析xml文件
 *
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public interface BeanDefinitionDocumentReader {

    void registerBeanDefinitions(Element root, BeanDefinitionRegistry registry)
            throws BeansException;

}
