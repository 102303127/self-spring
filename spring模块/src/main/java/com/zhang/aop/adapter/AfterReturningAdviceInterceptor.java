package com.zhang.aop.adapter;

import com.zhang.aop.advice.AfterAdvice;
import com.zhang.aop.advice.AfterReturningAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;

/**
 * 方法实现后置通知拦截器
 * 实现方法拦截器接口，重写invoke方法
 *
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public class AfterReturningAdviceInterceptor implements MethodInterceptor, AfterAdvice {

	private AfterReturningAdvice advice;

	public AfterReturningAdviceInterceptor() {
	}

	public AfterReturningAdviceInterceptor(AfterReturningAdvice advice) {
		this.advice = advice;
	}


	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		Object retVal = methodInvocation.proceed();
		this.advice.afterReturning(retVal, methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
		return retVal;
	}
}
