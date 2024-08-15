package com.zhang.event;

import com.zhang.context.event.ApplicationContextEvent;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class MyEvent extends ApplicationContextEvent {

    /**
     * 构造一个原型事件。
     *
     * @param source 事件最初发生的对象。
     * @throws IllegalArgumentException 如果 source 为 null。
     */
    public MyEvent(Object source) {
        super(source);
    }
}
