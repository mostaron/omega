# 2022-05-13 Spring的事件处理

事件处理基于观察者模式。

Spring定义了Root级的事件ApplicationEvent，所有Spring框架的Event实现，都要基于这个抽象类。
```java
public abstract class ApplicationEvent extends EventObject
```

这个抽象类基于Java的EventObject，包含了一个Object对象source，该对象被transient关键字修饰，表示在进行序列化后，内容丢失。
ApplicationEvent在此之上增加了timestamp字段，用以标记事件发生的时间戳。

此时实现事件的监听有以下几种方法
1. 直接向context上下文注册ApplicationListener接口的实现；
2. 使用@EventListener注解标记事件处理方法；
3. 将实现ApplicationListener接口的实现类注册为Bean，Spring会在初始化结束后进行判断并注册为事件监听器。

使用基于AbstractApplicationContext的publish方法进行消息发送时，Spring源代码如下
```java

protected void publishEvent(Object event, ResolvableType eventType) {
    Assert.notNull(event, "Event must not be null");
    if (logger.isTraceEnabled()) {
        logger.trace("Publishing event in " + getDisplayName() + ": " + event);
    }

    // Decorate event as an ApplicationEvent if necessary
    // 判断消息是否为Spring的ApplicationEvent消息，若不是，则构建PayloadApplicationEvent来处理自定义消息
    ApplicationEvent applicationEvent;
    if (event instanceof ApplicationEvent) {
        applicationEvent = (ApplicationEvent) event;
    }
    else {
        applicationEvent = new PayloadApplicationEvent<Object>(this, event);
        if (eventType == null) {
            eventType = ((PayloadApplicationEvent)applicationEvent).getResolvableType();
        }
    }

    // Multicast right now if possible - or lazily once the multicaster is initialized
    // Spring初始化过程中，会将earlyApplicationEvents初始化，
    // 此集合不为空，表示当前正在进行初始化，缓存当前事件消息，至multicaster初始化结束后再发布
    if (this.earlyApplicationEvents != null) {
        this.earlyApplicationEvents.add(applicationEvent);
    }
    else {
        getApplicationEventMulticaster().multicastEvent(applicationEvent, eventType);
    }

    // Publish event via parent context as well...
    // 尝试向父级Context发布事件
    if (this.parent != null) {
        if (this.parent instanceof AbstractApplicationContext) {
            ((AbstractApplicationContext) this.parent).publishEvent(event, eventType);
        }
        else {
            this.parent.publishEvent(event);
        }
    }
}

```
Spring默认使用SimpleApplicationEventMulticaster进行消息的广播发布，相关的事件广播代码如下：
```java

public void multicastEvent(final ApplicationEvent event, ResolvableType eventType) {
    ResolvableType type = (eventType != null ? eventType : resolveDefaultEventType(event));
    for (final ApplicationListener<?> listener : getApplicationListeners(event, type)) {
        Executor executor = getTaskExecutor();
        if (executor != null) {
            executor.execute(new Runnable() {
                @Override
                public void run() {
                    invokeListener(listener, event);
                }
            });
        }
        else {
            invokeListener(listener, event);
        }
    }
}

```
根据eventType，从预先注册的Listener中查找符合的监听器。
循环遍历进行事件消息的通知，若当前广播支持异步，则通过Executor进行异步处理，否则同步执行listener中定义的方法。