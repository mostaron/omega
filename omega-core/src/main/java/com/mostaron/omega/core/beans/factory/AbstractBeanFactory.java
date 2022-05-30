package com.mostaron.omega.core.beans.factory;

import com.mostaron.omega.core.beans.BeanDefinition;
import com.mostaron.omega.core.beans.factory.exceptions.TypeNotMatchException;
import com.mostaron.omega.core.exception.OmegaCommonException;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: AbstractBeanFactory <br>
 * date: 2022/5/30:030 14:06:59 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public abstract class AbstractBeanFactory implements BeanFactory {

    /**
     * 一级缓存
     */
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>();

    /**
     * 二级缓存（预创建对象）
     */
    private final Map<String, Object> earlyObjects = new ConcurrentHashMap<>();

    /**
     * 三级缓存， 工厂Bean
     */
    private final Map<String, ObjectFactory<?>> factoryObjects = new ConcurrentHashMap<>();

    private final Map<String, BeanDefinition> beanDefinitions = new ConcurrentHashMap<>();


    public <T> T getBean(String beanName, Class<T> clazz){

        Object bean = getBean(beanName);
        if (null != bean){
            try{
                return (T)bean;
            } catch (ClassCastException e) {
                throw new TypeNotMatchException("Bean {} cannot cast into {}, raw type is {}"
                        , beanName, clazz.getName(), bean.getClass().getName());
            }
        }

        return null;
    }

    public <T> T getBean(Class<T> clazz){
        return getBean(clazz.getName(), clazz);
    }

    public Object getBean(String beanName){
        return null;
    }

    public boolean containsBean(String beanName){
        if(singletonObjects.containsKey(beanName)) {
            return true;
        }

        if(earlyObjects.containsKey(beanName)){
            return true;
        }

        if(factoryObjects.containsKey(beanName)) {
            return true;
        }

        return false;
    }

    public boolean containsBean(Class<?> clazz) {
        return containsBean(clazz.getName());
    }

    public BeanDefinition getBeanDefinition(String beanName) {
        return beanDefinitions.get(beanName);
    }

    protected <T> T doGetBean(String Name, Class<T> requiredType, Object[] args) {
        return null;
    }

}
