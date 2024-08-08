package com.zhang.web.servlet.handler;

import com.zhang.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * 处理程序执行链
 *
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public class HandlerExecutionChain {

    /**
     * 包装对应类路径映射方法
     */
    private final HandlerMethod handlerMethod;

    private List<HandlerInterceptor> interceptors = new ArrayList<>();

    public HandlerExecutionChain(HandlerMethod handlerMethod){
        this.handlerMethod = handlerMethod;
    }

    public HandlerMethod getHandlerMethod() {
        return handlerMethod;
    }

    public void setInterceptors(List<HandlerInterceptor> interceptors) {
        // 路径映射匹配
        for (HandlerInterceptor interceptor : interceptors) {
            if (interceptor instanceof MappedInterceptor){
                if (((MappedInterceptor)interceptor).match(handlerMethod.getPath())) {
                    this.interceptors.add(interceptor);
                }
            }else {
                this.interceptors.add(interceptor);
            }

        }

    }

    /**
     * 应用前置拦截器
     * 多个拦截器执行，一旦有一个拦截器返回false，整个链路可以崩掉
     *
     * @param req  HTTP请求对象
     * @param resp HTTP响应对象
     * @return 如果所有前置拦截器都执行成功则返回true，否则返回false
     */
    public boolean applyPreHandle(HttpServletRequest req, HttpServletResponse resp) {
        for (HandlerInterceptor interceptor : this.interceptors) {
            if (!interceptor.preHandle(req,resp)) {
                return false;
            }
        }
        return true;
    }

    /**
     * 执行已经注册的拦截器 postHandle()方法。
     *
     * @param req  HTTP请求对象
     * @param resp HTTP响应对象
     * @return 无返回值
     */
    public void applyPostHandle(HttpServletRequest req, HttpServletResponse resp) {

        for (HandlerInterceptor interceptor : this.interceptors) {
            interceptor.postHandle(req,resp);
        }
    }

    /**
     * 触发后置拦截器的afterCompletion方法
     *
     * @param req HTTP请求对象
     * @param resp HTTP响应对象
     * @param handlerMethod
     */
    public void triggerAfterCompletion(HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod, Exception ex) {
        for (HandlerInterceptor interceptor : this.interceptors) {
            interceptor.afterCompletion(req,resp,handlerMethod,ex);
        }
    }
}
