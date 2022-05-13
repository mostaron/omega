package com.mostaron.omega.demo.event.listener;

import com.mostaron.omega.demo.event.event.OmegaEvent;

/**
 * description: OmegaEventListener <br>
 * date: 2022/5/13 14:23 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public abstract class OmegaEventListener<E extends OmegaEvent> {

    public abstract void onEvent(E event);
}
