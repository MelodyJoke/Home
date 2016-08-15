package com.teamsolo.home.application;

import com.facebook.drawee.backends.pipeline.Fresco;
import com.melody.base.template.application.BaseApplication;
import com.melody.base.template.application.UncaughtExceptionHandler;

/**
 * description: application
 * author: Melody
 * date: 2016/8/10
 * version: 0.0.0.1
 */
public class HomeApplication extends BaseApplication {

    @Override
    public void onCreate() {
        super.onCreate();

        Fresco.initialize(this);
    }

    @Override
    public UncaughtExceptionHandler initUncaughtExceptionHandler() {
        return new com.teamsolo.home.application.UncaughtExceptionHandler();
    }
}
