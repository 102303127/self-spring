package com.zhang.advice;

import com.zhang.aop.advice.MethodBeforeAdvice;

import java.lang.reflect.Method;

/**
 *
 * @author zhang
 * @date 2024/7/20
 * @Description
 */
public class Before implements MethodBeforeAdvice {

    @Override
    public void before(Method method, Object[] args, Object target) {
        System.out.println("AopTestService.test方法执行前");
    }

}
