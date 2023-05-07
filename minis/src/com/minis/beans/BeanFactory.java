package com.minis.beans;

public interface BeanFactory {
	Object getBean(String beanName) throws NoSuchBeanDefinitionException;
	void registerBean(String beanName, Object obj);
	boolean contaionsBean(String beanName);

	boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException;
	boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException;
	Class<?> getType(String beanName) throws NoSuchBeanDefinitionException;
}
