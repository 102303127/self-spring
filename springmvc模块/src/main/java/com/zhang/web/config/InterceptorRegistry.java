package com.zhang.web.config;

import com.zhang.web.servlet.HandlerInterceptor;
import com.zhang.web.servlet.handler.MappedInterceptor;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 注册用户定义的拦截器
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class InterceptorRegistry {

    private List<InterceptorRegistration> interceptorRegistrations = new ArrayList<>();

    public InterceptorRegistration addInterceptor(HandlerInterceptor interceptor) {
        final InterceptorRegistration interceptorRegistration = new InterceptorRegistration();
        interceptorRegistration.setInterceptor(interceptor);
        interceptorRegistrations.add(interceptorRegistration);
        return interceptorRegistration;
    }


    // 转换成路径映射匹配的拦截器
    public List<MappedInterceptor> getInterceptors() {
        final List<MappedInterceptor> mappedInterceptorList = this.interceptorRegistrations.stream().map(MappedInterceptor::new).collect(Collectors.toList());
        return mappedInterceptorList;
    }
}
