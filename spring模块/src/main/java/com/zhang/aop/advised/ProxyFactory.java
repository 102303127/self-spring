package com.zhang.aop.advised;

import com.zhang.aop.proxy.AopProxy;
import com.zhang.aop.proxy.CglibAopProxy;
import com.zhang.aop.proxy.JdkDynamicAopProxy;

/**
 * @author zhang
 * @date 2024/7/18
 * @Description
 */
public class ProxyFactory extends AdvisedSupport{

    public ProxyFactory() {
    }

    public Object getProxy() {
        return createAopProxy().getProxy();
    }


    private AopProxy createAopProxy() {
        if (this.isProxyTargetClass() || this.getTargetSource().getTargetClass().length == 0) {
            return new CglibAopProxy(this);
        }

        return new JdkDynamicAopProxy(this);
    }
}
