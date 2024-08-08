package com.zhang.web.servlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.zhang.web.servlet.handler.HandlerMethod;

/**
 * @author zhang
 * @date 2024/7/28
 * @Description
 */
public interface HandlerAdapter {

    boolean support(HandlerMethod handlerMethod);

    void handler(HttpServletRequest req, HttpServletResponse res, HandlerMethod handler) throws Exception;



}
