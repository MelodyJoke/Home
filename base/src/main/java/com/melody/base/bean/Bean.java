package com.melody.base.bean;

import android.os.Parcelable;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

/**
 * description: base bean
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 * <p>
 * 0.0.0.1: provide an abstract super class of bean which implement {@link Parcelable} interface.
 * <p>
 * Parcelable interface: provide a better performance while serializing in ram, do not implement
 * this interface while serializing in persistent memory.
 *
 * @see Response
 */
@SuppressWarnings("WeakerAccess")
public abstract class Bean implements Parcelable {

    /**
     * for {@link #toString()} to get string from a bean
     */
    protected static final Gson GSON = new GsonBuilder().create();

    public Bean() {
    }

    @Override
    public String toString() {
        return GSON.toJson(this);
    }

    @Override
    public boolean equals(Object o) {
        return o instanceof Bean && toString().equals(o.toString());
    }
}
