package com.mostaron.omega.core.metadata;

import com.mostaron.omega.core.annotation.ApplyTo;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;

/**
 * description: MergeInfo <br>
 * date: 2022/5/11 16:30 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class MergeInfo {
    private ApplyTo applyTo;
    private Map<String, Object> metadata;
    private Method method;

    public ApplyTo getApplyTo() {
        return applyTo;
    }

    public void setApplyTo(ApplyTo applyTo) {
        this.applyTo = applyTo;
    }

    public Map<String, Object> getMetadata() {
        return metadata;
    }

    public void setMetadata(Map<String, Object> metadata) {
        this.metadata = metadata;
    }

    public Method getMethod() {
        return method;
    }

    public void setMethod(Method method) {
        this.method = method;
    }


    public static MergeInfo buildFromMethod(Method method) {
        MergeInfo mergeInfo = new MergeInfo();

        mergeInfo.setApplyTo(method.getAnnotation(ApplyTo.class));
        mergeInfo.setMethod(method);

        return mergeInfo;
    }

    public boolean isNeedMergeToMetadata() {

        return applyToIsNotNull() && !metadataHasValue();
    }

    public boolean applyToIsNotNull() {
        return null != applyTo;
    }

    private boolean metadataHasValue() {
        if(null == metadata) {
            return false;
        }
        return null != metadata.get(method.getName());
    }
}
