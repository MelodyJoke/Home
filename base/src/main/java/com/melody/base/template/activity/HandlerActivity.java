package com.melody.base.template.activity;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * description: a base activity with a static handler to prevent memory leak
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public abstract class HandlerActivity extends BaseActivity {

    /**
     * the handler
     */
    protected StaticHandler handler = new StaticHandler(new WeakReference<>(this));

    /**
     * the static handler with weak references to prevent leak
     */
    protected static class StaticHandler extends Handler {

        /**
         * the weak reference of the outer class instance
         */
        private WeakReference<HandlerActivity> handlerActivityReference;

        /**
         * constructor
         *
         * @param handlerActivityReference the weak reference of the outer class instance
         */
        public StaticHandler(WeakReference<HandlerActivity> handlerActivityReference) {
            this.handlerActivityReference = handlerActivityReference;
        }

        /**
         * get the outer class instance
         *
         * @return the outer class instance
         */
        @Nullable
        private HandlerActivity getHandlerActivity() {
            if (handlerActivityReference != null) return handlerActivityReference.get();

            return null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            HandlerActivity activity = getHandlerActivity();
            if (activity != null) activity.handleMessage(activity, msg);
        }
    }

    /**
     * to handle message from the static handler
     *
     * @param msg message
     */
    protected abstract void handleMessage(HandlerActivity activity, Message msg);
}
