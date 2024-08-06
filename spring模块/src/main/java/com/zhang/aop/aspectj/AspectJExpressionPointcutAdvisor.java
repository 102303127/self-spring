package com.zhang.aop.aspectj;

import com.zhang.aop.Pointcut;
import com.zhang.aop.PointcutAdvisor;
import org.aopalliance.aop.Advice;


/**
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public class AspectJExpressionPointcutAdvisor implements PointcutAdvisor {

	private AspectJExpressionPointcut pointcut;

	private Advice advice;

	private String expression;

	public void setExpression(String expression) {
		this.expression = expression;
	}

	@Override
	public Pointcut getPointcut() {
		if (pointcut == null) {
			pointcut = new AspectJExpressionPointcut(expression);
		}
		return pointcut;
	}

	@Override
	public Advice getAdvice() {
		return advice;
	}

	public void setAdvice(Advice advice) {
		this.advice = advice;
	}
}
