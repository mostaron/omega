package com.mostaron.omega.demo.event.event;

/**
 * description: DemoEvent <br>
 * date: 2022/5/13 14:16 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class DemoEvent extends OmegaEvent{
    /**
     * Constructs a prototypical Event.
     *
     * @param source the object on which the Event initially occurred
     * @throws IllegalArgumentException if source is null
     */
    public DemoEvent(Object source) {
        super(source);
    }
}
