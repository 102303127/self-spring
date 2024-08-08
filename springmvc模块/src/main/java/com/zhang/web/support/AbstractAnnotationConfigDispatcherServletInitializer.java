package com.zhang.web.support;

import org.springframework.context.annotation.AnnotationConfigApplicationContext;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;

import javax.servlet.Filter;

/**
 * WebApplicationInitializer 注册并使用 DispatcherServlet
 * 基于 Java 的 Spring 配置。
 * getRootConfigClasses() -- 用于“根”应用程序上下文（非 Web 基础架构）配置。
 * getServletConfigClasses() -- 用于 DispatcherServlet 应用程序上下文（Spring MVC 基础设施）配置。
 *
 * @author zhang
 * @date 2024/7/27
 * @Description
 */
public abstract class AbstractAnnotationConfigDispatcherServletInitializer
        extends AbstractDispatcherServletInitializer{


    /**
     * 创建父容器, 主要用来dao，service
     * @return
     */
    @Override
    protected WebApplicationContext createRootApplicationContext() {
        Class<?>[] configClasses = getRootConfigClasses();
        if (!ObjectUtils.isEmpty(configClasses)) {
            AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
            context.register(configClasses);
            return context;
        }
        else {
            return null;
        }
    }

    /**
     * 创建子容器, 主要用来controller
     * @return
     */
    @Override
    protected WebApplicationContext createServletApplicationContext() {
        AnnotationConfigWebApplicationContext context = new AnnotationConfigWebApplicationContext();
        Class<?>[] configClasses = getServletConfigClasses();
        if (!ObjectUtils.isEmpty(configClasses)) {
            context.register(configClasses);
        }
        return context;
    }

    protected abstract Class<?>[] getRootConfigClasses();


    protected abstract Class<?>[] getServletConfigClasses();
}
