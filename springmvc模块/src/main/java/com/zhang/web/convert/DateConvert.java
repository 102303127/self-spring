package com.zhang.web.convert;

import com.zhang.web.convert.config.Convert;

import java.util.Date;
/**
 * @author zhang
 * @date 2024/7/26
 * @Description
 */
public class DateConvert extends Convert<Date> {


    public DateConvert(Class<Date> type) {
        super(type);
    }

    @Override
    public Object convert(Object arg) throws Exception {
        return defaultConvert(arg.toString());
    }
}
