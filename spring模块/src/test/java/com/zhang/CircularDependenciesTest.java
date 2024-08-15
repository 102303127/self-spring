package com.zhang;

import com.zhang.beans.*;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * 循环依赖的测试
 *
 * @author zhang
 * @date 2024/8/6
 * @Description
 */
public class CircularDependenciesTest {


    @Test
    public void test() throws BeansException, Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:circularDependencies.xml");
        A a = applicationContext.getBean("a", A.class);
        System.out.println(a);

        // 循环依赖
        B b = applicationContext.getBean("b", B.class);
        System.out.println(b);
        System.out.println(a.getB());
    }
}
