package com.zhang;

import com.zhang.web.servlet.DispatcherServlet;
import com.zhang.web.servlet.adapter.RequestMappingHandlerAdapter;
import com.zhang.web.servlet.handler.RequestMappingHandlerMapping;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @author zhang
 * @date 2024/8/7
 * @Description
 */

@Configuration
@ComponentScan(
        basePackages = {"com.zhang"}
)
public class MvcAutoConfiguration {


    @Bean
    public DispatcherServlet dispatcherServlet() {
        return new DispatcherServlet();
    }

    @Bean
    public RequestMappingHandlerMapping requestMappingHandlerMapping() {
        return new RequestMappingHandlerMapping();
    }

    @Bean
    public RequestMappingHandlerAdapter requestMappingHandlerAdapter() {
        return new RequestMappingHandlerAdapter();
    }

}
