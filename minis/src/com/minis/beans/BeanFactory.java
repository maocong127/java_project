package com.minis.beans;

public interface BeanFactory {
	Object getBean(String beanName) throws NoSuchBeanDefinitionException;
	void registerBean(String beanName, Object obj);
	boolean contaionsBean(String beanName);
}
