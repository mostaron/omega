package com.mostaron.omega.core.beans;

import com.mostaron.omega.core.beans.consts.ScopeEnum;

import java.lang.annotation.Annotation;
import java.util.ArrayList;

/**
 * description: BeanDefinition <br>
 * date: 2022/5/11 14:00 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class BeanDefinition {
    /**
     * 作用域范围
     */
    private ScopeEnum scope;
    /**
     * BeanClass的注解列表
     */
    private ArrayList<Annotation> annotations;
    /**
     * BeanClass的名称
     */
    private String className;
    /**
     * 实际的Class
     */
    private Class<?> clazz;

    /**
     * 是否被托管的Component
     */
    private boolean isComponent;

    public ScopeEnum getScope() {
        return scope;
    }

    public void setScope(ScopeEnum scope) {
        this.scope = scope;
    }

    public ArrayList<Annotation> getAnnotations() {
        return annotations;
    }

    public void setAnnotations(ArrayList<Annotation> annotations) {
        this.annotations = annotations;
    }

    public String getClassName() {
        return className;
    }

    public void setClassName(String className) {
        this.className = className;
    }

    public Class<?> getClazz() {
        return clazz;
    }

    public void setClazz(Class<?> clazz) {
        this.clazz = clazz;
    }

    public boolean isComponent() {
        return isComponent;
    }

    public void setComponent(boolean component) {
        isComponent = component;
    }
}
