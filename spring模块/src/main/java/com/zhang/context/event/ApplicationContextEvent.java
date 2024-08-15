package com.zhang.context.event;

import com.zhang.context.ApplicationContext;
import com.zhang.context.ApplicationEvent;

/**
 * 为 ApplicationContext引发的事件的基类。
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public abstract class ApplicationContextEvent extends ApplicationEvent {

    /**
     * 构造一个原型事件。
     *
     * @param source 事件最初发生的对象。
     * @throws IllegalArgumentException 如果 source 为 null。
     */
    public ApplicationContextEvent(Object source) {
        super(source);
    }


    public final ApplicationContext getApplicationContext() {
        return (ApplicationContext) getSource();
    }


}
