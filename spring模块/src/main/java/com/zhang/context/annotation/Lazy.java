package com.zhang.context.annotation;

import java.lang.annotation.*;


/**
 * 自定义注解
 *
 * @author zhang
 * @date 2024/8/13
 * @Description
 */
@Target({ElementType.TYPE, ElementType.METHOD, ElementType.CONSTRUCTOR, ElementType.PARAMETER, ElementType.FIELD})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Lazy {

	/**
	 * 是否应进行延迟初始化。
	 */
	boolean value() default true;

}