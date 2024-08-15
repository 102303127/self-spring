package com.zhang.beans.factory.xml;

import cn.hutool.core.util.StrUtil;
import com.zhang.beans.BeansException;
import com.zhang.beans.PropertyValue;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.config.BeanReference;
import com.zhang.beans.factory.support.beanDefinition.AbstractBeanDefinitionReader;
import com.zhang.beans.factory.support.beanDefinition.BeanDefinitionRegistry;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.core.io.Resource;
import com.zhang.core.io.ResourceLoader;
import org.dom4j.Document;
import org.dom4j.DocumentException;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

/**
 * 继承AbstractBeanDefinitionReader
 * 读取xml文件，封装成BeanDefinition
 *
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public class XmlBeanDefinitionReader extends AbstractBeanDefinitionReader {

    private Class<? extends BeanDefinitionDocumentReader> documentReaderClass =
            DefaultBeanDefinitionDocumentReader.class;

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry) {
        super(registry);
    }

    public XmlBeanDefinitionReader(BeanDefinitionRegistry registry, ResourceLoader resourceLoader) {
        super(registry, resourceLoader);
    }

    @Override
    public void loadBeanDefinitions(String location) throws BeansException {
        ResourceLoader resourceLoader = getResourceLoader();
        Resource resource = resourceLoader.getResource(location);
        loadBeanDefinitions(resource);
    }

    @Override
    public void loadBeanDefinitions(Resource resource) throws BeansException {
        try {
            try (InputStream inputStream = resource.getInputStream()) {
                doLoadBeanDefinitions(inputStream);
            }
        } catch (Exception ex) {
            throw new BeansException("IOException parsing XML document from " + resource, ex);
        }
    }


    /**
     * 解析xml文件，并封装成BeanDefinition
     *
     * 源码中解析xml是实现了BeanDefinitionDocumentReader接口的默认实现类
     * 可以通过parseBeanDefinitionElement方法解析单个bean标签，
     * 也可以通过parseBeanDefinitions方法解析多个bean标签
     *
     * @param inputStream
     * @throws BeansException
     * @throws DocumentException
     */
    protected void doLoadBeanDefinitions(InputStream inputStream) throws BeansException, DocumentException, NoSuchMethodException, InvocationTargetException, InstantiationException, IllegalAccessException {
        SAXReader reader = new SAXReader();
        Document document = reader.read(inputStream);

        Element root = document.getRootElement();

        BeanDefinitionDocumentReader documentReader = documentReaderClass.getDeclaredConstructor().newInstance();
        // 解析并注册
        documentReader.registerBeanDefinitions(root, getRegistry());
    }



}
