package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

import java.util.Collection;
import java.util.HashSet;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class SetConvert extends Convert<HashSet> {

    public SetConvert(Class<HashSet> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Collection.class).newInstance(arg);
    }
}
