package com.melody.base.template.application;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.support.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

/**
 * description: base application extend {@link Application}
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.2
 * <p>
 * 0.0.0.1: base application.
 */
@SuppressWarnings("unused")
public abstract class BaseApplication extends Application {

    /**
     * instance of application
     */
    @SuppressLint("StaticFieldLeak")
    private static BaseApplication instance;

    /**
     * uncaught exception handler
     */
    private UncaughtExceptionHandler uncaughtExceptionHandler;

    /**
     * activity list
     */
    private List<Activity> activities;

    /**
     * activities between the from one and the aim one
     */
    private List<Activity> gapActivities;

    /**
     * the current activity
     */
    private Activity currentActivity;

    @Override
    public void onCreate() {
        super.onCreate();

        instance = this;
        activities = new ArrayList<>();
        gapActivities = new ArrayList<>();

        // set uncaught exception handler
        uncaughtExceptionHandler = initUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(uncaughtExceptionHandler);
    }

    /**
     * init uncaught exception handler
     *
     * @return the uncaught exception handler
     */
    public abstract UncaughtExceptionHandler initUncaughtExceptionHandler();

    /**
     * get instance of application
     *
     * @return the instance
     */
    public static BaseApplication getInstance() {
        return instance;
    }

    /**
     * get application context
     *
     * @return the current application context
     */
    @Nullable
    public static Context getInstanceContext() {
        if (instance == null) return null;
        return instance.getApplicationContext();
    }

    /**
     * register an activity
     *
     * @param activity the new activity
     */
    public void registerActivity(Activity activity) {
        if (activities != null && !activities.contains(activity)) activities.add(activity);
    }

    /**
     * unregister an activity
     *
     * @param activity the activity to remove
     */
    public void unregisterActivity(Activity activity) {
        if (activities != null && activities.contains(activity)) activities.remove(activity);
    }

    /**
     * finish all activities
     */
    public void clearActivities() {
        if (activities != null && !activities.isEmpty()) {
            for (Activity activity : activities) activity.finish();

            activities.clear();
        }
    }

    /**
     * finish all gap activities
     */
    public void clearGapActivities() {
        if (gapActivities != null && !gapActivities.isEmpty()) {
            for (Activity activity : activities) activity.finish();

            gapActivities.clear();
        }
    }

    /**
     * set current activity
     *
     * @param activity the current activity
     */
    public void setCurrentActivity(Activity activity) {
        currentActivity = activity;
    }

    /**
     * get current activity
     *
     * @return the current activity
     */
    public Activity getCurrentActivity() {
        return currentActivity;
    }

    /**
     * get uncaught exception handler
     *
     * @return the uncaught exception handler
     */
    public UncaughtExceptionHandler getUncaughtExceptionHandler() {
        return uncaughtExceptionHandler;
    }
}
