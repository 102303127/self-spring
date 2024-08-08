package com.zhang.web.servlet.handler;

import javax.servlet.http.HttpServletRequest;

/**
 * 这种情况不进行实现
 *
 * @author zhang
 * @date 2024/7/29
 * @Description
 */
public class BeanNameUrlHandlerMapping extends AbstractHandlerMapping{
    @Override
    public void initApplicationContext() {
        super.initApplicationContext();
        detectHandlers();
    }

    protected void detectHandlers()  {
    }

    @Override
    protected HandlerMethod getHandlerInternal(HttpServletRequest request) throws Exception {
        return null;
    }

    @Override
    public int getOrder() {
        return 2;
    }
}
