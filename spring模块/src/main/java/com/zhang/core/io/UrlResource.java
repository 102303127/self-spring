package com.zhang.core.io;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;


/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public class UrlResource implements Resource{

    private final URL Url;

    public UrlResource(URL url) {
        Url= url;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
