package com.zhang.context;

import java.util.EventListener;

/**
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public interface ApplicationListener<E extends ApplicationEvent> extends EventListener {

	void onApplicationEvent(E event);
}