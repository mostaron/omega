# Welcome To Omega

作为Java平台最常用的框架，Spring的重要性已经得到Java社区的绝对认同。
随着这些年的发展，Spring也不再只是最初的IOC和AOP框架，而是逐渐演进为微服务平台的整体解决方案。

对于Java开发人员来讲，了解Spring的工作原理及其代码实现，成为一个合格Java工程师的必备素质。
众多大厂及伪大厂都要求对于Spring源代码有足够的学习经历。
然而漫无目的的浏览Spring代码，会在大量的代码中迷失自己，从而失去继续深入研究的兴趣。

子曾经曰过，学而不思则惘。

本项目旨在通过实现一套支持IOC及AOP的简易组件，然后参照Spring全家桶，逐步迭代其功能，进而辅助理解Spring框架的源码及设计逻辑。

## Spring学习笔记

[Spring的AbstractApplicationContext.refresh都做了什么](document/learn_note/20220512_spring_context_refresh.md)

[Spring的事件处理模型](document/learn_note/20220513_spring_event.md)

[Spring中的Autowire装配及Bean的生成过程](document/learn_note/20220524_spring_beanCreation.md)

## 计划演进路线

- 0.1 支持简单通过路径扫描，初始化并缓存被注册托管的Bean，提供工厂方法获取被托管的Bean，提供类似SpringBoot的启动器

  [0.1版开发笔记](document/V-0.1_release_note.md)
- 0.2 基于代理，实现简单的AOP，增加三级缓存
- 0.3 完善IOC及AOP功能，并扩展作用域管理等
- 0.4 增加AutoConfiguration等配置功能
- 0.5 加入Tomcat、Jetty及Undertow的Web支持，开始实现MVC功能
- ………………

