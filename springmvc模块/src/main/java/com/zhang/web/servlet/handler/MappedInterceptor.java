package com.zhang.web.servlet.handler;


import com.zhang.web.config.InterceptorRegistration;
import com.zhang.web.servlet.HandlerInterceptor;
import org.springframework.util.AntPathMatcher;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public class MappedInterceptor implements HandlerInterceptor{

    // 拦截器
    private  HandlerInterceptor interceptor;
    // 拦截路径
    private List<String> includePatterns = new ArrayList<>();
    // 排除路径
    private List<String> excludePatterns = new ArrayList<>();

    private final AntPathMatcher antPathMatcher = new AntPathMatcher();

    // 路径匹配
    public boolean match(String path){
        // 排除了，则不加入
        for (String excludePattern : this.excludePatterns) {
            if (antPathMatcher.match(excludePattern,path)){
                return false;
            }
        }
        // 如果匹配上则加入
        for (String includePattern : this.includePatterns) {
            if (antPathMatcher.match(includePattern,path)){
                return true;
            }
        }
        return false;
    }


    public MappedInterceptor(InterceptorRegistration interceptorRegistration){
        this.interceptor = interceptorRegistration.getInterceptor();
        this.includePatterns = interceptorRegistration.getIncludePatterns();
        this.excludePatterns = interceptorRegistration.getExcludePatterns();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response) {
        return interceptor.preHandle(request,response);
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response) {
        interceptor.postHandle(request,response);
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) {
        interceptor.afterCompletion(request,response,handler,ex);
    }
}
