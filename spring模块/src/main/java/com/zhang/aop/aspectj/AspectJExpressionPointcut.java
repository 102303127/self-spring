package com.zhang.aop.aspectj;



import com.zhang.aop.ClassFilter;
import com.zhang.aop.Pointcut;
import com.zhang.aop.target.MethodMatcher;
import org.aspectj.weaver.tools.PointcutExpression;
import org.aspectj.weaver.tools.PointcutParser;
import org.aspectj.weaver.tools.PointcutPrimitive;

import java.lang.reflect.Method;
import java.util.HashSet;
import java.util.Set;

/**
 * AspectJ切点
 *
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public class AspectJExpressionPointcut implements Pointcut, ClassFilter, MethodMatcher {

	private static final Set<PointcutPrimitive> SUPPORTED_PRIMITIVES = new HashSet<PointcutPrimitive>();

	static {
		SUPPORTED_PRIMITIVES.add(PointcutPrimitive.EXECUTION);
	}

	private final PointcutExpression pointcutExpression;

	/**
	 * AspectJExpressionPointcut 类的构造函数，
	 * 用于根据 AspectJ 切点表达式创建一个新的 AspectJExpressionPointcut 实例。
	 *
	 * @param expression AspectJ 切点表达式字符串
	 */
	public AspectJExpressionPointcut(String expression) {
		PointcutParser pointcutParser = PointcutParser.getPointcutParserSupportingSpecifiedPrimitivesAndUsingSpecifiedClassLoaderForResolution(SUPPORTED_PRIMITIVES, this.getClass().getClassLoader());
		pointcutExpression = pointcutParser.parsePointcutExpression(expression);
	}

	/**
	 * 判断给定的类是否与切点表达式匹配。
	 *
	 * @param clazz 要检查的类
	 * @return
	 */
	@Override
	public boolean matches(Class<?> clazz) {
		return pointcutExpression.couldMatchJoinPointsInType(clazz);
	}

	@Override
	public boolean matches(Method method, Class<?> targetClass) {
		return pointcutExpression.matchesMethodExecution(method).alwaysMatches();
	}

	@Override
	public ClassFilter getClassFilter() {
		return this;
	}

	@Override
	public MethodMatcher getMethodMatcher() {
		return this;
	}
}
