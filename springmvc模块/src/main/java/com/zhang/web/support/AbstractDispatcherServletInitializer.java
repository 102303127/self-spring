package com.zhang.web.support;


import com.zhang.web.servlet.DispatcherServlet;
import com.zhang.web.servlet.FrameworkServlet;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.core.Conventions;
import org.springframework.util.ObjectUtils;
import org.springframework.web.context.AbstractContextLoaderInitializer;
import org.springframework.web.context.WebApplicationContext;

import javax.servlet.*;

/**
 * 创建DispatcherServlet 并初始化ioc容器
 * 利用模板方法模式，有子类实现方法
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public abstract class AbstractDispatcherServletInitializer extends AbstractContextLoaderInitializer {

    public static final String DEFAULT_SERVLET_NAME = "dispatcher";


    @Override
    public void onStartup(ServletContext servletContext) throws ServletException {
        super.onStartup(servletContext);
        registerDispatcherServlet(servletContext);
    }



    protected void registerDispatcherServlet(ServletContext servletContext) {
        String servletName = DEFAULT_SERVLET_NAME;

        // 创建容器
        WebApplicationContext servletAppContext = createServletApplicationContext();

        // 创建 DispatcherServlet
        // 容器放入 servletContext
        FrameworkServlet dispatcherServlet = createDispatcherServlet(servletAppContext);
        dispatcherServlet.setContextInitializers(getServletApplicationContextInitializers());

        // 放入 servletContext中
        ServletRegistration.Dynamic registration = servletContext.addServlet(servletName, dispatcherServlet);
        if (registration == null) {
            throw new IllegalStateException("无法使用名称 '" + servletName + "'注册 servlet. " +
                    "检查是否有另一个 servlet 以相同的名称注册.");
        }


        registration.setLoadOnStartup(1);
        registration.addMapping(getServletMappings());
        registration.setAsyncSupported(true);

        // 设置拦截器
        Filter[] filters = getServletFilters();
        if (!ObjectUtils.isEmpty(filters)) {
            for (Filter filter : filters) {
                registerServletFilter(servletContext, filter);
            }
        }

        // 自定义方法
        customizeRegistration(registration);
    }



    protected FilterRegistration.Dynamic registerServletFilter(ServletContext servletContext, Filter filter) {
        String filterName = Conventions.getVariableName(filter);
        FilterRegistration.Dynamic registration = servletContext.addFilter(filterName, filter);

        if (registration == null) {
            int counter = 0;
            while (registration == null) {
                if (counter == 100) {
                    throw new IllegalStateException("Failed to register filter with name '" + filterName + "'. " +
                            "Check if there is another filter registered under the same name.");
                }
                registration = servletContext.addFilter(filterName + "#" + counter, filter);
                counter++;
            }
        }

        return registration;
    }


    /**
     * 获取映射器
     * @return 返回一个字符串数组，表示映射器路径
     */
    protected String[] getMappings(){
        return new String[]{"/"};
    }


    /**
     * 创建子容器
     */
    protected abstract WebApplicationContext createServletApplicationContext();


    protected FrameworkServlet createDispatcherServlet(WebApplicationContext servletAppContext) {
        return new DispatcherServlet(servletAppContext);
    }



    protected ApplicationContextInitializer<?>[] getServletApplicationContextInitializers() {
        return null;
    }

    // 过滤器
    protected Filter[] getServletFilters() {
        return null;
    }

    // 拦截器
    protected abstract String[] getServletMappings();



    /**
     * 执行进一步的注册自定义(可忽略)
     * @param registration
     */
    protected void customizeRegistration(ServletRegistration.Dynamic registration) {
    }

}
