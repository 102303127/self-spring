package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class MapConvert extends Convert<HashMap> {

    public MapConvert(Class<HashMap> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Map.class).newInstance(arg);
    }
}
