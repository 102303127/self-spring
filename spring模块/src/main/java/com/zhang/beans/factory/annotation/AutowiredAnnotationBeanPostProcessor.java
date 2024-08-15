package com.zhang.beans.factory.annotation;

import cn.hutool.core.bean.BeanUtil;
import cn.hutool.core.util.TypeUtil;
import com.zhang.beans.BeansException;
import com.zhang.beans.PropertyValues;
import com.zhang.beans.factory.BeanFactory;
import com.zhang.beans.factory.BeanFactoryAware;
import com.zhang.beans.factory.config.ConfigurableListableBeanFactory;
import com.zhang.beans.factory.config.InstantiationAwareBeanPostProcessor;
import com.zhang.core.convert.ConversionService;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.util.LinkedHashSet;
import java.util.Set;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
public class AutowiredAnnotationBeanPostProcessor implements InstantiationAwareBeanPostProcessor, BeanFactoryAware {


    /**
     * 添加注解类型
     */
    private final Set<Class<? extends Annotation>> autowiredAnnotationTypes = new LinkedHashSet<>(4);

    private ConfigurableListableBeanFactory beanFactory;

    @Override
    public void setBeanFactory(BeanFactory beanFactory) {
        this.beanFactory = (ConfigurableListableBeanFactory) beanFactory;
    }


    public AutowiredAnnotationBeanPostProcessor() {
        this.autowiredAnnotationTypes.add(Autowired.class);
        this.autowiredAnnotationTypes.add(Value.class);
    }

    public void setAutowiredAnnotationType(Class<? extends Annotation> autowiredAnnotationType) {
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.add(autowiredAnnotationType);
    }

    public void setAutowiredAnnotationTypes(Set<Class<? extends Annotation>> autowiredAnnotationTypes) {
        this.autowiredAnnotationTypes.clear();
        this.autowiredAnnotationTypes.addAll(autowiredAnnotationTypes);
    }



    /**
     * 对PropertyValues进行后处理
     * （这里仅实现对@Value和@Autowire注解的填充）
     *
     * @param pvs 待处理的PropertyValues对象
     * @param bean 待处理的bean对象
     * @param beanName bean对象的名称
     */

    @Override
    public PropertyValues postProcessPropertyValues(PropertyValues pvs, Object bean, String beanName) throws Exception, BeansException {

        Class<?> clazz = bean.getClass();
        Field[] fields = clazz.getDeclaredFields();
        //处理@Value注解
        handleValueAnnotations(bean, fields);
        //处理@Autowired注解
        handleAutowireAnnotations(bean, fields);

        return pvs;
    }

    private void handleAutowireAnnotations(Object bean, Field[] fields) throws Exception, BeansException {
        for (Field field : fields) {
        Autowired autowiredAnnotation = field.getAnnotation(Autowired.class);
        if (autowiredAnnotation != null) {
            Class<?> fieldType = field.getType();
            Object dependentBean = beanFactory.getBean(fieldType);
            BeanUtil.setFieldValue(bean, field.getName(), dependentBean);
        }
    }
    }

    private void handleValueAnnotations(Object bean, Field[] fields) {
        for (Field field : fields) {
            Value valueAnnotation = field.getAnnotation(Value.class);
            if (valueAnnotation != null) {
                Object value = valueAnnotation.value();
                // 解析占位符，替换为实际值
                value = beanFactory.resolveEmbeddedValue((String) value);

                //类型转换
                Class<?> sourceType = value.getClass();
                Class<?> targetType = (Class<?>) TypeUtil.getType(field);
                ConversionService conversionService = beanFactory.getConversionService();
                if (conversionService != null) {
                    if (conversionService.canConvert(sourceType, targetType)) {
                        value = conversionService.convert(value, targetType);
                    }
                }

                BeanUtil.setFieldValue(bean, field.getName(), value);
            }
        }
    }

}
