# 2022-05-07 ~ 2022-05-09

- 初始化项目，构建omega父工程及omega-core模块。

- 编写第一个注解@Component，此注解参照Spring的@Component注解，通过此注解告知omega容器该类为待托管的类

- 编写基于Java包路径的类扫描逻辑

    - 基于ClassLoader进行当前JVM上下文指定basePackage的URL扫描
    - 根据jar和file两种类型，遍历其包及子包路径下的Class文件
    - 载入Class文件
    - 过滤未使用@Component注解的Class