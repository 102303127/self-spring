package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class StringConvert extends Convert<String> {


    public StringConvert(Class<String> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
