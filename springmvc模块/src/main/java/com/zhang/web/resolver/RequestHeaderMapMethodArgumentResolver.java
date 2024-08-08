package com.zhang.web.resolver;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class RequestHeaderMapMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        return parameter.hasParameterAnnotation(RequestHeader.class) && parameter.getParameterType() == Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {

        final HttpServletRequest request = servletWebRequest.getRequest();
        final Enumeration<String> headerNames = request.getHeaderNames();
        Map<String,String> resultMap = new HashMap<>();
        while (headerNames.hasMoreElements()) {
            final String key = headerNames.nextElement();
            final String value = request.getHeader(key);
            resultMap.put(key,value);
        }

        return resultMap;
    }
}
