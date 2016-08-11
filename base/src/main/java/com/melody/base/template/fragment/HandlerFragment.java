package com.melody.base.template.fragment;

import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;

import java.lang.ref.WeakReference;

/**
 * description: a base fragment with a static handler to prevent memory leak
 * author: Melody
 * date: 2016/7/9
 * version: 0.0.0.1
 */
@SuppressWarnings("WeakerAccess, unused")
public abstract class HandlerFragment extends BaseFragment {

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
        private WeakReference<HandlerFragment> handlerFragmentWeakReference;

        /**
         * constructor
         *
         * @param handlerFragmentWeakReference the weak reference of the outer class instance
         */
        public StaticHandler(WeakReference<HandlerFragment> handlerFragmentWeakReference) {
            this.handlerFragmentWeakReference = handlerFragmentWeakReference;
        }

        /**
         * get the outer class instance
         *
         * @return the outer class instance
         */
        @Nullable
        private HandlerFragment getHandlerActivity() {
            if (handlerFragmentWeakReference != null) return handlerFragmentWeakReference.get();

            return null;
        }

        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            HandlerFragment fragment = getHandlerActivity();
            if (fragment != null) fragment.handleMessage(fragment, msg);
        }
    }

    /**
     * to handle message from the static handler
     *
     * @param msg message
     */
    protected abstract void handleMessage(HandlerFragment fragment, Message msg);
}
