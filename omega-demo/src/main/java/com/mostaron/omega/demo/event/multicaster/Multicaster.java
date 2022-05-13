package com.mostaron.omega.demo.event.multicaster;

import com.mostaron.omega.demo.event.event.OmegaEvent;
import com.mostaron.omega.demo.event.listener.OmegaEventListener;
import com.mostaron.omega.demo.event.util.GenericTypeUtil;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 * description: Multicaster <br>
 * date: 2022/5/13 14:18 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class Multicaster {
    /**
     * 监听器注册表
     */
    @SuppressWarnings("rawtypes")
    private final Map<Class<? extends OmegaEvent>, Set<OmegaEventListener>> listenerRegistry = new ConcurrentHashMap<>();

    /**
     * 将监听器注册到监听器注册表
     * description: registerListener <br>
     * version: 1.0 <br>
     * date: 2022/5/13 16:45 <br>
     * author: Neil <br>
     *
     * @param listener
     */
    public void registerListener(OmegaEventListener<? extends OmegaEvent> listener) {
        Class<? extends OmegaEvent> classDef = GenericTypeUtil.getTargetClass(listener.getClass(), OmegaEvent.class);
        synchronized (Objects.requireNonNull(classDef)) {
            @SuppressWarnings("rawtypes")
            Set<OmegaEventListener> listeners = listenerRegistry.computeIfAbsent(classDef, k -> new HashSet<>());
            listeners.add(listener);
        }
    }

    /**
     * 广播消息，根据事件类型查找对应的监听器并串行处理
     * @param event
     */
    @SuppressWarnings("rawtypes")
    public <T extends OmegaEvent> void publishEvent(T event) {
        Set<OmegaEventListener> listeners = listenerRegistry.get(event.getClass());
        if(null != listeners && listeners.size() > 0) {
            listeners.forEach(listener -> listener.onEvent(event));
        }
    }


}
