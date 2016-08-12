package com.teamsolo.home.bean;

import android.os.Parcel;

import com.melody.base.bean.Bean;

import org.jetbrains.annotations.Contract;

/**
 * description:
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public class User extends Bean {

    public String phone;

    public String password;

    public String portrait;

    public boolean rememberPassword;

    public User() {

    }

    /**
     * constructor
     *
     * @param phone            phone number
     * @param password         password
     * @param portrait         portrait url
     * @param rememberPassword if remember password
     */
    public User(String phone, String password, String portrait, boolean rememberPassword) {
        this.phone = phone;
        this.password = password;
        this.portrait = portrait;
        this.rememberPassword = rememberPassword;
    }

    private User(Parcel in) {
        this.phone = in.readString();
        this.password = in.readString();
        this.portrait = in.readString();
        this.rememberPassword = in.readByte() == 1;
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Contract("_ -> !null")
        @Override
        public User createFromParcel(Parcel parcel) {
            return new User(parcel);
        }

        @Contract(value = "_ -> !null", pure = true)
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(phone);
        dest.writeString(password);
        dest.writeString(portrait);
        dest.writeByte(rememberPassword ? (byte) 1 : 0);
    }
}
