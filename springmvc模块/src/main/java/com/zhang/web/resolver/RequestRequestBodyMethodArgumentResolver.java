package com.zhang.web.resolver;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.core.MethodParameter;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.context.request.ServletWebRequest;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;


public class RequestRequestBodyMethodArgumentResolver implements HandlerMethodArgumentResolver {

    ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(RequestBody.class);
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {

        final String json = getJson(servletWebRequest.getRequest());
        return objectMapper.readValue(json, parameter.getParameterType());
    }

    public String getJson(HttpServletRequest request){

        final StringBuilder builder = new StringBuilder();
        String line = null;
        try(final BufferedReader reader = request.getReader()) {
            while(line != (line = reader.readLine())){
                builder.append(line);
            }
        }catch (Exception e){
            e.printStackTrace();
        }


        return builder.toString();
    }
}
