package com.zhang.context.event;


import com.zhang.context.ApplicationContext;

/**
 * 开始时引发 ApplicationContext 的事件。
 * （spring中默认实现的事件。）
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class ContextStartedEvent extends ApplicationContextEvent {

	public ContextStartedEvent(ApplicationContext source) {
		super(source);
	}

}
