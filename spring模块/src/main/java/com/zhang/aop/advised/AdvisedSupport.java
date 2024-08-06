package com.zhang.aop.advised;


import com.zhang.aop.Advisor;
import com.zhang.aop.AdvisorChainFactory;
import com.zhang.aop.DefaultAdvisorChainFactory;
import com.zhang.aop.target.MethodMatcher;
import com.zhang.aop.target.TargetSource;

import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 用来生成拦截器chain
 *
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public class AdvisedSupport {

	/**
	 * 是否使用cglib代理
	 * 默认使用cglib
	 */
	private boolean proxyTargetClass = true;

	/**
	 * 目标对象
	 */
	private TargetSource targetSource;

	/**
	 * 匹配方法
	 */
	private MethodMatcher methodMatcher;


	private transient Map<Integer, List<Object>> methodCache;

	AdvisorChainFactory advisorChainFactory = new DefaultAdvisorChainFactory();

	private List<Advisor> advisors = new ArrayList<>();

	/**
	 * 无参构造时初始化方法缓存
	 */
	public AdvisedSupport() {
		this.methodCache = new ConcurrentHashMap<>(32);
	}
	public boolean isProxyTargetClass() {
		return proxyTargetClass;
	}

	public void setProxyTargetClass(boolean proxyTargetClass) {
		this.proxyTargetClass = proxyTargetClass;
	}

	public void addAdvisor(Advisor advisor) {
		advisors.add(advisor);
	}

	public void setAdvisors(List<Advisor> advisors) {
		this.advisors = advisors;
	}

	public List<Advisor> getAdvisors() {
		return advisors;
	}

	public TargetSource getTargetSource() {
		return targetSource;
	}

	public void setTargetSource(TargetSource targetSource) {
		this.targetSource = targetSource;
	}


	public MethodMatcher getMethodMatcher() {
		return methodMatcher;
	}

	public void setMethodMatcher(MethodMatcher methodMatcher) {
		this.methodMatcher = methodMatcher;
	}


	/**
	 * 用来返回方法的拦截器链
	 */
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(Method method, Class<?> targetClass) {
		Integer cacheKey = method.hashCode();
		List<Object> cached = this.methodCache.get(cacheKey);
		if (cached == null) {
			cached = this.advisorChainFactory.getInterceptorsAndDynamicInterceptionAdvice(
					this, method, targetClass);
			this.methodCache.put(cacheKey, cached);
		}
		return cached;
	}
}
