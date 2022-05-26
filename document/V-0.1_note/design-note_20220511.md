
# 2022-05-11
鉴于之前的残留问题，参考BeanDefinition的实现，编写Bean的定义类，存储Metadata等相关信息。

在初期扫描的过程中，将除java包下的其它Annotation依次添加至一个对应的ArrayList，以备后续使用。
同时还需要考虑多级Annotation的属性合并操作。

编写BeanDefinition对象，保存被托管Class的定义，包含Class名称、Class对象、注解列表等信息。
在遍历判断某个类是否为被托管对象时，同时计算其注解列表并处理。

在计算注解列表时，会逐级获取注解中包含@ApplyTo的属性，并将这些属性写回父级注解中。

## 注解属性合并处理

在递归遍历注解列表时，会生成一个默认的属性池，属性池的数据结构如下：
``` java
 Map<Class<? extends Annotation>, Map<String, Object>> metadataBox
```
该常量池的Key为被```@ApplyTo```注解标过的父级注解，Value为子注解中配置的属性名及值的Map集合。
属性合并遵循以下操作逻辑
1. 判断属性池中是否存在当前注解的属性，若存在则覆盖之。
    ```
    由于在JVM中，注解会被处理为代理类，所以需要通过代理来获取JVM中注解对应的代理对象InvocationHandler，
    在InvocationHandler中，使用了Map对象memberValues保存了原注解类定义的属性及其值。
    所以属性的覆盖，本质上就是对该Map的覆盖。

    JVM中注解的代理InvocationHandler对象最终被sun.reflect.annotation.AnnotationInvocationHandler来实现。
    由于本项目开发时基于JDK17，而在当前版本中，由于模块化的问题，会导致系统没有权限反射至sun.reflect包中，所以需要在启动时添加如下JVM参数：
    --add-opens java.base/sun.reflect.annotation=ALL-UNNAMED

    对于这个问题，后期将考虑将本项目升级为支持模块化。
    ```
2. 遍历当前注解中是否存在使用了```@ApplyTo```注解的属性，若存在则将其合并至属性池。

    1. 遍历当前注解Class包含的Method
    2. 将Method对象包装为MergeInfo对象
    3. 过滤出未使用```@ApplyTo```注解的Method
    4. 判断属性池是是否已经包含```@ApplyTo```注解标识的父注解及其属性的键值对
    5. 在递归的过程中，是由子级逐渐向父级递归，若已存在属性配置，表示在上级时已经处理过，优先级要高于当前注解，故不再处理
    6. 若不存在，同将其合并至属性池

至此，BeanDefinition的初步创建已经完成。

下一步将处理Bean初始化的过程，在未实现AOP之前，将暂时使用二级代理的方式处理，以规避循环依赖的问题。