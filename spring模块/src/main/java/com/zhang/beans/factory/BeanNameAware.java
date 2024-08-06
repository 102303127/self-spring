package com.zhang.beans.factory;

/**
 * @author zhang
 * @date 2024/7/16
 * @Description
 */
public interface BeanNameAware extends Aware{

    /**
     * 在创建此 Bean 的 Bean 工厂中设置 Bean 的名称。
     * @param name
     */
    void setBeanName(String name);

}
