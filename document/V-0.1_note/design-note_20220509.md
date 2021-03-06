# 2022-05-09
借鉴Spring框架， 在原有Component注解的基础之上，扩展Service注解。

Spring在处理的时候，使用了注解的注解，即@Service注解使用了@Component注解，
同时在扫描的过程中，对注解的注解进行递归，进而判断该类是否需要由容器管理。

这种方式需要扩展扫描器的判断逻辑，同时需要注意一些问题
1. 需要过滤掉java包自身的几个注解，因为他们存在了相互使用或自身使用。直接递归会导致栈溢出问题，如@Documented代码
  ``` java
  @Documented
  @Retention(RetentionPolicy.RUNTIME)
  @Target(ElementType.ANNOTATION_TYPE)
  public @interface Documented {
  }
  ```
2. 仅过滤java包，仍然会存在潜在风险。
   因为我们的目标是像Spring框架一样，可以做为第三方工具框架被使用，
   所以如果最终用户编写了自定义注解，且这些注解间也存在了互相使用的问题，依然会导致栈溢出。
3. 但如果将注解的扫描范围限在```com.mostaron.core.annotation```，则会严重限制代码的可扩展性。
   且如果用户将自定义注解也写在这个包下同时互相使用，仍然会重复问题2的结果。
4. 基于这些原因，在进行递归的过程中，加入递归深度计数器，暂定递归深度为**5**，超过将不再继续递归，而是直接返回否。

## 残留问题
目前只判断了类是否被@Component注解及其子注解标记，
但使用@Component的子注解并未将其Meta信息传递或标记出来，
这将导致通过Component的子注解筛选出来的对象，无法找到对应的Component注解，并进行相关处理。

Spring使用的方法为建立了一整套的MergedAnnotation处理方案，将多个不同的注解进行了合并，
同时使用MetadataReader进行相关的属性读取处理。

因此需要对isComponent方法再次重构，在判断是否被Component的最终子注解标记的同时，还需要记录该注解，
这样在后续的缓存Bean构建时，才可以根据这些配置信息进行自定义的处理。