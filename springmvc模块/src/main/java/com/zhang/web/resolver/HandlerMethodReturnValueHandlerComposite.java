package com.zhang.web.resolver;




import com.zhang.web.exception.MvcException;
import com.zhang.web.servlet.HandlerMethodReturnValueHandler;
import org.springframework.web.context.request.ServletWebRequest;

import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhang
 * @date 2024/7/30
 * @Description
 */

public class HandlerMethodReturnValueHandlerComposite {

    private List<HandlerMethodReturnValueHandler> methodReturnValueHandlers = new ArrayList<>();


    public void addMethodReturnValueHandlers(List<HandlerMethodReturnValueHandler> methodReturnValueHandlers) {
        this.methodReturnValueHandlers.addAll(methodReturnValueHandlers);
    }

    public HandlerMethodReturnValueHandler selectHandler(Method method) throws Exception{
        for (HandlerMethodReturnValueHandler returnValueHandler : this.methodReturnValueHandlers) {
            if (returnValueHandler.supportsReturnType(method)) {
                return returnValueHandler;
            }
        }
        throw new MvcException(method.toString() + "找不到返回值处理器");
    }

    public void doInvoke(Object returnValue, Method method, ServletWebRequest webServletRequest) throws Exception{
        // 选择返回值处理器
        final HandlerMethodReturnValueHandler returnValueHandler = selectHandler(method);
        // 执行
        returnValueHandler.handleReturnValue(returnValue,webServletRequest);
    }
}
