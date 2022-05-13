package com.mostaron.omega.demo.event.listener;

import com.mostaron.omega.demo.event.event.DemoEvent;

import java.util.Date;

/**
 * description: DemoEventListener <br>
 * date: 2022/5/13 14:55 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class DemoEventListener extends OmegaEventListener<DemoEvent>{
    @Override
    public void onEvent(DemoEvent event) {
        System.out.println(new Date(event.getTimestamp())+ "," + event.getSource().toString());
    }
}
