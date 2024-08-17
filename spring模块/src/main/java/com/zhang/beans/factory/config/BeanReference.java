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

    private final Class<?> beanType;

    private final boolean toParent;

    private Object source;



    public BeanReference(String beanName) {
        this(beanName, false);
    }


    public BeanReference(String beanName, boolean toParent) {
        this.beanName = beanName;
        this.beanType = null;
        this.toParent = toParent;
    }


    public BeanReference(Class<?> beanType) {
        this(beanType, false);
    }

    public BeanReference(Class<?> beanType, boolean toParent) {
        this.beanName = beanType.getName();
        this.beanType = beanType;
        this.toParent = toParent;
    }


    public String getBeanName() {
        return this.beanName;
    }


    public Class<?> getBeanType() {
        return this.beanType;
    }


    public boolean isToParent() {
        return this.toParent;
    }


    public void setSource(Object source) {
        this.source = source;
    }


    public Object getSource() {
        return this.source;
    }


    @Override
    public boolean equals( Object other) {
        if (this == other) {
            return true;
        }
        if (!(other instanceof BeanReference)) {
            return false;
        }
        BeanReference that = (BeanReference) other;
        return (this.beanName.equals(that.beanName) && this.beanType == that.beanType &&
                this.toParent == that.toParent);
    }

    @Override
    public int hashCode() {
        int result = this.beanName.hashCode();
        result = 29 * result + (this.toParent ? 1 : 0);
        return result;
    }

    @Override
    public String toString() {
        return '<' + getBeanName() + '>';
    }
}
