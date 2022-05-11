package com.mostaron.omega.core.annotation;

import java.lang.annotation.*;

/**
 * description: ApplyTo <br>
 * date: 2022/5/11 15:04 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.METHOD)
public @interface ApplyTo {
    Class<? extends Annotation> value();
}
