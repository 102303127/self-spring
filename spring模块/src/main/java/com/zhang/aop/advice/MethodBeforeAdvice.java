package com.zhang.aop.advice;

import java.lang.reflect.Method;

/**
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface MethodBeforeAdvice extends BeforeAdvice {

	void before(Method method, Object[] args, Object target) throws Throwable;
}
