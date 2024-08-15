package com.zhang.context;

import com.zhang.beans.BeansException;

/**
 * @author zhang
 * @date 2024/7/8
 * @Description
 */
public interface ApplicationEventPublisher {


    /**
     * 将事件通知在此应用程序中注册的所有 匹配 侦听器
     * 如果指定的 event 不是 ApplicationEvent，则将其包装在 PayloadApplicationEvent中。
     *
     * 这里简化实现,只对ApplicationEvent实现
     * @param event
     */
    void publishEvent(ApplicationEvent event) throws BeansException;
}
