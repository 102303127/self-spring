package com.zhang.beans;

/**
 * @author zhang
 * @date 2024/6/29
 * @Description
 */
public class BeansException extends Throwable {
    public BeansException(String msg) {
        super(msg);
    }

    public BeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
