package com.zhang.web.servlet;


import com.zhang.web.http.HttpMethod;
import org.springframework.beans.BeanUtils;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.web.context.ConfigurableWebApplicationContext;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.XmlWebApplicationContext;

import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public abstract class FrameworkServlet extends HttpServletBean implements ApplicationContextAware {

    /**
     * 要应用于上下文的实际 ApplicationContextInitializer 实例。
     */
    private final List<ApplicationContextInitializer<ConfigurableApplicationContext>> contextInitializers =
            new ArrayList<>();

    protected ApplicationContext webApplicationContext;

    /**
     * 要创建的 WebApplicationContext 实现类。
     */
    private Class<?> contextClass =  XmlWebApplicationContext.class;

    /**
     * 上下文配置文件的位置。
     */
    private String contextConfigLocation;


    public FrameworkServlet() {
    }

    public FrameworkServlet(ApplicationContext webApplicationContext) {
        this.webApplicationContext = webApplicationContext;
    }


    public Class<?> getContextClass() {
        return contextClass;
    }

    public void setContextClass(Class<?> contextClass) {
        this.contextClass = contextClass;
    }

    public String getContextConfigLocation() {
        return contextConfigLocation;
    }

    public void setContextConfigLocation(String contextConfigLocation) {
        this.contextConfigLocation = contextConfigLocation;
    }

    /**
     * 设置上下文初始化器。
     *
     * @param initializers 上下文初始化器，可以为null或空数组
     */
    public void setContextInitializers(ApplicationContextInitializer<?>... initializers) {
        if (initializers != null) {
            for (ApplicationContextInitializer<?> initializer : initializers) {
                this.contextInitializers.add((ApplicationContextInitializer<ConfigurableApplicationContext>) initializer);
            }
        }
    }






    @Override
    public void init(ServletConfig config) throws ServletException {
        super.init(config);
    }

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) {
        if (this.webApplicationContext == null && applicationContext instanceof WebApplicationContext) {
            this.webApplicationContext =  applicationContext;
        }
    }

    protected final void initServletBean() throws ServletException {
        this.webApplicationContext = initWebApplicationContext();
    }



    /**
     * 初始化 子容器
     *
     * @return 返回WebApplicationContext对象
     */
    protected ApplicationContext initWebApplicationContext() {
        // 获得父容器
        WebApplicationContext rootContext =
                WebApplicationContextUtils.getWebApplicationContext(getServletContext());

        ApplicationContext wac = null;
        //以spring配置方式时webApplicationContext!=null
        if (this.webApplicationContext != null) {
            // A context instance was injected at construction time -> use it
            wac = this.webApplicationContext;
            if (wac instanceof ConfigurableWebApplicationContext) {
                ConfigurableWebApplicationContext cwac = (ConfigurableWebApplicationContext) wac;
                if (!cwac.isActive()) {
                    // The context has not yet been refreshed -> provide services such as
                    // setting the parent context, setting the application context id, etc
                    if (cwac.getParent() == null) {
                        // The context instance was injected without an explicit parent -> set
                        // the root application context (if any; may be null) as the parent
                        cwac.setParent(rootContext);
                    }
                    configureAndRefreshWebApplicationContext(cwac);
                }
            }
        }
        if (wac == null) {
            //创建applicationContext
            wac = createWebApplicationContext(rootContext);
        }
        onRefresh(wac);
        return wac;
    }


    /**
     * 创建一个 子容器对象
     *
     * @param parent 父级ApplicationContext对象
     * @return 返回创建的WebApplicationContext对象
     */
    protected WebApplicationContext createWebApplicationContext(ApplicationContext parent) {
        //XmlWebApplicationContext
        Class<?> contextClass = getContextClass();
        //创建applicationContext
        ConfigurableWebApplicationContext wac =
                (ConfigurableWebApplicationContext) BeanUtils.instantiateClass(contextClass);

        //设置parent(ContextLoadListener中创建的applicationContext)
        wac.setParent(parent);

        //读取contextConfigLocation配置
        wac.setConfigLocation(getContextConfigLocation());

        //refresh()
        configureAndRefreshWebApplicationContext(wac);
        return wac;
    }


    /**
     * 模板方法，可以重写以添加特定于 servlet 的刷新工作。
     * 在成功刷新上下文后调用。
     *
     * @param context
     */
    protected void onRefresh(ApplicationContext context) {
        // 对于子类：默认情况下不执行任何操作。
    }

    /**
     * 配置并刷新WebApplicationContext对象
     *
     * @param wac 需要配置和刷新的WebApplicationContext对象
     */
    protected void configureAndRefreshWebApplicationContext(ConfigurableWebApplicationContext wac) {
        wac.setServletContext(getServletContext());
        wac.setServletConfig(getServletConfig());
        wac.refresh();
    }


    @Override
    protected void service(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpMethod httpMethod = HttpMethod.resolve(request.getMethod());
        if (httpMethod == HttpMethod.PATCH || httpMethod == null) {
            try {
                processRequest(request, response);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        else {
            super.service(request, response);
        }
    }

    @Override
    protected final void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    protected final void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected final void doPut(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    protected final void doDelete(HttpServletRequest request, HttpServletResponse response) {

        try {
            processRequest(request, response);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * 处理此请求，无论结果如何，都会发布事件。
     * 实际的事件处理由抽象模板方法执行。
     */
    protected final void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws Exception {

        doService(request, response);

    }


    /**
     * 子类必须实现此方法来执行请求处理工作，
     * 接收 GET、POST、PUT 和 DELETE 的集中回调。
     *
     * @param request
     * @param response
     * @throws Exception
     */
    protected abstract void doService(HttpServletRequest request, HttpServletResponse response)
            throws Exception;


}
