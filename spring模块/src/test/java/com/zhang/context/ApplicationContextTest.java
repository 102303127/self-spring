package com.zhang.context;

import com.zhang.beans.Animal;
import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.support.DefaultListableBeanFactory;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhang
 * @date 2024/7/10
 * @Description
 */
public class ApplicationContextTest {

    @Test
    public void test() throws BeansException, Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:spring.xml");
        Animal animal = applicationContext.getBean("animal", Animal.class);

        System.out.println(animal);

    // 循环依赖
    }
}
