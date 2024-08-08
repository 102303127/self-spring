package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class ListConvert extends Convert<ArrayList> {

    public ListConvert(Class<ArrayList> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {
        return this.type.getConstructor(Collection.class).newInstance(arg);

    }
}
