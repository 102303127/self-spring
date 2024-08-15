package com.zhang;

import com.zhang.beans.BeansException;
import com.zhang.context.ApplicationContext;
import com.zhang.context.support.ClassPathXmlApplicationContext;
import com.zhang.event.MyEvent;
import org.junit.Test;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class EventTest {

    @Test
    public void test() throws Exception, BeansException {
        ApplicationContext applicationContext = new ClassPathXmlApplicationContext("classpath:event.xml");

        // 发布事件
        applicationContext.publishEvent(new MyEvent(applicationContext));

    }
}
