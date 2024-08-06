package com.zhang.aop.target;

import java.lang.reflect.Method;

/**
 * 判断方法是否符合定义的切入点（pointcut）的条件，
 * 从而决定该方法是否应用某个方面（aspect）的增强（advice）
 *
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface MethodMatcher {

	/**
	 * 判断指定方法和目标类是否与给定的切点匹配。
	 *
	 * @param method 需要判断匹配性的方法
	 * @param targetClass 目标类，如果为null，则使用方法的声明类
	 * @return 如果切点与目标方法和目标类匹配，则返回true；否则返回false
	 */
	boolean matches(Method method, Class<?> targetClass);
}
