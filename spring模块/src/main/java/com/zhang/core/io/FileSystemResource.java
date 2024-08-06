package com.zhang.core.io;

import java.io.IOException;
import java.io.InputStream;

/**
 * @author zhang
 * @date 2024/7/5
 * @Description
 */
public class FileSystemResource implements Resource{

    private final String Path;

    public FileSystemResource(String path) {
        Path = path;
    }


    @Override
    public InputStream getInputStream() throws IOException {
        return null;
    }
}
