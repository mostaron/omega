package com.mostaron.omega.core.annotation;

import com.mostaron.omega.core.beans.consts.ScopeEnum;

import java.lang.annotation.*;

/**
 * description: ApplicationStart <br>
 * date: 2022/5/7 10:46 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */

@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Component {
    /**
     * name
     * @return
     */
    String value() default "";

    /**
     * 作用范围
     * @return
     */
    ScopeEnum scope() default ScopeEnum.SINGLETON;
}
