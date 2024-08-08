package com.zhang.web.servlet.handler;

import java.lang.reflect.Method;


/**
 * @author zhang
 * @date 2024/7/29
 * @Description
 */
public class ExceptionHandlerMethod extends HandlerMethod{


    public ExceptionHandlerMethod(Object bean, Method method) {
        super(bean, method);
    }
}
