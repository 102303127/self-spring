package com.zhang.web.resolver;

import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;


import javax.servlet.http.HttpServletRequest;

/**
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class ServletRequestMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.getParameterType() == HttpServletRequest.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {
        return servletWebRequest.getRequest();
    }
}
