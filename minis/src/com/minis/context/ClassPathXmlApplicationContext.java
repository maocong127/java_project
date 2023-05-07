package com.minis.context;
import com.minis.beans.BeanFactory;
import com.minis.beans.NoSuchBeanDefinitionException;
import com.minis.beans.SimpleBeanFactory;
import com.minis.beans.XmlBeanDefinitionReader;
import com.minis.core.ClassPathXmlResource;
import com.minis.core.Resource;

public class ClassPathXmlApplicationContext implements BeanFactory,ApplicationEventPublisher{
	BeanFactory beanFactory;
	
	public ClassPathXmlApplicationContext(String fileName){
		this(fileName,true);
	}

    public ClassPathXmlApplicationContext(String fileName, boolean isRefresh){
    	Resource res = new ClassPathXmlResource(fileName);
    	SimpleBeanFactory bf = new SimpleBeanFactory();
        XmlBeanDefinitionReader reader = new XmlBeanDefinitionReader(bf);
        reader.loadBeanDefinitions(res);
        this.beanFactory = bf;
		if (isRefresh) {
			((SimpleBeanFactory) this.beanFactory).refresh();
		}
    }
    
	@Override
	public Object getBean(String beanName) throws NoSuchBeanDefinitionException {
		return this.beanFactory.getBean(beanName);
	}

	// @Override
	// public void registerBeanDefinition(BeanDefinition bd) {
	// 	this.beanFactory.registerBeanDefinition(bd);
		
	// }

	@Override
	public void registerBean(String beanName, Object obj) {
		this.beanFactory.registerBean(beanName, obj);
	}

	@Override
	public boolean contaionsBean(String beanName) {
		return this.beanFactory.contaionsBean(beanName);
	}

	@Override
	public void publishEvent(ApplicationEvent event) {
		
	}

	@Override
	public boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException {
		return false;
	}

	@Override
	public boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException {
		return false;
	}

	@Override
	public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
		return null;
	}


    
}
