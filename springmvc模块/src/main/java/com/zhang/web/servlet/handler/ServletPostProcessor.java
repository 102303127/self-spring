package com.zhang.web.servlet.handler;

import org.springframework.beans.BeansException;
import org.springframework.beans.factory.config.BeanPostProcessor;
import org.springframework.web.context.ServletConfigAware;
import org.springframework.web.context.ServletContextAware;

import javax.servlet.ServletConfig;
import javax.servlet.ServletContext;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public class ServletPostProcessor implements BeanPostProcessor {

    private ServletContext servletContext;

    private ServletConfig servletConfig;

    public ServletPostProcessor(ServletContext servletContext, ServletConfig servletConfig) {
        this.servletContext = servletContext;
        this.servletConfig = servletConfig;
    }

    @Override
    public Object postProcessBeforeInitialization(Object bean, String beanName) throws BeansException {
        if (bean == null){
            return null;
        }
        if (bean instanceof ServletConfig) {
            ((ServletConfigAware)bean).setServletConfig(this.servletConfig);
        }
        if (bean instanceof ServletContext) {
            ((ServletContextAware)bean).setServletContext(this.servletContext);
        }
        return bean;
    }
}
