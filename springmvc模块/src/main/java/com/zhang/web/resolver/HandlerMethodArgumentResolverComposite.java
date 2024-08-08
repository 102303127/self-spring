package com.zhang.web.resolver;

import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class HandlerMethodArgumentResolverComposite implements HandlerMethodArgumentResolver {

    private final List<HandlerMethodArgumentResolver> argumentResolvers = new ArrayList<>();

    private final Map<MethodParameter, HandlerMethodArgumentResolver> argumentResolverCache =
            new ConcurrentHashMap<>(256);




    public HandlerMethodArgumentResolverComposite addResolver(HandlerMethodArgumentResolver resolver) {
        this.argumentResolvers.add(resolver);
        return this;
    }

    public HandlerMethodArgumentResolverComposite addResolvers(
            HandlerMethodArgumentResolver... resolvers) {

        if (resolvers != null) {
            Collections.addAll(this.argumentResolvers, resolvers);
        }
        return this;
    }

    public void addResolvers(List<HandlerMethodArgumentResolver> resolvers){
        this.argumentResolvers.addAll(resolvers);
    }


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        for (HandlerMethodArgumentResolver resolver : this.argumentResolvers) {
            if (resolver.supportsParameter(parameter)) {
                argumentResolverCache.put(parameter,resolver);
                return true;
            }
        }
        // 返回false,说明我们没有参数解析器可以应对当前请求携带数据的场景
        return false;
    }



    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest webRequest, ConvertComposite convertComposite) throws Exception {
        HandlerMethodArgumentResolver resolver = getResolverArgument(parameter);
        return resolver.resolveArgument(parameter, handlerMethod, webRequest, convertComposite);
    }

    protected HandlerMethodArgumentResolver getResolverArgument(MethodParameter parameter){
        return argumentResolverCache.get(parameter);
    }
}
