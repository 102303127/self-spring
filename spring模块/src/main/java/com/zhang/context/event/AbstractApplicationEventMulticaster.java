package com.zhang.context.event;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.BeanFactory;
import com.zhang.beans.factory.BeanFactoryAware;
import com.zhang.context.ApplicationEvent;
import com.zhang.context.ApplicationListener;

import java.util.HashSet;
import java.util.Set;


/**
 * 提供基本的侦听器注册工具。
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public abstract class AbstractApplicationEventMulticaster implements ApplicationEventMulticaster, BeanFactoryAware {

	public final Set<ApplicationListener<ApplicationEvent>> applicationListeners = new HashSet<>();

	private BeanFactory beanFactory;

	@Override
	public void addApplicationListener(ApplicationListener<?> listener) {
		applicationListeners.add((ApplicationListener<ApplicationEvent>) listener);
	}

	@Override
	public void removeApplicationListener(ApplicationListener<?> listener) {
		applicationListeners.remove(listener);
	}

	@Override
	public void setBeanFactory(BeanFactory beanFactory) {
		this.beanFactory = beanFactory;
	}
}
