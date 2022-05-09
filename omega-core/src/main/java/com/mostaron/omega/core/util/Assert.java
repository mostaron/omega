package com.mostaron.omega.core.util;

/**
 * description: Assert <br>
 * date: 2022/5/7 12:24 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class Assert {
    public static void notNull(Object[] array, String message) {
        if(null == array || array.length == 0) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void notNull(Object obj, String message) {
        if(null == obj) {
            throw new IllegalArgumentException(message);
        }
    }
}
