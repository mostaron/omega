package com.mostaron.omega.demo;

import com.mostaron.omega.demo.event.event.DemoEvent;
import com.mostaron.omega.demo.event.listener.DemoEventListener;
import com.mostaron.omega.demo.event.multicaster.Multicaster;

/**
 * description: EventTest <br>
 * date: 2022/5/13 16:41 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class EventTest {
    public static void main(String...args) {
        Multicaster multicaster = new Multicaster();
        DemoEventListener listener = new DemoEventListener();
        DemoEventListener listener2 = new DemoEventListener();

        multicaster.registerListener(listener);
        multicaster.registerListener(listener2);

        multicaster.publishEvent(new DemoEvent("hello"));
    }
}
