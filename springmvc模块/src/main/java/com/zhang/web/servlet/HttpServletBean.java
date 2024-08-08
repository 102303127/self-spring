package com.zhang.web.servlet;


import org.springframework.beans.*;
import org.springframework.beans.factory.config.BeanDefinition;
import org.springframework.beans.factory.support.RootBeanDefinition;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceEditor;
import org.springframework.core.io.ResourceLoader;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.Set;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public abstract class HttpServletBean extends HttpServlet {

    private final Set<String> requiredProperties = new HashSet<>(4);


    @Override
    public final void init() throws ServletException {
        // 从 init 参数设置 bean 属性。
        PropertyValues pvs = new ServletConfigPropertyValues(getServletConfig(), this.requiredProperties);

        //包装DispatcherServlet，准备放入容器
        BeanWrapper bw = PropertyAccessorFactory.forBeanPropertyAccess(this);
        bw.setPropertyValues(pvs, true);

        initServletBean();
    }




    protected void initServletBean() throws ServletException {
    }



    /**
     * 从 ServletConfig 初始化参数创建的 PropertyValues 实现。
     */
    private static class ServletConfigPropertyValues extends MutablePropertyValues {

        public ServletConfigPropertyValues(ServletConfig config, Set<String> requiredProperties) {

            Set<String> missingProps = new HashSet<>(requiredProperties) ;

            Enumeration<String> paramNames = config.getInitParameterNames();
            while (paramNames.hasMoreElements()) {
                String property = paramNames.nextElement();
                Object value = config.getInitParameter(property);
                addPropertyValue(new PropertyValue(property, value));
                if (missingProps != null) {
                    missingProps.remove(property);
                }
            }
        }
    }

}
