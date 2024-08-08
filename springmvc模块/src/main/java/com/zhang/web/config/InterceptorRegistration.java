package com.zhang.web.config;

import com.zhang.web.servlet.HandlerInterceptor;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * 封装拦截器信息
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class InterceptorRegistration {


    // 拦截器
    private HandlerInterceptor interceptor;
    // 拦截路径
    private List<String> includePatterns = new ArrayList<>();
    // 排除路径
    private List<String> excludePatterns = new ArrayList<>();

    public HandlerInterceptor getInterceptor() {
        return interceptor;
    }

    public List<String> getExcludePatterns() {
        return excludePatterns;
    }

    public List<String> getIncludePatterns() {
        return includePatterns;
    }

    public InterceptorRegistration addExcludePatterns(String... path) {

        this.excludePatterns.addAll(Arrays.asList(path));
        return this;
    }


    public InterceptorRegistration addPathPatterns(String... path) {
        this.includePatterns.addAll(Arrays.asList(path));
        return this;
    }

    public InterceptorRegistration setInterceptor(HandlerInterceptor interceptor) {
        this.interceptor = interceptor;
        return this;
    }

}
