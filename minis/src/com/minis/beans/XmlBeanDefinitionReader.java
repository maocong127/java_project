package com.minis.beans;

import java.util.List;

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

			// 处理属性
			List<Element> propertyElements = element.elements("property");
			PropertyValues propertyValues = new PropertyValues();
			for (Element propertyElement : propertyElements) {
				String name = propertyElement.attributeValue("name");
				String value = propertyElement.attributeValue("value");
				String type = propertyElement.attributeValue("type");
				propertyValues.addPropertyValue(new PropertyValue(name, value, type));
			}
			beanDefinition.setPropertyValues(propertyValues);
			
			// 处理构造器参数
			List<Element> constructorElements = element.elements("constructor-arg");
			ArgumentValues argumentValues = new ArgumentValues();
			for (Element constructorElement : constructorElements) {
				String name = constructorElement.attributeValue("name");
				String value = constructorElement.attributeValue("value");
				String type = constructorElement.attributeValue("type");
				argumentValues.addArgumentValue(new ArgumentValue(name, value, type));
			}
			beanDefinition.setConstructoArgumentValues(argumentValues);

            this.simpleBeanFactory.registerBeanDefinition(beanDefinition);
        }
		
	}
	


}
