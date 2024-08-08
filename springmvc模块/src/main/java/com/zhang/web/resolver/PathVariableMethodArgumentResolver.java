package com.zhang.web.resolver;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;


import javax.servlet.http.HttpServletRequest;
import java.util.HashMap;
import java.util.Map;

/**
 * 解析路径参数
 *
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class PathVariableMethodArgumentResolver implements HandlerMethodArgumentResolver {
    @Override
    public boolean supportsParameter(MethodParameter parameter) {
        return parameter.hasParameterAnnotation(PathVariable.class) && parameter.getParameterType() != Map.class;
    }

    @Override
    public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod, ServletWebRequest webServletRequest, ConvertComposite convertComposite) throws Exception {

        String name = "";
        Object result = null;
        // 1.获取 PathVariable 中的变量
        final PathVariable parameterAnnotation = parameter.getParameterAnnotation(PathVariable.class);
        name = parameterAnnotation.value().equals("") ?  parameter.getParameterName() : parameterAnnotation.value();
        Map<String,Object> resultMap = new HashMap<>();
        // 1.以/ 分割源path，找到变量 保存下标以及对应的变量
        final String path = handlerMethod.getPath();
        int index = -1;
        String[] split = path.split("/");
        for (int i = 0; i < split.length; i++) {
            final String s = split[i];
            if (s.contains("{") && s.contains("}") && s.contains(name)){
                index = i;
                break;
            }
        }
        final HttpServletRequest request = webServletRequest.getRequest();
        // 2.以/ 分割请求path，根据上一步找到的下标， 找到对应的值，放入resultMap
        split = request.getRequestURI().split("/");
        if (index != -1){
            result = split[index];
        }
        return convertComposite.convert(handlerMethod,parameter.getParameterType(),result);
    }

}
