package com.mostaron.omega.demo.event.util;

import com.mostaron.omega.demo.event.event.OmegaEvent;
import com.mostaron.omega.demo.event.listener.DemoEventListener;
import com.mostaron.omega.demo.event.listener.OmegaEventListener;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;

/**
 * 泛型处理工具类
 * description: GenericTypeUtil <br>
 * date: 2022/5/13 14:32 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class GenericTypeUtil {

    /**
     * 获取指定class的泛型定义，仅支持通过extends继承的父类，未处理接口泛型
     * description: getGenericType <br>
     * version: 1.0 <br>
     * date: 2022/5/13 16:19 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return java.lang.reflect.Type[]
     */
    public static Type[] getGenericType(Class<?> clazz) {
        Type type = clazz.getGenericSuperclass();

        if(! (type instanceof ParameterizedType parameterizedType)){
            return null;
        }

        return parameterizedType.getActualTypeArguments();
    }

    /**
     * 获取class中定义的继承于targetSuperClass的类泛型定义类
     * description: getTargetClass <br>
     * version: 1.0 <br>
     * date: 2022/5/13 16:22 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @param targetSuperClass
     * @return java.lang.Class<?>
     */
    public static <T> Class<? extends T> getTargetClass(Class<?> clazz, Class<T> targetSuperClass) {
        Type[] types = getGenericType(clazz);
        if(null == types || types.length == 0){
            return null;
        }
        for(Type type: types){
            Class<?> parsedClass = parseType(type);
            if(targetSuperClass.isAssignableFrom(parsedClass)) {
                //noinspection unchecked
                return (Class<? extends T>) parsedClass;
            }
        }
        return null;
    }

    /**
     * 将Type定义转换为Class
     * @param type
     * @return
     */
    private static Class<?> parseType(Type type) {
        try {
            return Class.forName(type.getTypeName());
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            throw new RuntimeException("class " + type.getTypeName() +" not found ");
        }
    }

    public static void main(String...args) throws ClassNotFoundException {
        OmegaEventListener listener = new DemoEventListener();
        System.out.println(listener.getClass());

        System.out.println(getTargetClass(listener.getClass(), OmegaEvent.class));
    }

}
