package com.zhang.web.servlet;

import com.zhang.web.servlet.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public interface HandlerInterceptor {

    default boolean preHandle(HttpServletRequest request, HttpServletResponse response){
        return true;
    }

    default void  postHandle(HttpServletRequest request, HttpServletResponse response){}


    /**
     * 执行清理工作
     *
     * @param request
     * @param response
     * @param handler
     * @param ex
     */
    default void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler,
                                 Exception ex){
    }
}
