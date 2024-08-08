package com.zhang.web.resolver;

import javax.servlet.http.HttpServletResponse;


import org.springframework.http.HttpHeaders;
import org.springframework.http.server.ServletServerHttpResponse;
import com.zhang.web.servlet.HandlerMethodReturnValueHandler;

import org.springframework.web.context.request.ServletWebRequest;


import java.lang.reflect.Method;

public class HttpHeadersReturnValueHandler implements HandlerMethodReturnValueHandler {

	@Override
	public boolean supportsReturnType(Method method) {
		return HttpHeaders.class.isAssignableFrom(method.getReturnType());
	}

	@Override
	public void handleReturnValue(Object returnValue, ServletWebRequest webServletRequest) throws Exception {
		HttpHeaders headers = (HttpHeaders) returnValue;
		if (!headers.isEmpty()) {
			HttpServletResponse servletResponse = webServletRequest.getNativeResponse(HttpServletResponse.class);
			ServletServerHttpResponse outputMessage = null;
			if (servletResponse != null) {
				outputMessage = new ServletServerHttpResponse(servletResponse);
			}
			outputMessage.getHeaders().putAll(headers);
			outputMessage.getBody();  // flush headers
		}
	}
}
