package com.zhang.context.event;

import com.zhang.context.ApplicationContext;

/**
 * 当一个 ApplicationContext 关闭时引发的事件。
 * （spring中默认实现的事件。）
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class ContextClosedEvent extends ApplicationContextEvent {

	public ContextClosedEvent(ApplicationContext source) {
		super(source);
	}

}
