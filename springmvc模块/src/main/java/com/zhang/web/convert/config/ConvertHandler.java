package com.zhang.web.convert.config;

import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.util.ObjectUtils;

import java.lang.reflect.Method;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class ConvertHandler extends HandlerMethod {

    public ConvertHandler(Object bean, Method method) {
        super(bean,method);
    }

    public Object convert(Object arg) throws Exception {
        if (ObjectUtils.isEmpty(arg)){
            return null;
        }
        return this.getMethod().invoke(this.getBean(),arg);
    }

}
