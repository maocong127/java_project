package com.minis.beans;

import org.dom4j.Element;

import com.minis.core.Resource;

public class XmlBeanDefinitionReader {
	SimpleBeanFactory simpleBeanFactory;
	public XmlBeanDefinitionReader(SimpleBeanFactory simpleBeanFactory) {
		this.simpleBeanFactory = simpleBeanFactory;
	}
	public void loadBeanDefinitions(Resource res) {
        while (res.hasNext()) {
        	Element element = (Element)res.next();
            String beanID=element.attributeValue("id");
            String beanClassName=element.attributeValue("class");
            BeanDefinition beanDefinition=new BeanDefinition(beanID,beanClassName);
            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
		
	}
	


}
