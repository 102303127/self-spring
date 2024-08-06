package com.zhang.beans.factory.support.beanDefinitionInStantiationStrategy;

import com.zhang.beans.BeansException;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;

/**
 * Bean的实例化策略
 *
 * @author d31445
 * @date 2024/7/10
 */
public interface InstantiationStrategy {

	Object instantiate(RootBeanDefinition beanDefinition) throws BeansException;
}
