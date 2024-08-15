package com.zhang;

import com.zhang.beans.Animal;
import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.support.DefaultListableBeanFactory;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import org.junit.Test;

import java.lang.reflect.InvocationTargetException;

/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public class SimpleBeanContainerTest {

    @Test
    public void testGetBean() throws BeansException, Exception {
        DefaultListableBeanFactory beanFactory = new DefaultListableBeanFactory();
        BeanDefinition beanDefinition = new RootBeanDefinition(Animal.class);
        beanFactory.registerBeanDefinition("animal", beanDefinition);

        Animal animal = (Animal) beanFactory.getBean("animal");
        System.out.println(animal);

    }
}
