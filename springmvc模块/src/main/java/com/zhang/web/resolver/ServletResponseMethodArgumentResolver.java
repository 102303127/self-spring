package com.zhang.web.resolver;

import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;


import javax.servlet.http.HttpServletResponse;

/**
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class   ServletResponseMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.getParameterType() == HttpServletResponse.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {
        return servletWebRequest.getResponse();
    }
}
