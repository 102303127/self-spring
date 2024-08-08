package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class FloatConvert extends Convert<Float> {


    public FloatConvert(Class<Float> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
