package com.zhang.web.resolver;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.exception.MvcException;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

/**
 * 获得解析请求头中的指定内容
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class RequestHeaderMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestHeader.class) && parameter.getParameterType() != Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {

        String name = "";
        final RequestHeader parameterAnnotation = parameter.getParameterAnnotation(RequestHeader.class);
        name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();

        final HttpServletRequest request = servletWebRequest.getRequest();
        if (parameterAnnotation.required() && ObjectUtils.isEmpty(request.getHeader(name))){
            throw new MvcException(handlerMethod.getPath() + "请求头没有携带: " + name);
        }
        return convertComposite.convert(handlerMethod,parameter.getParameterType(),request.getHeader(name));

    }
}
