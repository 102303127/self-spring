package com.zhang.aop;

import com.zhang.aop.advised.AdvisedSupport;

import java.lang.reflect.Method;
import java.util.List;

/**
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public interface AdvisorChainFactory {

    List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass);
}
