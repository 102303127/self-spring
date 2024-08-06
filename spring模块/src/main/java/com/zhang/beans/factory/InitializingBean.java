package com.zhang.beans.factory;

/**
 * @author zhang
 * @date 2024/7/15
 * @Description
 */
public interface InitializingBean {

    /**
     * 此方法允许 Bean 实例在设置了所有 Bean 属性后
     * 执行其整体配置的验证和最终初始化。
     * @throws Exception
     */
    void afterPropertiesSet() ;
}
