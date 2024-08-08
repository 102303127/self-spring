package com.zhang.web.config;

import org.springframework.format.FormatterRegistry;

/**
 * webmvc的配置类
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public interface WebMvcConfigurer {

    /**
     * 添加拦截器
     * @param registry
     */
    default void addInterceptors(InterceptorRegistry registry){}

    /**
     * 添加格式化器(类型转换器)
     * @param registry
     */
    default void addFormatters(FormatterRegistry registry) {
    }
}

