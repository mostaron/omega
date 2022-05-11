package com.mostaron.omega.core.annotation;

import com.mostaron.omega.core.beans.consts.ScopeEnum;

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
    @ApplyTo(Component.class)
    String value() default "";

    @ApplyTo(Component.class)
    ScopeEnum scope() default ScopeEnum.SINGLETON;
}
