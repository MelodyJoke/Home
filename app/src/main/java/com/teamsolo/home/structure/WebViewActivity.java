package com.teamsolo.home.structure;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.View;
import android.webkit.WebChromeClient;
import android.webkit.WebResourceRequest;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ProgressBar;

import com.melody.base.template.activity.BaseActivity;
import com.melody.base.util.BuildUtility;
import com.teamsolo.home.R;

import org.jetbrains.annotations.NotNull;

/**
 * description: webPage link page with a progressBar
 * author: Melody
 * date: 2016/8/11
 * version: 0.0.0.1
 */
@SuppressWarnings("unused")
public class WebViewActivity extends BaseActivity implements SwipeRefreshLayout.OnRefreshListener {

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private WebView mWebView;

    private ProgressBar mProgressBar;

    /**
     * title to show on toolbar, show app name if it is empty
     */
    private String title;

    private String url;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_webview);

        getBundle(getIntent());
        initViews();
        bindListeners();

        loadUrl(url);
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {
        title = intent.getStringExtra("title");
        url = intent.getStringExtra("url");
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mWebView.scrollTo(0, 0);
            }
        });
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BuildUtility.isRequired(Build.VERSION_CODES.LOLLIPOP)) finishAfterTransition();
                else finish();
            }
        });
        ActionBar actionBar = getSupportActionBar();
        if (actionBar != null) {
            actionBar.setDisplayShowHomeEnabled(true);
            actionBar.setDisplayHomeAsUpEnabled(true);
            actionBar.setTitle(TextUtils.isEmpty(title) ? getString(R.string.app_name) : title);
        }

        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.swipe);
        mSwipeRefreshLayout.setColorSchemeColors(
                Color.parseColor("#F44336"),
                Color.parseColor("#FF5722"),
                Color.parseColor("#CDDC39"),
                Color.parseColor("#4CAF50"));

        mWebView = (WebView) findViewById(R.id.webView);
        initWebView();

        mProgressBar = (ProgressBar) findViewById(R.id.progress);
        mProgressBar.setProgress(0);
        mProgressBar.setSecondaryProgress(0);
    }

    /**
     * init webView settings
     */
    @SuppressLint("SetJavaScriptEnabled")
    protected void initWebView() {
        WebSettings settings = mWebView.getSettings();
        settings.setAllowFileAccess(true);
        settings.setJavaScriptEnabled(true);
        settings.setAppCacheEnabled(true);
        settings.setDomStorageEnabled(true);
        settings.setDatabaseEnabled(true);

        mWebView.setWebViewClient(initWebViewClient());
        mWebView.setWebChromeClient(initWebChromeClient());
    }

    protected WebViewClient initWebViewClient() {
        return new WebViewClient() {
            @Override
            public boolean shouldOverrideUrlLoading(WebView view, WebResourceRequest request) {
                loadUrl(view.getUrl());
                return true;
            }
        };
    }

    protected WebChromeClient initWebChromeClient() {
        return new WebChromeClient() {
            @Override
            public void onProgressChanged(WebView view, int newProgress) {
                super.onProgressChanged(view, newProgress);
                onSubOperate(view, newProgress);
            }
        };
    }

    @Override
    protected void bindListeners() {
        mSwipeRefreshLayout.setOnRefreshListener(this);
    }

    /**
     * load url
     *
     * @param url the webPage url
     */
    protected void loadUrl(String url) {
        if (mWebView == null) return;

        if (TextUtils.isEmpty(url)) {
            Snackbar.make(mProgressBar, R.string.web_empty_url, Snackbar.LENGTH_INDEFINITE)
                    .setAction(R.string.web_back, new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onBackPressed();
                        }
                    })
                    .show();
            return;
        }

        filterUrl(url);
    }

    @Override
    public void onBackPressed() {
        if (mWebView.canGoBack()) mWebView.goBack();
        else if (BuildUtility.isRequired(Build.VERSION_CODES.LOLLIPOP)) finishAfterTransition();
        else super.onBackPressed();
    }

    @Override
    public void onRefresh() {
        mWebView.reload();
    }

    /**
     * define sub operations
     *
     * @param webView     the webView
     * @param newProgress new progress
     */
    protected void onSubOperate(WebView webView, int newProgress) {
        if (mSwipeRefreshLayout == null || mProgressBar == null) return;

        if (newProgress >= 100 && mSwipeRefreshLayout.isRefreshing())
            mSwipeRefreshLayout.setRefreshing(false);

        mProgressBar.setProgress(newProgress);
        mProgressBar.setSecondaryProgress(newProgress + 5 >= 100 ? 100 : (newProgress + 5));

        if (newProgress >= 100 && mProgressBar.getVisibility() == View.VISIBLE)
            mProgressBar.setVisibility(View.GONE);

        if (newProgress < 100 && mProgressBar.getVisibility() != View.VISIBLE)
            mProgressBar.setVisibility(View.VISIBLE);
    }

    /**
     * filter url
     *
     * @param url the url
     */
    protected void filterUrl(String url) {
        if (TextUtils.equals("app:finish", url)) {
            if (BuildUtility.isRequired(Build.VERSION_CODES.LOLLIPOP)) finishAfterTransition();
            else finish();
        } else if (TextUtils.equals("app:back", url)) onBackPressed();
        else if (TextUtils.equals("app:refresh", url) && !mSwipeRefreshLayout.isRefreshing()) {
            mSwipeRefreshLayout.setRefreshing(true);
            onRefresh();
        }
        else if (TextUtils.equals("app:login", url)) {
            // TODO:
        }
        else mWebView.loadUrl(url);
    }
}
