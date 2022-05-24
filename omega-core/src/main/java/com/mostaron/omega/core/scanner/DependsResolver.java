package com.mostaron.omega.core.scanner;

import com.mostaron.omega.core.annotation.Autowired;
import com.mostaron.omega.core.exception.OmegaCommonException;
import com.mostaron.omega.core.util.Assert;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.stream.Collectors;

/**
 * description: DependsResolver <br>
 * date: 2022/5/24:024 15:05:14 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class DependsResolver {

    private final static Logger logger = LoggerFactory.getLogger(DependsResolver.class);

    public static  Set<String> getDepends(Class<?> clazz) {
        Set<String> depends = new CopyOnWriteArraySet<>();
        try {
            depends.addAll(getDependsOnConstructor(clazz));
        } catch (NoSuchMethodException e) {
            logger.error("Error when try to get the constructor of {}", clazz.getName(), e);
            throw new OmegaCommonException("Error when try to get the constructor of {}", clazz.getName());
        }
        depends.addAll(getDependsOnSetter(clazz));
        depends.addAll(getDependsOnFields(clazz));

        return depends;
    }

    /**
     * 通过默认构造方法获取依赖类型列表
     * description: getDependsOnConstructor <br>
     * version: 1.0 <br>
     * date: 2022/5/24:024 16:01:52 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return java.util.List<java.lang.String>
     */
    private static Set<String> getDependsOnConstructor(Class<?> clazz) throws NoSuchMethodException {

        if(clazz.isInterface() || clazz.isEnum() || clazz.isAnnotation()) {
            return new HashSet<>();
        }


        //获取默认可用构造方法
        Constructor<?> constructor = getAvailableConstructor(clazz);
        Assert.notNull(constructor, String.format("Bean {} doesn't have any Constructor。", clazz.getName()));

        return Arrays.stream(constructor.getParameterTypes())
                .map(Class::getName)
                .collect(Collectors.toSet());

    }

    /**
     * 获取有效的构造方法。
     * 该方法优先获取带@Autowired标记的构造方法，若存在多于一个被@Autowired标记的构造方法，将抛出异常；
     * 若不存在使用@Autowired标记的构造方法，则将获取默认无参构造方法。
     * description: getAvailableConstructor <br>
     * version: 1.0 <br>
     * date: 2022/5/24:024 15:20:37 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return java.lang.reflect.Constructor<?>
     */
    public static  Constructor<?> getAvailableConstructor(Class<?> clazz) throws NoSuchMethodException {

        //获取当前Class的所有构造方法
        Constructor<?>[] constructors = clazz.getDeclaredConstructors();
        Constructor<?> defaultConstructor = null;
        Constructor<?> autowiredConstructor = null;
        for(Constructor<?> constructor: constructors) {
            //判断是否为默认空参构造器
            if(constructor.getParameterCount()==0 && null==defaultConstructor) {
                defaultConstructor = constructor;
                continue;
            }
            //判断是否为被@Autowired注解过的构造方法
            if(null != constructor.getAnnotation(Autowired.class)){
                if(null == autowiredConstructor) {
                    autowiredConstructor = constructor;
                    continue;
                }

                throw new OmegaCommonException("Bean {} already have an @Autowired constructor{}, but found {}",
                        clazz.getName(), autowiredConstructor.toGenericString(), constructor.toGenericString());

            }
        }
        //优先返回含Autowired注解的构造方法；
        //若不存在，则尝试返回默认空参构造方法；
        //若依然不存在，则尝试获取Class的默认构造方法，该方法有可能会抛出NoSuchMethodException
        return null != autowiredConstructor? autowiredConstructor:
                null != defaultConstructor? defaultConstructor: clazz.getDeclaredConstructor();
    }


    /**
     * 尝试获取使用了Autowired注解的Set方法，并通过这些方法整理依赖Bean列表
     * description: getDependsOnSetter <br>
     * version: 1.0 <br>
     * date: 2022/5/24:024 16:06:57 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return java.util.Set<java.lang.String>
     */
    private static Set<String> getDependsOnSetter(Class<?> clazz) {

        return Arrays.stream(clazz.getDeclaredMethods())
                //过滤未使用@Autowired标记的Set方法
                .filter(method -> null != method.getAnnotation(Autowired.class))
                //获取方法的参数列表
                .map(Method::getParameterTypes)
                //过滤空参方法
                .filter(params -> params.length != 0)
                //将多个参数列表Stream整合为一个Stream
                .flatMap(Arrays::stream)
                .distinct()
                //将Class对象转换为String的ClassName
                .map(Class::getName)
                .collect(Collectors.toSet());

    }

    /**
     * 尝试获取所有使用了Autowired标记的内部变量
     * description: getDependsOnFields <br>
     * version: 1.0 <br>
     * date: 2022/5/24:024 16:15:46 <br>
     * author: Neil <br>
     *
     * @param clazz
     * @return java.util.Set<java.lang.String>
     */
    private static Set<String> getDependsOnFields(Class<?> clazz) {

        return Arrays.stream(clazz.getDeclaredFields())
                // 过滤未使用Autowired的内部变量
                .filter(field -> null != field.getAnnotation(Autowired.class))
                //获取Field的Class，并获取其ClassName
                .map(Field::getType)
                .map(Class::getName)
                .collect(Collectors.toSet());
    }

}
