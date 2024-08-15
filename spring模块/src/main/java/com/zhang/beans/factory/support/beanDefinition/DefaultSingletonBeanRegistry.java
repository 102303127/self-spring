package com.zhang.beans.factory.support.beanDefinition;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.ObjectFactory;
import com.zhang.beans.factory.config.SingletonBeanRegistry;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 默认单例Bean注册的实现
 * @author zhang
 * @date 2024/6/28
 * DefaultSingletonBeanRegistry
 * @Description
 */
public class DefaultSingletonBeanRegistry implements SingletonBeanRegistry {

    /**
     * singleton 对象的缓存：bean name --> bean instance
     * 一级缓存：存放已经初始化完成的Bean
      */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);

    /**
     * singleton 的工厂对象的缓存：bean name --> ObjectFactory
     * 三级缓存：存放bean工厂
      */
    private final Map<String, ObjectFactory<?>> singletonFactories = new HashMap<>(16);

    /**
     * singletonFactory 制造出来的 singleton 的缓存：bean name --> bean instance
     * 二级缓存： 存放半成品Bean，既实例化完成未初始化的Bean。
     */
    private final Map<String, Object> earlySingletonObjects = new HashMap<>(16);
    //以上三个缓存是这个类存放单例 bean 的主要 Map

    /**
     * 已经注册的单例列表
     */
    private final Set<String> registeredSingletons = new LinkedHashSet<>(256);


    @Override
    public void registerSingleton(String beanName, Object singletonObject) throws BeansException {
        Object oldObject = this.singletonObjects.get(beanName);
        if (oldObject != null) {
            throw new BeansException("无法注册 "+ beanName + "已经注册过");
        }
        addSingleton(beanName, singletonObject);
    }

    protected void addSingleton(String beanName, Object singletonObject) {
        this.singletonObjects.put(beanName, singletonObject);
        //移除第二三级缓存
        this.singletonFactories.remove(beanName);
        this.earlySingletonObjects.remove(beanName);
        this.registeredSingletons.add(beanName);
    }

    /**
     * 返回在给定名称下注册的（原始）单例对象。
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object getSingleton(String beanName) {
        //从一级缓存中取
        Object singletonObject = singletonObjects.get(beanName);
        if (singletonObject == null) {
            //从二级缓存中取
            singletonObject = earlySingletonObjects.get(beanName);
            if (singletonObject == null) {
                ObjectFactory<?> singletonFactory = singletonFactories.get(beanName);
                if (singletonFactory != null) {
                    //实现对所取Bean实例化，并存放在三级缓存中
                    singletonObject = singletonFactory.getObject();
                    //从三级缓存放进二级缓存
                    earlySingletonObjects.put(beanName, singletonObject);
                    singletonFactories.remove(beanName);
                }
            }
        }
        return singletonObject;
    }

    /**
     * 添加到三级缓存里
     * @param beanName
     * @param singletonFactory
     */
    protected void addSingletonFactory(String beanName, ObjectFactory<?> singletonFactory) {
        if (!this.singletonObjects.containsKey(beanName)) {
            this.singletonFactories.put(beanName, singletonFactory);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.add(beanName);
        }
    }

    /**
     * 清除指定Bean
     * @param beanName
     */
    protected void removeSingleton(String beanName) {
        synchronized (this.singletonObjects) {
            this.singletonObjects.remove(beanName);
            this.singletonFactories.remove(beanName);
            this.earlySingletonObjects.remove(beanName);
            this.registeredSingletons.remove(beanName);
        }
    }




}
