package com.zhang.web.resolver;


import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.HandlerMethodReturnValueHandler;
import com.zhang.web.servlet.handler.ServletInvocableMethod;
import com.zhang.web.servlet.HandlerExceptionResolver;
import com.zhang.web.servlet.handler.ExceptionHandlerMethod;
import com.zhang.web.servlet.handler.HandlerMethod;

import org.springframework.beans.factory.BeanFactoryUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 异常解析器
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class ExceptionHandlerExceptionResolver extends ApplicationObjectSupport implements HandlerExceptionResolver, InitializingBean {

    private int order;

    private Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    private HandlerMethodArgumentResolverComposite methodArgumentResolverComposite = new HandlerMethodArgumentResolverComposite();

    private HandlerMethodReturnValueHandlerComposite returnValueHandlerComposite = new HandlerMethodReturnValueHandlerComposite();

    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws Exception {
        final ExceptionHandlerMethod exceptionHandlerMethod = getExceptionHandlerMethod(handler, ex);

        if (!ObjectUtils.isEmpty(exceptionHandlerMethod)){

            final ServletWebRequest webServletRequest = new ServletWebRequest(request, response);
            final ServletInvocableMethod servletInvocableMethod = new ServletInvocableMethod();
            servletInvocableMethod.setExceptionHandlerMethodMap(exceptionHandlerMethodMap);
            servletInvocableMethod.setReturnValueHandlerComposite(returnValueHandlerComposite);
            servletInvocableMethod.setResolverComposite(methodArgumentResolverComposite);
            servletInvocableMethod.setHandlerMethod(exceptionHandlerMethod);
            Object[] args = {ex,exceptionHandlerMethod};
            servletInvocableMethod.invokeAndHandle(webServletRequest,exceptionHandlerMethod,args);
            return true;
        }
        return false;
    }

    public ExceptionHandlerMethod getExceptionHandlerMethod(HandlerMethod handlerMethod, Exception ex){
        Class aClass = ex.getClass();
        ExceptionHandlerMethod exceptionHandlerMethod = null;
        // 找局部
        if (handlerMethod!=null && handlerMethod.getExceptionHandlerMethodMap().size()!=0){
            final Map<Class, ExceptionHandlerMethod> exMap = handlerMethod.getExceptionHandlerMethodMap();
            while (exceptionHandlerMethod == null){
                exceptionHandlerMethod = exMap.get(aClass);
                aClass = aClass.getSuperclass();
                if (aClass == Throwable.class && exceptionHandlerMethod == null){
                    break;
                }
            }
        }
        aClass = ex.getClass();
        // 在找全局
        while (exceptionHandlerMethod == null){
            exceptionHandlerMethod = this.exceptionHandlerMethodMap.get(aClass);
            aClass = aClass.getSuperclass();
            if (aClass == Throwable.class && exceptionHandlerMethod == null){
                break;
            }
        }

        return exceptionHandlerMethod;
    }


    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return this.order;
    }

    // 初始化基础组件
    @Override
    public void afterPropertiesSet() throws Exception {
        exceptionHandlerMethodMap.putAll(initExceptionHandler());
        methodArgumentResolverComposite.addResolvers(getDefaultArgumentResolver());
        returnValueHandlerComposite.addMethodReturnValueHandlers(getDefaultMethodReturnValueHandler());
    }

    // 初始化返回值处理器
    public List<HandlerMethodReturnValueHandler> getDefaultMethodReturnValueHandler(){
        final ArrayList<HandlerMethodReturnValueHandler> handlerMethodReturnValueHandlers = new ArrayList<>();


        return handlerMethodReturnValueHandlers;
    }

    // 初始化参数解析器
    public List<HandlerMethodArgumentResolver> getDefaultArgumentResolver(){
        final ArrayList<HandlerMethodArgumentResolver> handlerMethodArgumentResolvers = new ArrayList<>();
        handlerMethodArgumentResolvers.add(new ServletRequestMethodArgumentResolver());
        handlerMethodArgumentResolvers.add(new ServletResponseMethodArgumentResolver());

        return handlerMethodArgumentResolvers;
    }




    // 初始化异常解析器
    public Map<Class, ExceptionHandlerMethod> initExceptionHandler(){
        final ApplicationContext context = obtainApplicationContext();
        Map<Class,ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();
        // 从容器当中拿带有@ControllerAdvice bean
        final String[] names = BeanFactoryUtils.beanNamesForAnnotationIncludingAncestors(context, ControllerAdvice.class);
        for (String name : names) {
            final Class<?> type = context.getType(name);
            final Method[] methods = type.getDeclaredMethods();
            for (Method method : methods) {
                if (AnnotatedElementUtils.hasAnnotation(method, ExceptionHandler.class)) {
                    final ExceptionHandler exceptionHandler = AnnotatedElementUtils.findMergedAnnotation(method, ExceptionHandler.class);
                    final Class<? extends Throwable>[] exType = exceptionHandler.value();
                    for (Class<? extends Throwable> aClass : exType) {
                        exceptionHandlerMethodMap.put(aClass,new ExceptionHandlerMethod(context.getBean(name),method));
                    }


                }
            }
        }

        return exceptionHandlerMethodMap;
    }
}
