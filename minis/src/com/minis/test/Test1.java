package com.minis.test;

import com.minis.beans.BeansException;
import com.minis.beans.factory.support.NoSuchBeanDefinitionException;
import com.minis.context.ClassPathXmlApplicationContext;

public class Test1 {

	public static void main(String[] args) throws NoSuchBeanDefinitionException, BeansException {
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext("beans.xml",true);
	    AService aService=(AService)ctx.getBean("aservice");
	    aService.sayHello();
	}

}
