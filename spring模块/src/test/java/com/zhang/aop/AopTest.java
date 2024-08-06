package com.zhang.aop;

import com.zhang.beans.BeansException;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhang
 * @date 2024/7/20
 * @Description
 */

public class AopTest {


    @Test
    public void test() throws BeansException, Exception {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:aop.xml");
        AopTestService aopTestService = applicationContext.getBean("aopTestService", AopTestService.class);

        aopTestService.test();

    }

}
