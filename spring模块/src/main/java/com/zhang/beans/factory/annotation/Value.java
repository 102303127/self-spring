package com.zhang.beans.factory.annotation;

import java.lang.annotation.*;

/**
 * @author zhang
 * @date 2024/8/14
 * @Description
 */
@Target({ElementType.FIELD, ElementType.METHOD, ElementType.PARAMETER, ElementType.ANNOTATION_TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Documented
public @interface Value {


    String value();

}
