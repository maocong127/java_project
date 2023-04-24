package com.minis.context;
import com.minis.beans.BeanDefinition;
import com.minis.beans.BeanFactory;
import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory{
	BeanFactory beanFactory;
	
    public ClassPathXmlApplicationContext(String fileName){
    	Resource res = new ClassPathXmlResource(fileName);
    	BeanFactory bf = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(res);
        this.beanFactory = bf;
    }
    
	@Override
	public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
		return this.beanFactory.getBean(beanName);
	}

	@Override
	public void registerBeanDefinition(BeanDefinition bd) {
		this.beanFactory.registerBeanDefinition(bd);
		
	}

    
}
