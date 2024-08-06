package com.zhang.aop.advice;

import java.lang.reflect.Method;

/**
 * 后置增强
 *
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface AfterReturningAdvice extends AfterAdvice {

    void afterReturning( Object returnValue, Method method, Object[] args,  Object target) throws Throwable;
}
