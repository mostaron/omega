package com.mostaron.omega.core.beans;

import com.mostaron.omega.core.annotation.Component;
import com.mostaron.omega.core.beans.consts.ScopeEnum;

import java.lang.annotation.Annotation;
import java.util.ArrayList;
import java.util.Set;

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

    /**
     * 当前Bean初始化所依赖的其它Bean列表
     */
    private Set<String> dependsOn;

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
        //尝试获取Component注解中指定的Scope并设置当前BeanDefinition
        annotations.stream()
                .filter(annotation -> annotation.annotationType() == Component.class)
                .findAny()
                .ifPresent(annotation -> setScope(((Component) annotation).scope()));
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

    /**
     * 获取当前Class是否为托管对象，除满足isComponent以外，过滤注解及枚举类型
     * description: isComponent <br>
     * version: 1.0 <br>
     * date: 2022/5/24:024 16:36:05 <br>
     * author: Neil <br>
     *
     * @param
     * @return boolean
     */
    public boolean isComponent() {
        return isComponent
                && !clazz.isAnnotation()
                && !clazz.isEnum();
    }

    public void setComponent(boolean component) {
        isComponent = component;
    }

    public Set<String> getDependsOn() {
        return dependsOn;
    }

    public void setDependsOn(Set<String> dependsOn) {
        this.dependsOn = dependsOn;
    }
}
