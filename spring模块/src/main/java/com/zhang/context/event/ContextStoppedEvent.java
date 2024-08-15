
package com.zhang.context.event;


import com.zhang.context.ApplicationContext;

/**
 * 停止时引发 ApplicationContext 的事件。
 * （spring中默认实现的事件。）
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class ContextStoppedEvent extends ApplicationContextEvent {

	public ContextStoppedEvent(ApplicationContext source) {
		super(source);
	}

}
