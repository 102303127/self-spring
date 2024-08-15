package com.zhang.context.event;

import com.zhang.beans.BeansException;
import com.zhang.context.ApplicationEvent;
import com.zhang.context.ApplicationListener;

/**
 * @author zhang
 * @date 2024/7/8
 * @Description
 */
public interface ApplicationEventMulticaster {

    void addApplicationListener(ApplicationListener<?> listener);

    void removeApplicationListener(ApplicationListener<?> listener);

    void multicastEvent(ApplicationEvent event) throws BeansException;
}
