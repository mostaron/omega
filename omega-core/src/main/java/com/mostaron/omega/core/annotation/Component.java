package com.mostaron.omega.core.annotation;

import java.lang.annotation.*;

/**
 * description: ApplicationStart <br>
 * date: 2022/5/7 10:46 <br>
 * author: neil <br>
 * version: 0.1 <br>
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    String value() default "";
}
