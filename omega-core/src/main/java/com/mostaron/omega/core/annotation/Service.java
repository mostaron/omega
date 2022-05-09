package com.mostaron.omega.core.annotation;

import java.lang.annotation.*;

/**
 * description: Service <br>
 * date: 2022/5/9 16:18 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@Component
public @interface Service {
    String value() default "";
}
