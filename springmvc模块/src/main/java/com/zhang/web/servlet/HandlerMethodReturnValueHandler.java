package com.zhang.web.servlet;
import org.springframework.web.context.request.ServletWebRequest;
import java.lang.reflect.Method;

/**
 * 返回值处理器
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public interface HandlerMethodReturnValueHandler {


    // 当前method 是否支持
    boolean supportsReturnType(Method method);

    // 执行
    void handleReturnValue(Object returnValue, ServletWebRequest webServletRequest) throws Exception;
}