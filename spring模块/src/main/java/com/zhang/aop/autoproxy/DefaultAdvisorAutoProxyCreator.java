package com.zhang.aop.autoproxy;

import com.zhang.aop.Advisor;
import com.zhang.aop.aspectj.AspectJExpressionPointcutAdvisor;
import com.zhang.aop.support.AopUtils;
import com.zhang.aop.target.TargetSource;
import com.zhang.beans.BeansException;
import com.zhang.beans.factory.config.BeanPostProcessor;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/19
 * @Description
 */
public class DefaultAdvisorAutoProxyCreator extends AbstractAutoProxyCreator  {


    /**
     * 返回与给定bean类、bean名称和自定义目标源相关的advice和advisors数组。
     *
     * @param beanClass bean的类
     * @param beanName bean的名称
     * @param customTargetSource 自定义的目标源，如果不需要则为null
     * @return 匹配的advice和advisors数组，如果没有则返回null
     */
    @Override
    protected Object[] getAdvicesAndAdvisorsForBean(Class<?> beanClass, String beanName, TargetSource customTargetSource) throws BeansException {

        List<Advisor> advisors = findEligibleAdvisors(beanClass, beanName);

        if (advisors.isEmpty()) {
            return null;
        }

        return advisors.toArray();
    }

    /**
     * 查找适用于给定beanClass和beanName的符合条件的Advisor列表。
     *
     * @param beanClass 目标bean的Class对象
     * @param beanName  目标bean的名称
     * @return 符合条件的Advisor列表
     */
    protected List<Advisor> findEligibleAdvisors(Class<?> beanClass, String beanName) {
        List<Advisor> candidateAdvisors = findCandidateAdvisors();
        List<Advisor> eligibleAdvisors = findAdvisorsThatCanApply(candidateAdvisors, beanClass, beanName);
        return eligibleAdvisors;
    }




    protected List<Advisor> findAdvisorsThatCanApply(List<Advisor> candidateAdvisors, Class<?> beanClass, String beanName) {

        return AopUtils.findAdvisorsThatCanApply(candidateAdvisors, beanClass);

    }


    /**
     * 查找候选的Advisor列表
     *
     * @return 包含所有AspectJExpressionPointcutAdvisor类型的bean的Advisor列表
     */
    private List<Advisor> findCandidateAdvisors() {
        Map<String, AspectJExpressionPointcutAdvisor> beansOfType = beanFactory.getBeansOfType(AspectJExpressionPointcutAdvisor.class);
        List<Advisor> advisors = new ArrayList<>(beansOfType.size());
        for (Advisor bean : beansOfType.values()) {
            if (bean != null) {
                advisors.add(bean);
            }
        }
        return advisors;
    }


}
