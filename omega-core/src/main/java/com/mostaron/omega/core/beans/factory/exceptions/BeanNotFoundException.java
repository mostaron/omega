package com.mostaron.omega.core.beans.factory.exceptions;

import com.mostaron.omega.core.exception.OmegaCommonException;

/**
 * description: BeanNotFoundException <br>
 * date: 2022/5/30:030 14:28:37 <br>
 * author: Neil <br>
 * version: 0.1 <br>
 */
public class BeanNotFoundException extends OmegaCommonException {
    public BeanNotFoundException(String message) {
        super(message);
    }

    public BeanNotFoundException(String template, String... args) {
        super(template, args);
    }
}
