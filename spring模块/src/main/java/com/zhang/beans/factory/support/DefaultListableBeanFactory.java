package com.zhang.beans.factory.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;
import com.zhang.beans.factory.support.beanDefinition.BeanDefinitionRegistry;

import java.lang.reflect.InvocationTargetException;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认的BeanFactory类型。
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public class DefaultListableBeanFactory extends AbstractAutowireCapableBeanFactory
        implements ConfigurableListableBeanFactory, BeanDefinitionRegistry {

    /**
     * Bean 定义对象的映射，按 Bean 名称键控。
     */
    private final Map<String, BeanDefinition> beanDefinitionMap = new ConcurrentHashMap<>(256);


    @Override
    public void registerBeanDefinition(String beanName, BeanDefinition beanDefinition) {
        this.beanDefinitionMap.put(beanName, beanDefinition);
    }

    @Override
    public void removeBeanDefinition(String beanName) {
        this.beanDefinitionMap.remove(beanName);
    }

    @Override
    public BeanDefinition getBeanDefinition(String beanName) throws BeansException {
        BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        if (beanDefinition == null) {
            throw new BeansException("没有名字为" + beanName + "的Bean定义");
        }
        return beanDefinition;
    }

    @Override
    public boolean containsBeanDefinition(String beanName) {
        return beanDefinitionMap.containsKey(beanName);
    }

    @Override
    public String[] getBeanDefinitionNames() {
        Set<String> beanNames = beanDefinitionMap.keySet();
        return beanNames.toArray(new String[beanNames.size()]);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws BeansException, InvocationTargetException, IllegalAccessException {
        List<String> beanNames = new ArrayList<>();
        for (Map.Entry<String, BeanDefinition> entry : beanDefinitionMap.entrySet()) {
            Class beanClass = entry.getValue().getClass();
            if (requiredType.isAssignableFrom(beanClass)) {
                beanNames.add(entry.getKey());
            }
        }
        if (beanNames.size() == 1) {
            return getBean(beanNames.get(0), requiredType);
        }
        throw new BeansException(
                "没有类型是是" + requiredType.getName() + "的Bean被定义"
        );
    }

    /**
     * 源码中实现两个Map：allBeanNamesByType and singletonBeanNamesByType
     * 这里简化用遍历BeanMap
     * @param type
     * @return
     * @param <T>
     * @throws BeansException
     */
    @Override
    public <T> Map<String, T> getBeansOfType(Class<T> type) {
        Map<String, T> result = new HashMap<>();
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            try {
                if (type.isAssignableFrom(beanDefinition.getBeanClass())) {
                    T bean = type.cast(getBean(beanName));
                    result.put(beanName, bean);
                }
            } catch (BeansException | InvocationTargetException | IllegalAccessException e) {
                throw new RuntimeException("Error getting bean for name: " + beanName, e);
            }
        });
        return result;
    }


    @Override
    public int getBeanDefinitionCount() {
        return this.beanDefinitionMap.size();
    }

    @Override
    public void preInstantiateSingletons() {
        //List<String> beanNames = new ArrayList<>(this.beanDefinitionNames);
        beanDefinitionMap.forEach((beanName, beanDefinition) -> {
            //只有当bean是单例且不为懒加载才会被创建
            if (beanDefinition.isSingleton() && !beanDefinition.isLazyInit()) {
                try {
                    getBean(beanName);
                } catch (BeansException | InvocationTargetException | IllegalAccessException e) {
                    throw new RuntimeException(e);
                }
            }
        });
    }


}
