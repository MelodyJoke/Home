package com.melody.base.template.application;

import android.os.Process;

import com.melody.base.util.FileManager;
import com.melody.base.util.LogUtility;

import java.io.PrintWriter;

/**
 * description: base uncaught exception handler
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 * <p/>
 * 0.0.0.1: base uncaught exception handler.
 */
@SuppressWarnings("WeakerAccess")
public abstract class UncaughtExceptionHandler implements Thread.UncaughtExceptionHandler {

    @Override
    public void uncaughtException(Thread thread, Throwable ex) {
        PrintWriter writer = FileManager.getLogPrintWriter();
        if (writer != null) {
            ex.printStackTrace(writer);
            writer.flush();
            writer.close();
        }

        ex.printStackTrace();

        LogUtility.e("UEHandler", "thread: id=" + thread.getId() +
                " name=" + thread.getName() +
                " state=" + thread.getState().name() +
                "\n" + ex.toString());

        subPerform();
        Process.killProcess(Process.myPid());
    }

    /**
     * sub perform after catch exception
     */
    protected abstract void subPerform();
}
