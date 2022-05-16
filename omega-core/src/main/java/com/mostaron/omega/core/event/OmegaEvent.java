package com.mostaron.omega.core.event;

import java.util.EventObject;

/**
 * description: OmegaEvent <br>
 * date: 2022/5/13 14:14 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public abstract class OmegaEvent extends EventObject {

    private final long timestamp;

    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public OmegaEvent(Object source) {
        super(source);
        this.timestamp = System.currentTimeMillis();
    }

    public long getTimestamp() {
        return timestamp;
    }
}
