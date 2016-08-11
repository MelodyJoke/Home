package com.melody.base.util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;

import org.jetbrains.annotations.Contract;

/**
 * description: build util
 * author: Melody
 * date: 2016/6/18
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public final class BuildUtility {

    private static final String TAG = "BuildUtility";

    private BuildUtility() {
    }

    /**
     * get apk version name
     *
     * @param context context
     * @return the version name
     */
    public static String getApkVersionName(Context context) {
        PackageManager pm = context.getPackageManager();
        PackageInfo info = null;
        String packageName = "";

        try {
            info = pm.getPackageInfo(context.getPackageName(), 0);
        } catch (PackageManager.NameNotFoundException e) {
            LogUtility.e(TAG, e.getMessage());
        }

        if (null != info) packageName = info.versionName;

        return packageName;
    }

    /**
     * check build require sdk version
     *
     * @param requiredVersionCode the version code required,
     *                            check in {@link android.os.Build.VERSION_CODES}
     * @return if is required
     */
    @Contract(pure = true)
    public static boolean isRequired(int requiredVersionCode) {
        return Build.VERSION.SDK_INT >= requiredVersionCode;
    }
}
