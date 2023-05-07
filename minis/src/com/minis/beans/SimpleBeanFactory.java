package com.minis.beans;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
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

	private Object createBean(BeanDefinition beanDefinition) {
		Class<?> clz = null; 
		Object obj = null; 
		Constructor<?> con = null;
		try {
			clz = Class.forName(beanDefinition.getClassName());
			ArgumentValues argumentValues = beanDefinition.getConstructoArgumentValues();
			// 如果有参数
			if (! argumentValues.isEmpty()){
				// 获取参数类型和值
				Class<?>[] paramTypes = new Class<?>[argumentValues.getArgumentCount()];
				Object[] paramValues = new Object[argumentValues.getArgumentCount()];
				// 对每一个参数，分参数类型处理
				for (int i = 0;i < argumentValues.getArgumentCount();i++){
					ArgumentValue argumentValue = argumentValues.getIndexedArgumentValue(i);
					if ("String".equals(argumentValue.getType())){
						paramTypes[i] = String.class;
						paramValues[i] = argumentValue.getValue();
					} else if ("int".equals(argumentValue.getType())){
						paramTypes[i] = int.class;
						paramValues[i] = Integer.valueOf((String)argumentValue.getValue());
					} else if ("float".equals(argumentValue.getType())){
						paramTypes[i] = float.class;
						paramValues[i] = Float.valueOf((Float)argumentValue.getValue());
					} else if ("double".equals(argumentValue.getType())){
						paramTypes[i] = double.class;
						paramValues[i] = Double.valueOf((Double)argumentValue.getValue());
					} else if ("boolean".equals(argumentValue.getType())){
						paramTypes[i] = boolean.class;
						paramValues[i] = Boolean.valueOf((Boolean)argumentValue.getValue());
					} else {
						paramTypes[i] = Class.forName(argumentValue.getType());
						paramValues[i] = argumentValue.getValue();
					}
				}
                try {
                    // 按照特定的参数类型获取构造函数
                    con = clz.getConstructor(paramTypes);
                    // 通过构造函数创建实例
                    obj = con.newInstance(paramValues);
                } catch (NoSuchMethodException e) {
                    // 如果没有特定的构造函数，则获取默认构造函数
                    con = clz.getConstructor();
                    // 通过构造函数创建实例
                    obj = con.newInstance();
                }
			}

        } catch (Exception e) {} 
        // 处理属性
        PropertyValues propertyValues = beanDefinition.getPropertyValues();
        if (!propertyValues.isEmpty()) {
            for (int i = 0; i < propertyValues.size(); i++) {
                //对每一个属性，分数据类型分别处理
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pType = propertyValue.getType();
                String pName = propertyValue.getName();
                Object pValue = propertyValue.getValue();
                Class<?>[] paramTypes = new Class<?>[1];
               if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                    paramTypes[0] = String.class;
                } else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                    paramTypes[0] = Integer.class;
                } else if ("int".equals(pType)) {
                    paramTypes[0] = int.class;
                } else { // 默认为string
                    paramTypes[0] = String.class;
                }
                Object[] paramValues = new Object[1];
                paramValues[0] = pValue;

                //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0, 1).toUpperCase() + pName.substring(1);
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (NoSuchMethodException e){
                    e.printStackTrace();
                }

                try {
                    method.invoke(obj, paramValues);
                } catch (Exception e){
                    e.printStackTrace();
                }

            }
        }

        return obj;

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
