package com.zhang.aop;

/**
 * 限制切入点或简介与给定目标类集的匹配的过滤器。
 * 函数式接口，只有一个方法：matches。
 *
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public interface ClassFilter {

	boolean matches(Class<?> clazz);
}
