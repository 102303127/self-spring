package com.zhang.config;

import com.zhang.web.annotation.EnableWebMvc;
import com.zhang.web.config.InterceptorRegistry;
import com.zhang.web.config.WebMvcConfigurer;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;


@Configuration
@ComponentScan("com.zhang")
@EnableWebMvc
public class SpringMvcConfig implements WebMvcConfigurer {

    @Autowired
    private com.zhang.interceptor.projectInterceptor projectInterceptor;


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(projectInterceptor).addPathPatterns("/students","/students/*");
    }


}
