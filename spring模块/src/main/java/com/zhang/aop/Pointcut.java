package com.zhang.aop;


import com.zhang.aop.target.MethodMatcher;

/**
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface Pointcut {

	ClassFilter getClassFilter();

	MethodMatcher getMethodMatcher();
}
