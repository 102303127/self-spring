package com.zhang;

import com.zhang.beans.BeansException;
import com.zhang.beans.Person;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import com.zhang.event.MyEvent;
import org.junit.Test;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class PackageScanTest {

    @Test
    public void test() throws Exception, BeansException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:package-scan.xml");

        Person person = applicationContext.getBean("person", Person.class);
        System.out.println(person);

    }
}
