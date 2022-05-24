package com.mostaron.omega.core.beans.factory;

/**
 * BeanFactory Bean工厂抽象接口
 * description: BeanFactory <br>
 * date: 2022/5/16 16:40 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public interface BeanFactory {
    <T> T getBean(String beanName, Class<T> clazz);

    <T> T getBean(Class<T> clazz);

    Object getBean(String beanName);

    boolean containsBean(String beanName);

    boolean containsBean(Class<?> clazz);
}
