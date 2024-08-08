package com.zhang.web.resolver;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析 Map 用 @RequestParam 注释的方法参数，其中注释未指定请求参数名称
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class RequestParamMapMethodArgumentResolver implements HandlerMethodArgumentResolver {



    // 写RequestParam 不写都可以, 会遇到其他的也有不写的场景，HttpServletRequest Mule...
    @Override
    public boolean supportsParameter(MethodParameter parameter) {

        final Class<?> parameterType = parameter.getParameterType();
        if (parameterType == HttpServletResponse.class || parameterType == HttpServletRequest.class){
            return false;
        }
        if (parameter.hasParameterAnnotation(RequestBody.class)){
            return false;
        }

        return parameterType == Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {

        Map<String,Object> resultMap = new HashMap<>();

        final HttpServletRequest request = servletWebRequest.getRequest();
        final Map<String, String[]> parameterMap = request.getParameterMap();
        parameterMap.forEach((k,v)->{
            resultMap.put(k,v[0]);
        });
        return resultMap;
    }

}
