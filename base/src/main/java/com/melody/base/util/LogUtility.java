package com.melody.base.util;

import android.text.TextUtils;
import android.util.Log;

/**
 * description: log util
 * author: Melody
 * date: 2016/6/4
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public final class LogUtility {

    /**
     * eager mode / develop mode
     * print all logs
     */
    public static final int MODE_EAGER = 0, MODE_DEVELOP = 0;

    /**
     * test mode
     * save exception log to file only
     */
    public static final int MODE_TEST = 1;

    /**
     * sluggish mode / release mode
     * print none, show dialog
     */
    public static final int MODE_SLUGGISH = 2, MODE_RELEASE = 2;

    /**
     * current mode
     * check in {@link #MODE_EAGER,#MODE_DEVELOP,#MODE_TEST,#MODE_SLUGGISH,#MODE_RELEASE}
     */
    private static int MODE = MODE_EAGER;

    private LogUtility() {
    }

    /**
     * init log util
     *
     * @param mode check in
     *             {@link #MODE_EAGER,#MODE_DEVELOP,#MODE_TEST,#MODE_SLUGGISH,#MODE_RELEASE},
     *             {@link #MODE_EAGER} otherwise.
     */
    public static void init(int mode) {
        if (mode <= MODE_EAGER || mode > MODE_SLUGGISH) MODE = MODE_EAGER;
        else MODE = mode;
    }

    /**
     * Send a {@link Log#DEBUG} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void d(String tag, String msg) {
        switch (MODE) {
            case MODE_EAGER:
                if (!TextUtils.isEmpty(msg)) Log.d(tag, msg);
                break;

            default:
        }
    }

    /**
     * Send an {@link Log#INFO} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void i(String tag, String msg) {
        switch (MODE) {
            case MODE_EAGER:
                if (!TextUtils.isEmpty(msg)) Log.i(tag, msg);
                break;

            default:
        }
    }

    /**
     * Send a {@link Log#WARN} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void w(String tag, String msg) {
        switch (MODE) {
            case MODE_EAGER:
                if (!TextUtils.isEmpty(msg)) Log.w(tag, msg);
                break;

            default:
        }
    }

    /**
     * Send an {@link Log#ERROR} log message.
     *
     * @param tag Used to identify the source of a log message.  It usually identifies
     *            the class or activity where the log call occurs.
     * @param msg The message you would like logged.
     */
    public static void e(String tag, String msg) {
        switch (MODE) {
            case MODE_EAGER:
                if (!TextUtils.isEmpty(msg)) Log.e(tag, msg);
                break;

            default:
        }
    }

    /**
     * get current mode
     *
     * @return the current mode
     */
    public static int getMode() {
        return MODE;
    }
}
