package com.zhang.aop.adapter;

import com.zhang.aop.advice.BeforeAdvice;
import com.zhang.aop.advice.MethodBeforeAdvice;
import org.aopalliance.intercept.MethodInterceptor;
import org.aopalliance.intercept.MethodInvocation;


/**
 * 方法实现前置通知拦截器
 * 实现方法拦截器接口，重写invoke方法
 *
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public class MethodBeforeAdviceInterceptor implements MethodInterceptor, BeforeAdvice {

	private MethodBeforeAdvice advice;

	public MethodBeforeAdviceInterceptor() {
	}

	public MethodBeforeAdviceInterceptor(MethodBeforeAdvice advice) {
		this.advice = advice;
	}

	public void setAdvice(MethodBeforeAdvice advice) {
		this.advice = advice;
	}

	/**
	 * 重写invoke方法，用于在执行被代理方法之前先执行before advice操作
	 *
	 * @param methodInvocation 被代理的方法调用对象
	 * @return
	 */
	@Override
	public Object invoke(MethodInvocation methodInvocation) throws Throwable {
		// 在执行被代理方法之前先执行before advice操作
		this.advice.before(methodInvocation.getMethod(), methodInvocation.getArguments(), methodInvocation.getThis());
		// 继续执行被代理的方法
		return methodInvocation.proceed();
	}
}
