package com.zhang.web.servlet.handler;

import com.zhang.web.servlet.HandlerInterceptor;
import com.zhang.web.servlet.HandlerMapping;
import org.springframework.context.support.ApplicationObjectSupport;
import org.springframework.util.ObjectUtils;
import org.springframework.web.bind.annotation.RequestMethod;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public abstract class AbstractHandlerMapping extends ApplicationObjectSupport
        implements HandlerMapping {

    private List<HandlerInterceptor> handlerInterceptors = new ArrayList<>();


    public void addHandlerInterceptors(List<MappedInterceptor> handlerInterceptors) {
        this.handlerInterceptors.addAll(handlerInterceptors);
    }

    /**
     * 获得Handler
     * @param request
     * @return
     * @throws Exception
     */
    @Override
    public HandlerExecutionChain getHandler(HttpServletRequest request) throws Exception {

        HandlerMethod handlerMethod = getHandlerInternal(request);
        if (ObjectUtils.isEmpty(handlerMethod)) {
            return null;
        }
        HandlerExecutionChain executionChain = new HandlerExecutionChain(handlerMethod);
        executionChain.setInterceptors(handlerInterceptors);

        // 拦截器
        return executionChain;
    }


    protected HandlerMethod getHandlerMethod(Set<HandlerMethod> handlerMethods, HttpServletRequest request) throws Exception {
        final String requestMethod = request.getMethod();
        for (HandlerMethod handlerMethod : handlerMethods) {
            // RequestMapping 接受任意请求
            // GetMapping 接受get
            // DeleteMapping 接受delete
            for (RequestMethod method : handlerMethod.getRequestMethods()) {
                if (method.name().equals(requestMethod)){
                    return handlerMethod;
                }
            }
        }
        return null;
    }



    /**
     * 获取请求处理器方法
     *
     * @param request HTTP请求对象
     */
    protected abstract HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception;

}
