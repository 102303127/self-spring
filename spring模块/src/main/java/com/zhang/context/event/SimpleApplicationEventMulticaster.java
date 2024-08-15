package com.zhang.context.event;


import com.zhang.beans.BeansException;
import com.zhang.beans.factory.BeanFactory;
import com.zhang.context.ApplicationEvent;
import com.zhang.context.ApplicationListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 将所有事件多播到所有已注册的侦听器
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class SimpleApplicationEventMulticaster extends AbstractApplicationEventMulticaster {

	public SimpleApplicationEventMulticaster(BeanFactory beanFactory) throws BeansException {
		setBeanFactory(beanFactory);
	}

	/**
	 * 向所有的监听器广播事件。
	 *
	 * @param event 要广播的事件
	 * @throws BeansException 如果事件广播过程中发生Bean异常
	 */
	@Override
	public void multicastEvent(ApplicationEvent event) throws BeansException {
		for (ApplicationListener<ApplicationEvent> applicationListener : applicationListeners) {
			if (supportsEvent(applicationListener, event)) {
				applicationListener.onApplicationEvent(event);
			}
		}
	}

	/**
	 * 监听器是否对该事件支持
	 *
	 * @param applicationListener
	 * @param event
	 * @return
	 */
	protected boolean supportsEvent(ApplicationListener<ApplicationEvent> applicationListener, ApplicationEvent event) throws BeansException {
		Type type = applicationListener.getClass().getGenericInterfaces()[0];
		Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
		String className = actualTypeArgument.getTypeName();
		Class<?> eventClassName;
		try {
			eventClassName = Class.forName(className);
		} catch (ClassNotFoundException e) {
			throw new BeansException("wrong event class name: " + className);
		}
		return eventClassName.isAssignableFrom(event.getClass());
	}
}
