package com.zhang.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public interface Resource {

    /**
     * 获得输入资源流
     * @return
     * @throws IOException
     */
    InputStream getInputStream() throws IOException;
}
