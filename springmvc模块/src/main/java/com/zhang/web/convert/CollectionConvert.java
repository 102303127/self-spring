package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

import java.util.ArrayList;
import java.util.Collection;

/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class CollectionConvert extends Convert<Collection> {

    public CollectionConvert(Class<Collection> type) {
        super(type);
    }

    @Override
    protected Object convert(Object arg) throws Exception {

        return ArrayList.class.getConstructor(Collection.class).newInstance(arg);
    }
}
