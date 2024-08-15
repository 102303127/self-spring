package com.zhang.event;

import com.zhang.context.ApplicationListener;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class MyEventListener implements ApplicationListener<MyEvent> {
    @Override
    public void onApplicationEvent(MyEvent event) {
        System.out.println(event.getSource());
        System.out.println(this.getClass().getName());
    }
}
