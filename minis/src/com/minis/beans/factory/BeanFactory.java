package com.minis.beans.factory;

import com.minis.beans.BeansException;
import com.minis.beans.factory.support.NoSuchBeanDefinitionException;

public interface BeanFactory {
	Object getBean(String beanName) throws NoSuchBeanDefinitionException, BeansException;
	void registerBean(String beanName, Object obj);
	boolean contaionsBean(String beanName);

	boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException;
	boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException;
	Class<?> getType(String beanName) throws NoSuchBeanDefinitionException;
}
