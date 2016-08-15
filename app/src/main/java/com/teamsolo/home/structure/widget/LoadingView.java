package com.teamsolo.home.structure.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.teamsolo.home.R;

/**
 * description: loading view
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public class LoadingView extends FrameLayout {

    private Context mContext;

    private ProgressBar mProgressBar;

    private ImageView mHintImage;

    private TextView mHintText;

    private OnReloadListener mOnReloadListener;

    private View mReactView;

    private int status;

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

    private void init() {
        inflate(mContext, R.layout.view_loading, this);
        initViews();
        bindListeners();
    }

    private void initViews() {
        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mHintImage = (ImageView) findViewById(R.id.image);
        mHintText = (TextView) findViewById(R.id.hint);
    }

    private void bindListeners() {
        setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (mOnReloadListener != null && mProgressBar.getVisibility() == VISIBLE)
                    mOnReloadListener.onReload();
            }
        });
    }

    public LoadingView configImage(int imageRes) {
        mHintImage.setImageResource(imageRes);
        return this;
    }

    public LoadingView configHint(String hint) {
        mHintText.setText(hint);
        return this;
    }

    public void dismiss() {
        setVisibility(GONE);
        if (mReactView != null) mReactView.setVisibility(VISIBLE);
    }

    public void hideProgress() {
        mProgressBar.setVisibility(View.INVISIBLE);
    }

    public void show(boolean showProgressBar) {
        mProgressBar.setVisibility(showProgressBar ? VISIBLE : GONE);
        mHintImage.setVisibility(showProgressBar ? GONE : VISIBLE);

        setVisibility(VISIBLE);
        if (mReactView != null) mReactView.setVisibility(GONE);
    }

    public void setReactView(View view) {
        this.mReactView = view;
    }

    public void setOnReloadListener(OnReloadListener listener) {
        this.mOnReloadListener = listener;
    }

    public interface OnReloadListener {
        void onReload();
    }
}
