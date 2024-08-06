

package com.zhang.context.support;


import com.zhang.beans.factory.BeanNameAware;
import com.zhang.beans.factory.InitializingBean;
import com.zhang.context.ApplicationContext;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public abstract class AbstractRefreshableConfigApplicationContext extends AbstractRefreshableApplicationContext
		implements BeanNameAware, InitializingBean {

	private String[] configLocations;

	public AbstractRefreshableConfigApplicationContext() {
	}

	public void setConfigLocation(String location) {
		this.configLocations = new String[] {location};
	}


	protected String[] getConfigLocations() {
		return (this.configLocations != null ? this.configLocations : getDefaultConfigLocations());
	}

	protected String[] getDefaultConfigLocations() {
		return null;
	}


}
