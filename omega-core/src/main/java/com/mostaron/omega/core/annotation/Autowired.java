package com.mostaron.omega.core.annotation;

import java.lang.annotation.*;

/**
 * 自动注入注解<br/>
 * description: Autowired <br>
 * date: 2022/5/9 18:35 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.FIELD, ElementType.PARAMETER, ElementType.METHOD, ElementType.CONSTRUCTOR})
public @interface Autowired {
    String value() default "";
}
