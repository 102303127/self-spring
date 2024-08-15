package com.zhang;

import com.zhang.beans.BeansException;
import com.zhang.beans.Person;
import com.zhang.beans.Student;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import org.junit.Test;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class AutowireTest {

    @Test
    public void test() throws Exception, BeansException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:autowire.xml");

        Student student = applicationContext.getBean("student", Student.class);
        System.out.println(student);

    }
}
