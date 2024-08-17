package com.zhang.beans.factory.support;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.ClassUtil;
import cn.hutool.core.util.StrUtil;
import cn.hutool.core.util.TypeUtil;
import com.zhang.beans.BeansException;
import com.zhang.beans.PropertyValue;
import com.zhang.beans.PropertyValues;
import com.zhang.beans.factory.Aware;
import com.zhang.beans.factory.BeanFactoryAware;
import com.zhang.beans.factory.BeanNameAware;
import com.zhang.beans.factory.InitializingBean;
import com.zhang.beans.factory.config.*;
import com.zhang.core.convert.ConversionService;
import com.zhang.beans.factory.support.beanDefinition.RootBeanDefinition;
import com.zhang.beans.factory.support.beanDefinitionInStantiationStrategy.InstantiationStrategy;
import com.zhang.beans.factory.support.beanDefinitionInStantiationStrategy.SimpleInstantiationStrategy;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.Statement;


/**
 *
 *
 * @author zhang
 * @date 2024/6/28
 * @Description
 */
public abstract class AbstractAutowireCapableBeanFactory extends AbstractBeanFactory implements AutowireCapableBeanFactory {

    /**
     * 对bean实例化的策略,这里只实现了简单实例化策略，直接赋值简单实例化策略。
     */
    private InstantiationStrategy instantiationStrategy = new SimpleInstantiationStrategy();

    @Override
    protected Object createBean(String beanName, RootBeanDefinition beanDefinition) throws BeansException, Exception {
        // Give BeanPostProcessors a chance to return a proxy instead of the target bean instance.
        // 这一步在Aop中直接返回null，Aop实现在实例化并填充属性之后
        Object bean = resolveBeforeInstantiation(beanName, beanDefinition);
        if (bean != null) {
            return bean;
        }
        return doCreateBean(beanName, beanDefinition);
    }

    /**
     * 执行InstantiationAwareBeanPostProcessor的方法，
     * 如果bean需要代理，直接返回代理对象
     *
     * @param beanName
     * @param beanDefinition
     * @return
     */
    protected Object resolveBeforeInstantiation(String beanName, RootBeanDefinition beanDefinition) throws BeansException {
        Object bean = applyBeanPostProcessorsBeforeInstantiation(beanDefinition.getBeanClass(), beanName);
        if (bean != null) {
            bean = applyBeanPostProcessorsAfterInitialization(bean, beanName);
        }
        return bean;
    }


    /**
     * 实例化之前执行BeanPostProcessors
     *
     * @param beanClass
     * @param beanName
     * @return
     */
    protected Object applyBeanPostProcessorsBeforeInstantiation(Class beanClass, String beanName) throws BeansException {
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                Object result = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessBeforeInstantiation(beanClass, beanName);
                if (result != null) {
                    return result;
                }
            }
        }

        return null;
    }



    protected Object doCreateBean(String beanName, RootBeanDefinition beanDefinition) throws BeansException, Exception {
        Object bean = createBeanInstance(beanDefinition);

        //为解决循环依赖问题，将实例化后的bean放进三级缓存中
        if (beanDefinition.isSingleton()) {
            Object finalBean = bean;
            // lambda表达式放进三级缓存中，调用getObject方法时，才会执行lambda表达式
            addSingletonFactory(beanName, () -> {
                try {
                    return getEarlyBeanReference(beanName, beanDefinition, finalBean);
                } catch (BeansException e) {
                    throw new RuntimeException(e);
                }
            });
        }


        // 填充属性值
        populateBean(beanName, beanDefinition, bean);


        // TODO 注册有销毁方法的bean(待实现)
        // registerDisposableBeanIfNecessary(beanName, bean, beanDefinition);


        // 初始化 bean 对象
        bean = initializeBean(beanName, bean, beanDefinition);
        return bean;
    }


    /**
     * 获取用于早期访问指定 Bean 的引用
     *
     * @param beanName
     * @param beanDefinition
     * @param bean
     * @return
     */
    protected Object getEarlyBeanReference(String beanName, BeanDefinition beanDefinition, Object bean) throws BeansException {
        Object exposedObject = bean;
        for (BeanPostProcessor bp : getBeanPostProcessors()) {
            if (bp instanceof InstantiationAwareBeanPostProcessor) {
                exposedObject = ((InstantiationAwareBeanPostProcessor) bp).getEarlyBeanReference(exposedObject, beanName);
                if (exposedObject == null) {
                    return exposedObject;
                }
            }
        }

        return exposedObject;
    }


    /**
     * 1.激活 Aware 方法
     * 2.执行bean的初始化方法
     * 3.BeanPostProcessor的前置和后置处理方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @return
     * @throws BeansException
     */
    protected Object initializeBean(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException, InvocationTargetException, IllegalAccessException {

        // 激活Aware方法
        invokeAwareMethods(beanName, bean);

        // 执行BeanPostProcessor的前置处理
        Object wrappedBean = applyBeanPostProcessorsBeforeInitialization(bean, beanName);

        // 执行bean初始化方法
        invokeInitMethods(beanName, bean, beanDefinition);

        // 执行BeanPostProcessor的后置处理
        wrappedBean = applyBeanPostProcessorsAfterInitialization(wrappedBean, beanName);

        return wrappedBean;
    }

    /**
     * 调用Aware方法
     *
     * @param beanName
     * @param bean
     */
    private void invokeAwareMethods(String beanName, Object bean) {
        if (bean instanceof Aware) {
            if (bean instanceof BeanNameAware) {
                ((BeanNameAware) bean).setBeanName(beanName);
            }
            if (bean instanceof BeanFactoryAware) {
                ((BeanFactoryAware) bean).setBeanFactory(AbstractAutowireCapableBeanFactory.this);
            }
        }
    }

    /**
     * 在初始化之前执行
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object applyBeanPostProcessorsBeforeInitialization(Object existingBean, String beanName)
            throws BeansException {
        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            Object current = processor.postProcessBeforeInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }


    /**
     * 执行bean的初始化方法
     *
     * @param beanName
     * @param bean
     * @param beanDefinition
     * @throws Throwable
     */
    protected void invokeInitMethods(String beanName, Object bean, BeanDefinition beanDefinition) throws BeansException, InvocationTargetException, IllegalAccessException {
        if (bean instanceof InitializingBean) {
            ((InitializingBean) bean).afterPropertiesSet();
        }
        // 执行自定义的初始化方法
        String initMethodName = beanDefinition.getInitMethodName();
        if (StrUtil.isNotEmpty(initMethodName) && !(bean instanceof InitializingBean && "afterPropertiesSet".equals(initMethodName))) {
            Method initMethod = ClassUtil.getPublicMethod(beanDefinition.getClass(), initMethodName);
            if (initMethod == null) {
                throw new BeansException("没有找到Bean"+ beanName + "的初始化方法");
            }
            initMethod.invoke(bean);
        }
    }

    /**
     * 在初始化之后实现
     *
     * @param existingBean
     * @param beanName
     * @return
     * @throws BeansException
     */
    @Override
    public Object applyBeanPostProcessorsAfterInitialization(Object existingBean, String beanName)
            throws BeansException {

        Object result = existingBean;
        for (BeanPostProcessor processor : getBeanPostProcessors()) {
            // 如果需要Aop操作，这里会返回代理对象
            Object current = processor.postProcessAfterInitialization(result, beanName);
            if (current == null) {
                return result;
            }
            result = current;
        }
        return result;
    }


    /**
     * 填充Bean
     * @param beanName
     * @param mbd
     * @param bean
     * @throws BeansException
     */
    protected void populateBean(String beanName, RootBeanDefinition mbd, Object bean) throws BeansException, Exception {

        // 在设置bean属性之前，允许BeanPostProcessor修改属性值
        for (BeanPostProcessor beanPostProcessor : getBeanPostProcessors()) {
            if (beanPostProcessor instanceof InstantiationAwareBeanPostProcessor) {
                PropertyValues pvs = ((InstantiationAwareBeanPostProcessor) beanPostProcessor).postProcessPropertyValues(mbd.getPropertyValues(), bean, beanName);
                if (pvs != null) {
                    for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                        mbd.getPropertyValues().addPropertyValue(propertyValue);
                    }
                }
            }
        }

        PropertyValues pvs = mbd.getPropertyValues();

        applyPropertyValues(beanName, bean, pvs);
    }



    /**
     * 为bean填充属性,设置Bean的属性值
     *
     */
    protected void applyPropertyValues(String beanName, Object bean, PropertyValues pvs) throws BeansException {
        try {
            for (PropertyValue propertyValue : pvs.getPropertyValues()) {
                String name = propertyValue.getName();
                Object value = propertyValue.getValue();
                // 如果是bean引用，则先实例化
                if (value instanceof BeanReference) {
                    // beanA依赖beanB，先实例化beanB
                    BeanReference beanReference = (BeanReference) value;
                    value = getBean(beanReference.getBeanName());
                } else {
                    //类型转换
                    Class<?> sourceType = value.getClass();
                    Class<?> targetType = (Class<?>) TypeUtil.getFieldType(bean.getClass(), name);
                    ConversionService conversionService = getConversionService();
                    if (conversionService != null) {
                        if (conversionService.canConvert(sourceType, targetType)) {
                            value = conversionService.convert(value, targetType);
                        }
                    }
                }

                //通过反射设置属性
                BeanUtil.setFieldValue(bean, name, value);
            }
        } catch (Exception ex) {
            throw new BeansException("Error setting property values for bean: " + beanName, ex);
        }
    }

    protected Object createBeanInstance(RootBeanDefinition beanDefinition) throws BeansException {
        return instantiationStrategy.instantiate(beanDefinition);
    }
}
