package com.minis.beans;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory,BeanDefinitionRegistry{
    private Map<String,BeanDefinition> beanDefinitionMap= new ConcurrentHashMap<>(25);
    private List<String> beanDefinitionNames =new ArrayList<>();
    // private Map<String, Object> singletons =new HashMap<>();

    public SimpleBeanFactory() {
    }

    public Object getBean(String beanName) throws NoSuchBeanDefinitionException{
        // 先尝试直接拿bean实例
		Object singleton = this.getSingleton(beanName);
		// 如果此时还没有这个bean实例，则获取它的定义来创建实例
        if (singleton == null) {
			BeanDefinition beanDefinition = beanDefinitionMap.get(beanName);
        	if (beanDefinition == null) {
        		throw new NoSuchBeanDefinitionException();
        	}
        	try {
				singleton = Class.forName(beanDefinition.getClassName()).newInstance();
			} catch (InstantiationException | IllegalAccessException | ClassNotFoundException e) { 
			}
			this.registerSingleton(beanName, singleton);
  	
        }
        return singleton;
    }
    public void registerBeanDefinition(BeanDefinition bd){
    	this.beanDefinitionMap.put(bd.getId(),bd);
    }

	@Override
	public void registerBean(String beanName, Object obj) {
		this.registerSingleton(beanName, obj);
	}

	@Override
	public boolean contaionsBean(String beanName) {
		return this.containsSingleton(beanName);
	}

	@Override
	public void registerBeanDefinition(String name, BeanDefinition bd) {
		this.beanDefinitionMap.put(name, bd);
		this.beanDefinitionNames.add(name);
		if (!bd.isLazyInit()) {
			try {
				this.getBean(name);
			} catch (NoSuchBeanDefinitionException e) {
				e.printStackTrace();
			}
		}
	}

	@Override
	public void removeBeanDefinition(String name) {
		this.beanDefinitionMap.remove(name);
		this.beanDefinitionNames.remove(name);
		this.removeSingleton(name);
	}

	@Override
	public BeanDefinition getBeanDefinition(String name) {
		return this.beanDefinitionMap.get(name);
	}

	@Override
	public boolean containsBeanDefinition(String name) {
		return this.beanDefinitionMap.containsKey(name);
	}

	@Override
	public boolean isSingleton(String beanName) throws NoSuchBeanDefinitionException {
		return this.containsSingleton(beanName);
	}

	@Override
	public boolean isPrototype(String beanName) throws NoSuchBeanDefinitionException {
		return this.beanDefinitionMap.get(beanName).isPrototype();
	}

	@Override
	public Class<?> getType(String beanName) throws NoSuchBeanDefinitionException {
		return this.beanDefinitionMap.get(beanName).getClass();
	}
    
}
