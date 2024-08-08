package com.zhang.web.config;



import org.springframework.format.FormatterRegistry;

import java.util.ArrayList;
import java.util.List;

/**
 * mvc配置组合
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class WebMvcConfigurerComposite implements WebMvcConfigurer {

    private List<WebMvcConfigurer> webMvcConfigurers = new ArrayList<>();

    public void addWebMvcConfigurers(List<WebMvcConfigurer> webMvcConfigurers) {
        this.webMvcConfigurers.addAll(webMvcConfigurers);
    }


    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        for (WebMvcConfigurer webMvcConfigurer : webMvcConfigurers) {
            webMvcConfigurer.addInterceptors(registry);
        }
    }

    @Override
    public void addFormatters(FormatterRegistry registry) {
        for (WebMvcConfigurer delegate : this.webMvcConfigurers) {
            delegate.addFormatters(registry);
        }
    }
}


