package com.zhang.web.resolver;

import java.util.HashMap;
import java.util.Map;


import com.zhang.web.convert.config.ConvertComposite;
import com.zhang.web.servlet.HandlerMethodArgumentResolver;
import com.zhang.web.servlet.handler.HandlerMethod;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.context.request.ServletWebRequest;
import org.springframework.core.MethodParameter;


import javax.servlet.http.HttpServletRequest;

/**
 * @author zhang
 * @date 2024/7/30
 * @Description
 */
public class PathVariableMapMethodArgumentResolver implements HandlerMethodArgumentResolver {

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(PathVariable.class) && parameter.getParameterType() == Map.class;
	}

	/**
	 * 将路径参数解析组装成Map返回
	 * 源码中使用webRequest.getAttribute()
	 * 该方法直接借用github上的代码
	 *
	 * @param parameter 方法参数信息
	 * @param handlerMethod 处理该请求的处理器方法
	 * @param webRequest 当前请求的ServletWebRequest对象
	 * @param convertComposite 类型转换复合器
	 */
	@Override
	public Object resolveArgument(MethodParameter parameter, HandlerMethod handlerMethod,
								  ServletWebRequest webRequest, ConvertComposite convertComposite) throws Exception {

		// 目标: 将所有路径上的参数进行解析组装成map返回
		Map<String,Object> resultMap = new HashMap<>();
		Map<Integer,String> indexMap = new HashMap<>();
		// 1.以/ 分割源path，找到变量 保存下标以及对应的变量
		final String path = handlerMethod.getPath();
		String[] split = path.split("/");
		for (int i = 0; i < split.length; i++) {
			final String s = split[i];
			if (s.contains("{") && s.contains("}")){
				indexMap.put(i, s.substring(1, s.length()-1));
			}
		}
		final HttpServletRequest request = webRequest.getRequest();
		// 2.以/ 分割请求path，根据上一步找到的下标， 找到对应的值，放入resultMap
		split = request.getRequestURI().split("/");
		for (Integer index : indexMap.keySet()) {
			resultMap.put(indexMap.get(index),split[index]);
		}
		return resultMap;
	}

}
