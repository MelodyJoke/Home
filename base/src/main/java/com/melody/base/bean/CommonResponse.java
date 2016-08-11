package com.melody.base.bean;

import android.os.Parcel;

import org.jetbrains.annotations.Contract;

/**
 * description: a common implementation of {@link Response}
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 * <p>
 * 0.0.0.1: the common implementation of {@link Response}, if the response do not contains other
 * data, use this.
 */
@SuppressWarnings("WeakerAccess, unused")
public class CommonResponse extends Response {

    public static final Creator<CommonResponse> CREATOR = new Creator<CommonResponse>() {
        @Contract("_ -> !null")
        @Override
        public CommonResponse createFromParcel(Parcel in) {
            return new CommonResponse(in);
        }

        @Contract(value = "_ -> !null", pure = true)
        @Override
        public CommonResponse[] newArray(int size) {
            return new CommonResponse[size];
        }
    };

    private CommonResponse() {
    }

    private CommonResponse(int code, String message) {
        super(code, message);
    }

    private CommonResponse(Parcel in) {
        this.code = in.readInt();
        this.message = in.readString();
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(code);
        dest.writeString(message);
    }
}
