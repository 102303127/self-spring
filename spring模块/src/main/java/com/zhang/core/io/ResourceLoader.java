package com.zhang.core.io;

/**
 * 解析资源的接口
 *
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public interface ResourceLoader {
    /**
     * 从类路径加载的伪 URL 前缀：“classpath：”。
     */
    String CLASSPATH_URL_PREFIX = "classpath:";

    /**
     *获得资源
     * @param location
     * @return
     */
    Resource getResource(String location);
}
