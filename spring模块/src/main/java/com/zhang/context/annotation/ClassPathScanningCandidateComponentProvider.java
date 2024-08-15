package com.zhang.context.annotation;

import cn.hutool.core.util.ClassUtil;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.stereotype.Component;

import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class ClassPathScanningCandidateComponentProvider {

	/**
	 * 查找指定基础包路径下带有Component注解的类，
	 * 并将它们转换成BeanDefinition集合返回。
	 *
	 * @param basePackage 基础包路径
	 * @return 包含候选组件Bean定义的集合
	 */
	public Set<BeanDefinition> findCandidateComponents(String basePackage) {
		Set<BeanDefinition> candidates = new LinkedHashSet<BeanDefinition>();
		// 扫描有Component注解的类
		Set<Class<?>> classes = ClassUtil.scanPackageByAnnotation(basePackage, Component.class);
		for (Class<?> clazz : classes) {
			BeanDefinition beanDefinition = new RootBeanDefinition(clazz);
			candidates.add(beanDefinition);
		}
		return candidates;
	}
}
