package com.zhang.web.servlet.handler;


import com.zhang.web.convert.config.ConvertHandler;
import org.springframework.core.MethodParameter;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * 包装对应类路径映射方法
 *
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public class HandlerMethod {
    protected Object bean;

    protected Class type;

    protected Method method;

    protected String path;

    protected RequestMethod[] requestMethods = new RequestMethod[0];

    // 增强的参数 plus
    protected MethodParameter[] parameters = new MethodParameter[0];

    private Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap = new HashMap<>();

    private Map<Class, ConvertHandler> convertHandlerMap = new HashMap<>();


    public HandlerMethod() {
    }

    public HandlerMethod(Object bean, Method method) {
        this.bean = bean;
        this.type = bean.getClass();
        this.method = method;
        final Parameter[] parameters = method.getParameters();
        MethodParameter[] methodParameters = new MethodParameter[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            methodParameters[i] = new MethodParameter(method, i);
        }
        this.parameters = methodParameters;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getPath() {
        return path;
    }

    // 如果为空，则设置所有请求类型
    public void setRequestMethods(RequestMethod[] requestMethods) {
        if (ObjectUtils.isEmpty(requestMethods)) {
            requestMethods = RequestMethod.values();
        }
        this.requestMethods = requestMethods;
    }

    public MethodParameter[] getParameters() {
        return parameters;
    }

    public RequestMethod[] getRequestMethods() {
        return requestMethods;
    }

    public Method getMethod() {
        return method;
    }

    public Object getBean() {
        return bean;
    }


    public void setExceptionHandlerMethodMap(Map<Class, ExceptionHandlerMethod> exceptionHandlerMethodMap) {
        this.exceptionHandlerMethodMap = exceptionHandlerMethodMap;
    }

    public Map<Class, ExceptionHandlerMethod> getExceptionHandlerMethodMap() {
        return exceptionHandlerMethodMap;
    }

    public void setConvertHandlerMap(Map<Class, ConvertHandler> convertHandlerMap) {
        this.convertHandlerMap = convertHandlerMap;
    }

    public Map<Class, ConvertHandler> getConvertHandlerMap() {
        return convertHandlerMap;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        HandlerMethod that = (HandlerMethod) o;
        return Objects.equals(path, that.path) &&
                Arrays.equals(requestMethods, that.requestMethods);
    }

    @Override
    public int hashCode() {
        int result = Objects.hash(path);
        result = 31 * result + Arrays.hashCode(requestMethods);
        return result;
    }
}
