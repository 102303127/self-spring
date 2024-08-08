package com.zhang.web.servlet;

import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;


/**
 * 对请求中的参数进行解析
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public interface HandlerMethodArgumentResolver {

    /**
     * 判断当前参数是否支持当前的请求中携带的数据
     *
     * @Param parameter
      */
    boolean supportsParameter(MethodParameter parameter);

    /**
     * 解析参数
     *
     * @param parameter
     * @param handlerMethod
     * @param webServletRequest
     * @param convertComposite
     */
    Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest webServletRequest, ConvertComposite convertComposite) throws Exception;
}