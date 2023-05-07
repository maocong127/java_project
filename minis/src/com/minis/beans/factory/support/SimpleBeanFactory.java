package com.minis.beans.factory.support;

import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.minis.beans.factory.BeanFactory;
import com.minis.beans.factory.config.ArgumentValue;
import com.minis.beans.factory.config.ArgumentValues;
import com.minis.beans.factory.config.BeanDefinition;
import com.minis.beans.factory.config.PropertyValue;
import com.minis.beans.factory.config.PropertyValues;

public class SimpleBeanFactory extends DefaultSingletonBeanRegistry implements BeanFactory,BeanDefinitionRegistry{
    private Map<String,BeanDefinition> beanDefinitionMap= new ConcurrentHashMap<>(25);
    private List<String> beanDefinitionNames =new ArrayList<>();
    // private Map<String, Object> singletons =new HashMap<>();
    private final Map<String, Object> earlySingletonObjects = new HashMap<String, Object>(16);

    public SimpleBeanFactory() {
    }

    public void refresh() {
        for (String bearnName:beanDefinitionNames) {
            try {
                getBean(bearnName);
            } catch (NoSuchBeanDefinitionException e) {
                e.printStackTrace();
            }
        }
    }
    

    // 获取Bean，对外暴露方法
    public Object getBean(String beanName) throws NoSuchBeanDefinitionException{
        // 先尝试直接从容器中获取bean实例
        System.out.println("尝试直接从容器中获取bean实例"+beanName);
        Object singleton = this.getSingleton(beanName);
        // 如果没有实例，则尝试从毛坯实例中获取
        if (singleton == null) {
            System.out.println("尝试从毛坯实例中获取");
            singleton = this.earlySingletonObjects.get(beanName);
            // 如果毛坯实例中没有，则创建Bean实例并注册
            if (singleton == null) {
                // 1. 先获取Bean定义
                BeanDefinition beanDefinition = this.beanDefinitionMap.get(beanName);
                // 2. 创建Bean实例
                singleton = this.createBean(beanDefinition);
                // 3. 注册Bean实例
                this.registerSingleton(beanName, singleton);
            }
        }
        return singleton;
    }

    private Object createBean(BeanDefinition beanDefinition) {
        Class<?> clz = null; 
        // 创建毛坯实例
        System.out.println("创建毛坯实例");
        Object object = this.doCreateBean(beanDefinition);
        // 毛坯实例放入毛坯实例缓存中
        this.earlySingletonObjects.put(beanDefinition.getId(), object);

        try {
            clz = Class.forName(beanDefinition.getClassName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        // 处理属性注入
        this.handleProperties(beanDefinition, clz, object);
        return object;
    }

    private Object doCreateBean(BeanDefinition beanDefinition) {
        Class<?> clz = null;
        Object obj = null; 
        Constructor<?> con = null;
        try {
            clz = Class.forName(beanDefinition.getClassName());
            ArgumentValues argumentValues = beanDefinition.getConstructorArgumentValues();
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
                }
            } else {
                // 如果没有参数，直接创建实例
                obj = clz.newInstance();
            }

        } catch (Exception e) {} 

        System.out.println("create bean : " + beanDefinition.getId());
        return obj;

    }

    
    private void handleProperties(BeanDefinition bd, Class<?> clz, Object obj) {
          // 处理属性
        System.out.println("handle properties for bean : " + bd.getId());
        PropertyValues propertyValues = bd.getPropertyValues();
          //如果有属性
        if (!propertyValues.isEmpty()) {
            for (int i=0; i<propertyValues.size(); i++) {
                PropertyValue propertyValue = propertyValues.getPropertyValueList().get(i);
                String pName = propertyValue.getName();
                String pType = propertyValue.getType();
                Object pValue = propertyValue.getValue();
                boolean isRef = propertyValue.getIsRef();
                System.out.println("pName : " + pName + ", pType : " + pType + ", pValue : " + pValue + ", isRef : " + isRef);
                Class<?>[] paramTypes = new Class<?>[1];          
                Object[] paramValues =   new Object[1];  
                if (!isRef) { //如果不是ref，只是普通属性
                      //对每一个属性，分数据类型分别处理
                    if ("String".equals(pType) || "java.lang.String".equals(pType)) {
                        paramTypes[0] = String.class;
                    }
                    else if ("Integer".equals(pType) || "java.lang.Integer".equals(pType)) {
                        paramTypes[0] = Integer.class;
                    }
                    else if ("int".equals(pType)) {
                        paramTypes[0] = int.class;
                    }
                    else {
                        paramTypes[0] = String.class;
                    }
            
                paramValues[0] = pValue;
                }
                else { //is ref, create the dependent beans
                    try {
                        System.out.println("pType : " + pType);
                        paramTypes[0] = Class.forName(pType);
                    } catch (ClassNotFoundException e) {
                        e.printStackTrace();
                    }
                    try {
                          //再次调用getBean创建ref的bean实例
                        paramValues[0] = getBean((String)pValue);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }   
    
                  //按照setXxxx规范查找setter方法，调用setter方法设置属性
                String methodName = "set" + pName.substring(0,1).toUpperCase() + pName.substring(1);                  
                Method method = null;
                try {
                    method = clz.getMethod(methodName, paramTypes);
                } catch (Exception e){
                    e.printStackTrace();
                }
                try {
                    method.invoke(obj, paramValues);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }    
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
