package com.mostaron.omega.core.beans.factory.exceptions;

import com.mostaron.omega.core.exception.OmegaCommonException;

/**
 * description: TypeNotMatchException <br>
 * date: 2022/5/30:030 14:29:28 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class TypeNotMatchException extends OmegaCommonException {

    public TypeNotMatchException(String message) {
        super(message);
    }

    public TypeNotMatchException(String template, String... args) {
        super(template, args);
    }
}
