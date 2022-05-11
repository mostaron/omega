package com.mostaron.omega.core.util;

import com.mostaron.omega.core.annotation.ApplyTo;
import com.mostaron.omega.core.exception.OmegaCommonException;
import com.mostaron.omega.core.metadata.MergeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

/**
 * 处理属性合并操作的工具类。
 * 本工具处理两种情况：
 * 待合并属性池中存在当前注解的配置时，使用属性池中的属性覆盖当前注解中的属性；
 * 当前注解的属性中包含{@link ApplyTo}的注解时，将这些属性合并至待合并属性池。
 * 待合并属性池的数据结构如下：
 * Map<Class<? extends Annotation>, Map<String, Object>>
 * 其中Key为通过{@link ApplyTo}标识的父级注解，value为属性名与属性值的键值对。<br/>
 * <p>
 * 从待合并属性池向本注解合并时，会使用覆盖策略，从而保证原始的配置会替换当前的配置配置；
 * 将本注解中属性合并至属性池时，若属性池中对应的父级注解已存在对应的属性及值，则保留原配置，不再进行合并操作。
 * <p>
 * description: AnnotationMergeUtil <br>
 * date: 2022/5/11 15:17 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class AnnotationMergeUtil {

    private static final Logger logger = LoggerFactory.getLogger(AnnotationMergeUtil.class);

    /**
     * 将src注解中的属性以及preMetadata中的属性合并至desc，同时将src注解中使用 {@link com.mostaron.omega.core.annotation.ApplyTo}
     * 注解的属性合并至preMetadata
     *
     * @param dest
     * @param metadataBox
     * @return
     */
    public static <DEST extends Annotation>
    void merge(Map<Class<? extends Annotation>, Map<String, Object>> metadataBox, DEST dest) {

        Assert.notNull(metadataBox, "metadataBox cannot be null");

        // 若预处理metadata中包含了desc的class，表示在之前的处理中已经找到需要被合并的信息
        // 需要执行合并操作
        if (metadataBox.containsKey(dest.annotationType())) {
            mergeMetadataToAnnotation(metadataBox.get(dest.annotationType()), dest);
        }

        // 将当前注解中包含ApplyTo注解的属性合并至待合并属性池
        mergeAnnotationToMetadata(dest, metadataBox);

    }

    /**
     * 将预生成的待合并Metadata合并至对应的注解中
     * description: mergeMetadataToAnnotation <br>
     * version: 1.0 <br>
     * date: 2022/5/11 16:18 <br>
     * author: Neil <br>
     *
     * @param metadata
     * @param dest
     * @return void
     */
    private static <DEST extends Annotation> void mergeMetadataToAnnotation(Map<String, Object> metadata, DEST dest) {

        metadata.keySet().stream().forEach(key -> {
            mergeProperty(key, metadata.get(key), dest);
        });
    }

    /**
     * 根据反射原理获取注解的代理类，并将属性合并至目标注解中
     * description: mergeProperty <br>
     * version: 1.0 <br>
     * date: 2022/5/11 16:17 <br>
     * author: Neil <br>
     *
     * @param propertyName
     * @param value
     * @param dest
     * @return void
     */
    private static <DEST extends Annotation> void mergeProperty(String propertyName, Object value, DEST dest) {
        //获取代理处理器
        InvocationHandler invocationHandler = Proxy.getInvocationHandler(dest);
        try {
            Field field = invocationHandler.getClass().getDeclaredField("memberValues");
            field.setAccessible(true);

            @SuppressWarnings("unchecked")
            Map<String, Object> memberValues = (Map<String, Object>) field.get(invocationHandler);

            memberValues.put(propertyName, value);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            logger.error("Error while merge property[{}] to Annotation[{}]",
                    propertyName, dest.annotationType().getName(), e);

            throw new OmegaCommonException("Error while merge property[{}] to Annotation[{}]",
                    propertyName, dest.annotationType().getName());
        }
    }

    /**
     * 将注解中使用了{@link ApplyTo}注解的属性，合并至待合并属性池
     *
     * @param src
     * @param metadataBox
     * @param <SRC>
     */
    private static <SRC extends Annotation> void
    mergeAnnotationToMetadata(SRC src, Map<Class<? extends Annotation>, Map<String, Object>> metadataBox) {

        Arrays.stream(src.annotationType().getDeclaredMethods())
                //将Method对象转换成MergeInfo对象
                .map(MergeInfo::buildFromMethod)
                //若当前method未使用ApplyTo注解，则被过滤出
                .filter(MergeInfo::applyToIsNotNull)
                //将属性池中对应当前注解的配置取出
                .map(mergeInfo -> dealMetadata(mergeInfo, metadataBox))
                //若属性池不为空，则包含当前属性，则不再执行后续合并操作
                .filter(MergeInfo::isNeedMergeToMetadata)
                //将当前属性合并至属性池
                .forEach(mergeInfo -> mergePropertyToMetadata(mergeInfo, src));
    }

    /**
     * 完善当前合并信息，将待合并对象的metadata进行初始化
     *
     * @param mergeInfo
     * @param metadataBox
     * @return
     */
    private static MergeInfo dealMetadata(MergeInfo mergeInfo,
                                          Map<Class<? extends Annotation>, Map<String, Object>> metadataBox) {
        Map<String, Object> metadata = metadataBox.
                computeIfAbsent(mergeInfo.getApplyTo().value(), k -> new HashMap<>());

        mergeInfo.setMetadata(metadata);

        return mergeInfo;
    }

    /**
     * 将属性合并至待合并信息
     *
     * @param mergeInfo
     * @param src
     * @param <SRC>
     */
    private static <SRC extends Annotation> void mergePropertyToMetadata(MergeInfo mergeInfo, SRC src) {

        try {
            Method method = mergeInfo.getMethod();
            Object value = method.invoke(src);

            mergeInfo.getMetadata().put(method.getName(), value);
        } catch (IllegalAccessException | InvocationTargetException e) {
            logger.error("Error while merge property[{}] from [{}] to metadata",
                    mergeInfo.getMethod().getName(), src.annotationType().getName(), e);
            throw new OmegaCommonException("Error while merge property[{}] from [{}] to metadata",
                    mergeInfo.getMethod().getName(), src.annotationType().getName());
        }

    }

}
