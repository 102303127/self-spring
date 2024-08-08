package com.zhang.web.servlet;

import com.zhang.web.exception.MvcException;
import com.zhang.web.servlet.handler.HandlerExecutionChain;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.BeanInitializationException;
import org.springframework.context.ApplicationContext;
import org.springframework.core.OrderComparator;
import org.springframework.core.annotation.AnnotationAwareOrderComparator;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PropertiesLoaderUtils;
import org.springframework.util.ClassUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.*;

/**
 * 用于HTTP请求处理程序/控制器的中央调度器
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class DispatcherServlet extends FrameworkServlet{

    public static final String WEB_APPLICATION_CONTEXT_ATTRIBUTE = DispatcherServlet.class.getName() + ".CONTEXT";

    /**
     * 存储加载默认的HandlerMapping的内容
     */
    private static final String DEFAULT_STRATEGIES_PATH = "DispatcherServlet.properties";

    private List<HandlerMapping> handlerMappings = new ArrayList<>();

    private List<HandlerAdapter> handlerAdapters = new ArrayList<>();

    private List<HandlerExceptionResolver> handlerExceptionResolvers = new ArrayList<>();


    private Properties defaultStrategies;

    public DispatcherServlet() {
    }

    public DispatcherServlet(ApplicationContext webApplicationContext) {
        super(webApplicationContext);
    }

    @Override
    protected void onRefresh(ApplicationContext context) {
        initStrategies(context);
    }

    protected void initStrategies(ApplicationContext context) {
        // 初始化 文件上传解析器
        // initMultipartResolver(context);
        // 初始化 本地解析器
        // initLocaleResolver(context);
        // 初始化 主题解析器
        // initThemeResolver(context);
        // 初始化 url处理映射器
        initHandlerMappings(context);
        // 初始化真正调用controller方法的类
        initHandlerAdapters(context);
        // 初始化 异常解析器
        initHandlerExceptionResolvers(context);
        // 初始化请 求到视图名称转换器
        // initRequestToViewNameTranslator(context);
        // 初始化 视图解析器
        // initViewResolvers(context);
        // 初始化 FlashMap管理器
        // initFlashMapManager(context);
    }

    /**
     * 初始化 HandlerMapping
     * @param context Spring的ApplicationContext上下文对象
     */
    private void initHandlerMappings(ApplicationContext context) {
        // 这里默认实现加载全部的HandlerMapping
        // 从容器中拿
        Map<String, HandlerMapping> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerMapping.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerMappings = new ArrayList<HandlerMapping>(matchingBeans.values());
            // HandlerMappings 按排序顺序排列
            OrderComparator.sort(this.handlerMappings);
        }
        // 如果没有,则从默认配置文件中拿
        if (this.handlerMappings == null) {
            this.handlerMappings = getDefaultStrategies(context, HandlerMapping.class);
        }
    }

    /**
     * 初始化 HandlerAdapters
     * @param context Spring的ApplicationContext上下文对象
     */
    private void initHandlerAdapters(ApplicationContext context) {
        // 默认实现加载全部的HandlerAdapter
        // 从容器中拿
        Map<String, HandlerAdapter> matchingBeans =
                BeanFactoryUtils.beansOfTypeIncludingAncestors(context, HandlerAdapter.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerAdapters = new ArrayList<>(matchingBeans.values());
            // HandlerAdapters 按排序顺序排列
            AnnotationAwareOrderComparator.sort(this.handlerAdapters);
        }

        // 如果没有,则从默认配置文件中拿
        if (this.handlerAdapters == null) {
            this.handlerAdapters = getDefaultStrategies(context, HandlerAdapter.class);
        }
    }

    /**
     * 初始化 HandlerExceptionResolvers
     * @param context Spring的ApplicationContext上下文对象
     */
    private void initHandlerExceptionResolvers(ApplicationContext context) {
        // 这里默认实现加载全部的HandlerExceptionResolver
        // 从容器中拿
        Map<String, HandlerExceptionResolver> matchingBeans = BeanFactoryUtils
                .beansOfTypeIncludingAncestors(context, HandlerExceptionResolver.class, true, false);
        if (!matchingBeans.isEmpty()) {
            this.handlerExceptionResolvers = new ArrayList<>(matchingBeans.values());
            // We keep HandlerExceptionResolvers in sorted order.
            AnnotationAwareOrderComparator.sort(this.handlerExceptionResolvers);
        }
        // 如果没有,则从默认配置文件中拿
        if (this.handlerExceptionResolvers == null) {
            this.handlerExceptionResolvers = getDefaultStrategies(context, HandlerExceptionResolver.class);
        }
    }


    /**
     * 从默认策略配置文件中获取指定接口类型的默认策略列表
     * 这里直接拷贝的源码，时间不太够了，先这样用着
     *
     * @param context Spring的ApplicationContext上下文对象
     * @param strategyInterface 策略接口类型
     * @param <T> 泛型类型，表示策略接口的类型
     */
    protected <T> List<T> getDefaultStrategies(ApplicationContext context, Class<T> strategyInterface) {
        if (defaultStrategies == null) {
            try {
                ClassPathResource resource = new ClassPathResource(DEFAULT_STRATEGIES_PATH, DispatcherServlet.class);
                defaultStrategies = PropertiesLoaderUtils.loadProperties(resource);
            } catch (IOException ex) {
                throw new IllegalStateException("Could not load '" + DEFAULT_STRATEGIES_PATH + "': " + ex.getMessage());
            }
        }

        String key = strategyInterface.getName();
        String value = defaultStrategies.getProperty(key);
        if (value != null) {
            String[] classNames = StringUtils.commaDelimitedListToStringArray(value);
            List<T> strategies = new ArrayList<>(classNames.length);
            for (String className : classNames) {
                try {
                    Class<?> clazz = ClassUtils.forName(className, DispatcherServlet.class.getClassLoader());
                    Object strategy = context.getAutowireCapableBeanFactory().createBean(clazz);
                    strategies.add((T) strategy);
                } catch (ClassNotFoundException ex) {
                    throw new BeanInitializationException(
                            "Could not find DispatcherServlet's default strategy class [" + className +
                                    "] for interface [" + key + "]", ex);
                } catch (LinkageError err) {
                    throw new BeanInitializationException(
                            "Unresolvable class definition for DispatcherServlet's default strategy class [" +
                                    className + "] for interface [" + key + "]", err);
                }
            }
            return strategies;
        } else {
            return Collections.emptyList();
        }
    }




    @Override
    protected void doService(HttpServletRequest request, HttpServletResponse response) throws Exception {
        // 将相关配置设置到request作用于中

        // 将WebApplicationContext设置到request中
        request.setAttribute(WEB_APPLICATION_CONTEXT_ATTRIBUTE, this.webApplicationContext);

        // 将LocaleResolver设置到request中
        // request.setAttribute(LOCALE_RESOLVER_ATTRIBUTE, this.localeResolver);
        // 将ThemeResolver设置到request中
        // request.setAttribute(THEME_RESOLVER_ATTRIBUTE, this.themeResolver);
        // 将ThemeSource设置到request中
        // request.setAttribute(THEME_SOURCE_ATTRIBUTE, getThemeSource());
        // 创建一个新的FlashMap对象，并设置到request中
        // request.setAttribute(OUTPUT_FLASH_MAP_ATTRIBUTE, new FlashMap());
        // 将FlashMapManager设置到request中
        //  request.setAttribute(FLASH_MAP_MANAGER_ATTRIBUTE, this.flashMapManager);

        // 调用doDispatch方法，执行实际的请求处理逻辑
        doDispatch(request, response);

    }


    protected void doDispatch(HttpServletRequest request, HttpServletResponse response) throws Exception {

        Exception ex = null;
        HandlerExecutionChain handlerExecutionChain = null;
        try {
            handlerExecutionChain = getHandler(request);
            if (ObjectUtils.isEmpty(handlerExecutionChain)){
                response.sendError(HttpServletResponse.SC_NOT_FOUND);
                return;
            }

            // 获得适配器
            HandlerAdapter ha = getHandlerMethodAdapter(handlerExecutionChain.getHandlerMethod());
            if (!handlerExecutionChain.applyPreHandle(request,response)) {
                return;
            }
            ha.handler(request,response,handlerExecutionChain.getHandlerMethod());
            handlerExecutionChain.applyPostHandle(request,response);
        } catch (InvocationTargetException ite){
            ex = (Exception) ite.getCause();
        } catch (Exception e) {
            throw new MvcException(e.getMessage());
        }
        try {
            processResult(request,response,handlerExecutionChain,ex);
        } catch (Exception e) {
            throw new ServletException(e.getMessage());
        }

    }



    private void processResult(HttpServletRequest req, HttpServletResponse resp, HandlerExecutionChain handlerExecutionChain, Exception ex) throws Exception {

        if (ex!=null){
            HandlerMethod handlerMethod = handlerExecutionChain == null ? null : handlerExecutionChain.getHandlerMethod();
            processResultException(req,resp,handlerMethod,ex);
        }

        handlerExecutionChain.triggerAfterCompletion(req,resp,handlerExecutionChain.getHandlerMethod(),ex);
    }

    /**
     * 处理结果异常
     *
     * @param req HTTP请求对象
     * @param resp HTTP响应对象
     * @param handlerMethod 处理该请求的处理器方法
     * @param ex 发生的异常
     */
    private void processResultException(HttpServletRequest req, HttpServletResponse resp, HandlerMethod handlerMethod, Exception ex) throws Exception {

        for (HandlerExceptionResolver handlerExceptionResolver : this.handlerExceptionResolvers) {
            if (handlerExceptionResolver.resolveException(req, resp, handlerMethod, ex)) {
                return;
            }
        }
        throw new ServletException(ex.getMessage());
    }

    /**
     * 获取处理器方法适配器
     *
     * @param handlerMethod 处理器方法
     * @return 返回处理器方法适配器
     * @throws Exception 如果找不到支持的适配器，则抛出MvcException异常
     */
    private HandlerAdapter getHandlerMethodAdapter(HandlerMethod handlerMethod) throws Exception {
        for (HandlerAdapter handlerAdapter : this.handlerAdapters) {
            if (handlerAdapter.support(handlerMethod)) {
                return handlerAdapter;
            }
        }
        throw new MvcException(handlerMethod + "没有支持的适配器");
    }


    /**
     * 返回此请求的 HandlerExecutionChain。
     * 按顺序尝试所有处理程序映射。
     *
     * @param req
     * @return
     * @throws Exception
     */
    private HandlerExecutionChain getHandler(HttpServletRequest req) throws Exception {
        // 拿到所有组件进行遍历
        for (HandlerMapping handlerMapping : handlerMappings) {
            final HandlerExecutionChain handler = handlerMapping.getHandler(req);
            if (handler!=null){
                return handler;
            }
        }
        return null;
    }

}
