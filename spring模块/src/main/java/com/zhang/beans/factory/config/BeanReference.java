package com.zhang.beans.factory.config;

/**
 * 以抽象方式公开对 Bean 名称的引用的接口。
 * 此类并不一定意味着对实际 Bean 实例的引用;
 * 它只是表达了对 Bean 名称的逻辑引用。
 *
 * @author zhang
 * @date 2024/7/13
 * @Description
 */
public class BeanReference {

    private final String beanName;


    public BeanReference(String beanName) {
        this.beanName = beanName;
    }

    public String getBeanName() {
        return beanName;
    }
}
