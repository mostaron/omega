package com.mostaron.omega.core.exception;

/**
 * description: OmegaCommonException <br>
 * date: 2022/5/9 15:05 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class OmegaCommonException extends RuntimeException{

    public OmegaCommonException(String message) {
        super(message);
    }

    public OmegaCommonException(String template, String...args) {
        super(String.format(template, args));
    }
}
