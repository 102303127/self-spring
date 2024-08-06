package com.zhang.context;

import com.zhang.beans.factory.Aware;

/**
 * @author zhang
 * @date 2024/7/23
 * @Description
 */
public interface ApplicationContextAware extends Aware {

    void setApplicationContext(ApplicationContext applicationContext);
}
