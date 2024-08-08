package com.zhang.web.resolver;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.beans.BeanUtils;
import org.springframework.core.MethodParameter;
import org.springframework.util.ReflectionUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Field;
import java.util.Map;

/**
 * 解析 Map 用 @RequestParam 注释的方法参数，其中注释未指定请求参数名称
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class RequestParamMethodArgumentResolver implements HandlerMethodArgumentResolver {


    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        final Class<?> parameterType = parameter.getParameterType();
        if (parameterType == HttpServletResponse.class || parameterType == HttpServletRequest.class){
            return false;
        }
        if (parameterType == Map.class){
            return false;
        }
        if (parameter.hasParameterAnnotation(RequestBody.class)){
            return false;
        }
        if (parameter.hasParameterAnnotation(PathVariable.class)){
            return false;
        }
        return true;
    }



    // 需要处理对象 需要处理基本数据类型
    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest servletWebRequest, ConvertComposite convertComposite) throws Exception {

        final Class<?> parameterType = parameter.getParameterType();
        final HttpServletRequest request = servletWebRequest.getRequest();
        // 基础数据类型
        if (BeanUtils.isSimpleProperty(parameterType)){
            String name = parameter.getParameterName();
            if (parameter.hasParameterAnnotation(RequestParam.class)){
                final RequestParam parameterAnnotation = parameter.getParameterAnnotation(RequestParam.class);
                name = parameterAnnotation.value().equals("") ? parameter.getParameterName() : parameterAnnotation.value();
            }
            return convertComposite.convert(handlerMethod,parameterType,request.getParameter(name));
         // 对象
        }else {
            // 0.如果当前标注了RequestParam则报错
            if (parameter.hasParameterAnnotation(RequestParam.class)) {
                throw new IllegalArgumentException(handlerMethod.getBean().getClass().getName() +" "+ handlerMethod.getMethod().getName() + "@RequestParam 不支持标注在对象上");
            }
            // 1.获取所有参数
            final Map<String, String[]> parameterMap = request.getParameterMap();
            // 2.创建对象
            final Object o = ReflectionUtils.accessibleConstructor(parameterType).newInstance();
            // 3.遍历对象中的字段赋值
            final Field[] fields = parameterType.getDeclaredFields();
            initObject(parameterMap,o,fields,handlerMethod,convertComposite);
            return o;
        }
    }

    public void initObject(Map<String, String[]> parameterMap,Object o,Field[] fields,HandlerMethod handlerMethod,ConvertComposite convertComposite) throws Exception {
        for (Field field : fields) {
            final Class<?> type = field.getType();
            if (BeanUtils.isSimpleProperty(type)){
                // 对象
                // 基础数据类型
                if (parameterMap.containsKey(field.getName())) {
                    field.setAccessible(true);
                    field.set(o,convertComposite.convert(handlerMethod, type,parameterMap.get(field.getName())[0]));
                    field.setAccessible(false);
                }
            }else {
                final Object o1 = ReflectionUtils.accessibleConstructor(type).newInstance();
                // 属性填充
                final Field[] fields1 = type.getDeclaredFields();
                initObject(parameterMap,o1,fields1,handlerMethod,convertComposite);
                field.setAccessible(true);
                field.set(o,o1);
                 field.setAccessible(false);
            }

        }
    }


}
