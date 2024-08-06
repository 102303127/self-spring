package com.zhang.context;

import java.util.EventObject;

/**
 * @author zhang
 * @date 2024/7/8
 * @Description
 */
public abstract class ApplicationEvent extends EventObject {
    /**
     * 构造一个原型事件。
     *
     * @param source 事件最初发生的对象。
     * @throws IllegalArgumentException 如果 source 为 null。
     */
    public ApplicationEvent(Object source) {
        super(source);
    }
}
