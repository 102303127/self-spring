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
public @interface ComponentScans {

	ComponentScan[] value();

}