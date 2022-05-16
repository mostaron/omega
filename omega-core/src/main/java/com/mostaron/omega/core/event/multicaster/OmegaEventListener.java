package com.mostaron.omega.core.event.multicaster;


import com.mostaron.omega.core.event.OmegaEvent;

/**
 * description: OmegaEventListener <br>
 * date: 2022/5/13 14:23 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public interface OmegaEventListener<E extends OmegaEvent> {

    void onEvent(E event);
}
