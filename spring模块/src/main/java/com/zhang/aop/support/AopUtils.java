package com.zhang.aop.support;

import com.zhang.aop.Advisor;
import com.zhang.aop.PointcutAdvisor;
import com.zhang.aop.Pointcut;
import com.zhang.aop.target.MethodMatcher;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @date 2024/7/20
 * @Description
 */
public class AopUtils {

    /**
     * Find all eligible advisors that can apply to the given class.
     *
     * @param candidateAdvisors the list of candidate advisors
     * @param beanClass the target bean class
     * @return the list of advisors that can apply to the given class
     */
    public static List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass) {
        List<Advisor> eligibleAdvisors = new ArrayList<>();

        for (Advisor advisor : candidateAdvisors) {
            if (advisor instanceof PointcutAdvisor) {
                PointcutAdvisor pointcutAdvisor = (PointcutAdvisor) advisor;
                if (canApply(pointcutAdvisor.getPointcut(), beanClass)) {
                    eligibleAdvisors.add(advisor);
                }
            } else {
                eligibleAdvisors.add(advisor);
            }
        }

        return eligibleAdvisors;
    }


    /**
     * 判断给定的Pointcut是否适用于给定的目标类。
     *
     * @param pointcut 给定的Pointcut对象
     * @param targetClass 目标类的Class对象
     * @return 如果Pointcut适用于目标类，则返回true；否则返回false
     *
     */
    private static boolean canApply(Pointcut pointcut, Class<?> targetClass) {


        // 检查类级别的匹配项
        return pointcut.getClassFilter().matches(targetClass);
    }

}
