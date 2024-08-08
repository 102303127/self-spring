package com.zhang.web.config;

import com.zhang.web.resolver.DefaultHandlerExceptionResolver;
import com.zhang.web.resolver.ExceptionHandlerExceptionResolver;
import com.zhang.web.servlet.adapter.RequestMappingHandlerAdapter;
import com.zhang.web.servlet.handler.RequestMappingHandlerMapping;
import com.zhang.web.servlet.HandlerAdapter;
import com.zhang.web.servlet.HandlerExceptionResolver;
import com.zhang.web.servlet.HandlerMapping;
import com.zhang.web.servlet.handler.MappedInterceptor;
import org.springframework.context.annotation.Bean;

import java.util.List;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public abstract class WebMvcConfigurationSupport {

    // 初始化组件

    @Bean
    public HandlerMapping handlerMapping(){

        final RequestMappingHandlerMapping requestMappingHandlerMapping = new RequestMappingHandlerMapping();
        requestMappingHandlerMapping.setOrder(0);
        final InterceptorRegistry registry = new InterceptorRegistry();
        getIntercept(registry);
        //  通过 registry 获取 MappedInterceptor
        // 获取拦截器
        final List<MappedInterceptor> interceptors = registry.getInterceptors();
        requestMappingHandlerMapping.addHandlerInterceptors(interceptors);
        // 添加拦截器
        return requestMappingHandlerMapping;
    }

    protected abstract void getIntercept(InterceptorRegistry registry);

    @Bean
    public HandlerAdapter handlerAdapter(){
        final RequestMappingHandlerAdapter requestMappingHandlerAdapter = new RequestMappingHandlerAdapter();
        requestMappingHandlerAdapter.setOrder(0);
        return requestMappingHandlerAdapter;
    }

    @Bean
    public HandlerExceptionResolver defaultHandlerExceptionResolver(){

        final DefaultHandlerExceptionResolver defaultHandlerExceptionResolver = new DefaultHandlerExceptionResolver();
        defaultHandlerExceptionResolver.setOrder(1);
        return defaultHandlerExceptionResolver;
    }

    @Bean
    public HandlerExceptionResolver exceptionHandlerExceptionResolver(){

        final ExceptionHandlerExceptionResolver exceptionHandlerExceptionResolver = new ExceptionHandlerExceptionResolver();
        exceptionHandlerExceptionResolver.setOrder(0);
        return exceptionHandlerExceptionResolver;
    }

}

