package com.zhang.beans.factory.support.beanDefinitionInStantiationStrategy;

import java.lang.reflect.Constructor;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;

/**
 * @author derekyi
 * @date 2020/11/23
 */
public class SimpleInstantiationStrategy implements InstantiationStrategy {


	@Override
	public Object instantiate(RootBeanDefinition beanDefinition) throws BeansException {
		// 获取Bean的类对象
		Class beanClass = beanDefinition.getBeanClass();
		try {
			// 获取无参构造函数
			Constructor constructor = beanClass.getDeclaredConstructor();
			// 使用无参构造函数实例化对象并返回
			return constructor.newInstance();
		} catch (Exception e) {
			// 实例化失败时抛出BeansException异常，并携带相关错误信息
			throw new BeansException("Failed to instantiate [" + beanClass.getName() + "]", e);
		}
	}
}
