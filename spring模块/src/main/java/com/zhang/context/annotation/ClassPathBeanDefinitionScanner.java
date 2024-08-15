package com.zhang.context.annotation;

import cn.hutool.core.util.StrUtil;
import com.zhang.beans.factory.annotation.AutowiredAnnotationBeanPostProcessor;
import com.zhang.beans.factory.config.BeanDefinition;
import com.zhang.beans.factory.support.beanDefinition.BeanDefinitionRegistry;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.stereotype.Component;

import java.util.Set;

/**
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
public class ClassPathBeanDefinitionScanner extends ClassPathScanningCandidateComponentProvider {

	/**
	 * 内部管理的 Autowired 注释处理器的 Bean 名称。
	 */
	public static final String AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME = "org.springframework.context.annotation.internalAutowiredAnnotationProcessor";

	private BeanDefinitionRegistry registry;

	public ClassPathBeanDefinitionScanner(BeanDefinitionRegistry registry) {
		this.registry = registry;
	}

	public void doScan(String... basePackages) {
		for (String basePackage : basePackages) {
			Set<BeanDefinition> candidates = findCandidateComponents(basePackage);
			for (BeanDefinition candidate : candidates) {
				// 解析bean的作用域
				String beanScope = resolveBeanScope(candidate);
				if (StrUtil.isNotEmpty(beanScope)) {
					candidate.setScope(beanScope);
				}
				//生成bean的名称
				String beanName = determineBeanName(candidate);
				//注册BeanDefinition
				registry.registerBeanDefinition(beanName, candidate);
			}
		}

		// 注册处理@Autowired和@Value注解的BeanPostProcessor
		registry.registerBeanDefinition(AUTOWIRED_ANNOTATION_PROCESSOR_BEAN_NAME,
				new RootBeanDefinition(AutowiredAnnotationBeanPostProcessor.class));
	}

	/**
	 * 获取bean的作用域
	 *
	 * @param beanDefinition
	 * @return
	 */
	private String resolveBeanScope(BeanDefinition beanDefinition) {
		Class<?> beanClass = beanDefinition.getBeanClass();
		Scope scope = beanClass.getAnnotation(Scope.class);
		if (scope != null) {
			return scope.value();
		}

		return StrUtil.EMPTY;
	}


	/**
	 * 生成bean的名称
	 *
	 * @param beanDefinition
	 * @return
	 */
	private String determineBeanName(BeanDefinition beanDefinition) {
		Class<?> beanClass = beanDefinition.getBeanClass();
		Component component = beanClass.getAnnotation(Component.class);
		String value = component.value();
		if (StrUtil.isEmpty(value)) {
			value = StrUtil.lowerFirst(beanClass.getSimpleName());
		}
		return value;
	}
}
