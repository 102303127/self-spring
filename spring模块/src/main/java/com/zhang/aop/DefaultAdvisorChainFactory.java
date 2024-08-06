package com.zhang.aop;

import com.zhang.aop.advised.AdvisedSupport;
import com.zhang.aop.target.MethodMatcher;
import org.aopalliance.intercept.MethodInterceptor;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;



/**
 * 默认的AdvisorChainFactory实现类
 *
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public class DefaultAdvisorChainFactory implements AdvisorChainFactory {

	/**
	 * 获取与指定配置、方法和目标类相关的拦截器和动态拦截建议。
	 *
	 * @param config 配置信息，包含多个Advisor
	 * @param method 目标方法
	 * @param targetClass 目标类，如果不为空则使用它，否则使用method所在的Class
	 * @return 包含所有匹配到的拦截器的列表，如果没有匹配到则返回空列表
	 */
	@Override
	public List<Object> getInterceptorsAndDynamicInterceptionAdvice(AdvisedSupport config, Method method, Class<?> targetClass) {
		// 获取所有的Advisor
		Advisor[] advisors = config.getAdvisors().toArray(new Advisor[0]);
		// 创建一个用于存储拦截器的列表
		List<Object> interceptorList = new ArrayList<>(advisors.length);
		// 获取目标类的Class对象，如果targetClass不为空则使用targetClass，否则使用method所在的Class
		Class<?> actualClass = (targetClass != null ? targetClass : method.getDeclaringClass());
		for (Advisor advisor : advisors) {
			// 如果Advisor是PointcutAdvisor类型
			if (advisor instanceof PointcutAdvisor) {
				// 将Advisor强制转换为PointcutAdvisor
				// Add it conditionally.
				PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
				// 校验当前Advisor是否适用于当前对象
				if (pointcutAdvisor.getPointcut().getClassFilter().matches(actualClass)) {
					// 获取Advisor的Pointcut的MethodMatcher
					MethodMatcher mm = pointcutAdvisor.getPointcut().getMethodMatcher();
					boolean match;
					// 校验Advisor是否应用到当前方法上
					match = mm.matches(method, actualClass);
					if (match) {
						// 获取Advisor的Advice，并将其转换为MethodInterceptor
						MethodInterceptor interceptor = (MethodInterceptor) advisor.getAdvice();
						// 将MethodInterceptor添加到拦截器列表中
						interceptorList.add(interceptor);
					}
				}
			}
		}
		// 返回拦截器列表
		return interceptorList;
	}
}
