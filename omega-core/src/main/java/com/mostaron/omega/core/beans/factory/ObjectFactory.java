package com.mostaron.omega.core.beans.factory;

/**
 * description: ObjectFactory <br>
 * date: 2022/5/30:030 14:08:04 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public interface ObjectFactory<T> {

    T getBean();

}
