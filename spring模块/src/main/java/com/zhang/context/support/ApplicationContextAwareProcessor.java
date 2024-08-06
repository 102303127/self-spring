package com.zhang.context.support;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanPostProcessor;
import com.zhang.context.ApplicationContextAware;
import com.zhang.context.ConfigurableApplicationContext;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
class ApplicationContextAwareProcessor implements BeanPostProcessor {

	private final ConfigurableApplicationContext applicationContext;

	/**
	 * Create a new ApplicationContextAwareProcessor for the given context.
	 */
	public ApplicationContextAwareProcessor(ConfigurableApplicationContext applicationContext) {
		this.applicationContext = applicationContext;
	}

	@Override
	public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {

		invokeAwareInterfaces(bean);

		return bean;
	}

	private void invokeAwareInterfaces(Object bean) {
		if (bean instanceof ApplicationContextAware) {
			((ApplicationContextAware) bean).setApplicationContext(this.applicationContext);
		}
	}

}
