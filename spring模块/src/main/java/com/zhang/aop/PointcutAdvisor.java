package com.zhang.aop;

/**
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public interface PointcutAdvisor extends Advisor {

	Pointcut getPointcut();
}
