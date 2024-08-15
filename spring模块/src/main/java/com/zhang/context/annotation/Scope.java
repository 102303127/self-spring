package com.zhang.context.annotation;

import java.lang.annotation.*;

/**
 * 自定义注解，用于指定bean的作用域
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Scope {

	String value() default "singleton";
}
