package com.zhang.context.annotation;


import java.lang.annotation.*;

/**
 * @author zhang
 * @date 2024/8/15
 * @Description
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Documented
@Repeatable(ComponentScans.class)
public @interface ComponentScan {



	String[] value() default {};


	String[] basePackages() default {};


	Class<?>[] basePackageClasses() default {};


}