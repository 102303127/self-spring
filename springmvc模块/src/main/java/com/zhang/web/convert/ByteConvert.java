package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class ByteConvert extends Convert<Byte> {


    public ByteConvert(Class<Byte> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
