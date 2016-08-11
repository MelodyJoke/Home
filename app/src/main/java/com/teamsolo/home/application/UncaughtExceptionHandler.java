package com.teamsolo.home.application;

import android.widget.Toast;

/**
 * description: uncaught exception handler
 * author: Melody
 * date: 2016/8/10
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public class UncaughtExceptionHandler extends
        com.melody.base.template.application.UncaughtExceptionHandler {

    @Override
    protected void subPerform() {
        Toast.makeText(App.getInstanceContext(), "Oh no! Crash...", Toast.LENGTH_LONG).show();
    }
}
