package com.zhang.web.servlet.handler;




import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;


import java.lang.reflect.Method;


/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public class RequestMappingHandlerMapping extends AbstractHandlerMethodMapping{



    @Override
    public void afterPropertiesSet() throws Exception {
        super.afterPropertiesSet();
    }

    @Override
    protected boolean isHandler(Class beanType) {
        return (AnnotatedElementUtils.hasAnnotation(beanType, Controller.class) ||
                AnnotatedElementUtils.hasAnnotation(beanType, RequestMapping.class));
    }


    @Override
    protected RequestMapping getMappingForMethod(Method method, Class handlerType) {
        return AnnotatedElementUtils.findMergedAnnotation(method, RequestMapping.class);
    }


}
