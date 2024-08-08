package com.zhang.web.resolver;


import com.zhang.web.exception.MvcException;
import com.zhang.web.servlet.HandlerExceptionResolver;
import com.zhang.web.servlet.handler.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 默认异常解析器，尽可能的枚举所有上层发生的异常进行处理
 *
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class DefaultHandlerExceptionResolver implements HandlerExceptionResolver {

    private int order;

    @Override
    public Boolean resolveException(HttpServletRequest request, HttpServletResponse response, HandlerMethod handler, Exception ex) throws IOException {

        // 错误处理统一500
        final Class<? extends Exception> type = ex.getClass();
        if (type == MvcException.class) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR,ex.getMessage());
            return true;
        }
        return false;
    }

    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public int getOrder() {
        return 1;
    }
}
