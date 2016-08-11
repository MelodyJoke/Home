package com.melody.base.bean;

/**
 * description: base response bean
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 * <p>
 * 0.0.0.1: a base response bean provide two default fields: {@link #code}, {@link #message}
 *
 * @see CommonResponse is the common implementation
 */
@SuppressWarnings("WeakerAccess, unused")
public abstract class Response extends Bean {

    /**
     * response code
     */
    public int code;

    /**
     * response message, usually is the description or cause of {@link #code}
     */
    public String message;

    protected Response() {
    }

    protected Response(int code, String message) {
        this.code = code;
        this.message = message;
    }
}
