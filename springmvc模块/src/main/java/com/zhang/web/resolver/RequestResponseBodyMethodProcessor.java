package com.zhang.web.resolver;

import com.fasterxml.jackson.annotation.JsonAutoDetect;
import com.fasterxml.jackson.annotation.PropertyAccessor;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.web.servlet.HandlerMethodReturnValueHandler;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.context.request.ServletWebRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;


public class RequestResponseBodyMethodProcessor implements HandlerMethodReturnValueHandler {

    // 避免对应实体类没有get方法
    final ObjectMapper objectMapper = new ObjectMapper().setVisibility(PropertyAccessor.FIELD, JsonAutoDetect.Visibility.ANY);

    @Override
    public boolean supportsReturnType(Method method) {
        return AnnotatedElementUtils.hasAnnotation(method.getDeclaringClass(), ResponseBody.class) || AnnotatedElementUtils.hasAnnotation(method,ResponseBody.class);
    }

    @Override
    public void handleReturnValue(Object returnValue, ServletWebRequest servletWebRequest) throws Exception {

        final HttpServletResponse response = servletWebRequest.getResponse();
        response.setCharacterEncoding("UTF-8");
        response.setContentType("application/json");
        response.getWriter().println(objectMapper.writeValueAsString(returnValue));
        response.getWriter().flush();


    }
}
