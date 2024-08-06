package com.zhang.context.event;

import com.zhang.context.ApplicationEvent;

/**
 * @author zhang
 * @date 2024/7/8
 * @Description
 */
public interface ApplicationEventMulticaster {

    void multicastEvent(ApplicationEvent event);
}
