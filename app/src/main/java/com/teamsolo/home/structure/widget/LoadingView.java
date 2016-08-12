package com.teamsolo.home.structure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;

import com.teamsolo.home.R;

/**
 * description: loading view
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public class LoadingView extends FrameLayout {

    protected Context mContext;

    public LoadingView(Context context) {
        super(context);
        mContext = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        init();
    }

    public LoadingView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mContext = context;
        init();
    }

    protected void init() {
        inflate(mContext, R.layout.view_loading, this);
    }

    public void dismiss() {
        setVisibility(GONE);
    }

    public void show() {
        // TODO:
        setVisibility(VISIBLE);
    }
}
