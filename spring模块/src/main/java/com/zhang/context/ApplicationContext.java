package com.zhang.context;

import com.zhang.beans.factory.HierarchicalBeanFactory;
import com.zhang.beans.factory.ListableBeanFactory;
import com.zhang.core.io.ResourceLoader;

/**
 * @author zhang
 * @date 2024/7/3
 * @Description
 */
public interface ApplicationContext extends HierarchicalBeanFactory, ListableBeanFactory, ResourceLoader, ApplicationEventPublisher {
}
