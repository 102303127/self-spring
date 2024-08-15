package com.zhang.context.annotation;

import java.lang.annotation.*;

/**
 * @author zhang
 * @date 2024/8/15
 * @Description
 */
@Target({ElementType.METHOD, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Bean {


    String[] value() default {};


    String[] name() default {};
}
