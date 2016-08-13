package com.teamsolo.home.structure.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
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

    private static final int PERMISSION_CHECK_REQUEST_CODE = 127;

    private SwipeRefreshLayout mSwipeRefreshLayout;

    private WebView mWebView;

    private ProgressBar mProgressBar;

    private String phone;

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
        try {
            // call js function
            if (url.startsWith("javascript:")) mWebView.loadUrl(url);
                // finish this page
            else if (TextUtils.equals("http://app:finish", url)) {
                if (BuildUtility.isRequired(Build.VERSION_CODES.LOLLIPOP)) finishAfterTransition();
                else finish();
            }
            // back to last page
            else if (TextUtils.equals("http://app:back", url)) onBackPressed();
                // refresh this page
            else if (TextUtils.equals("http://app:refresh", url) && !mSwipeRefreshLayout.isRefreshing()) {
                mSwipeRefreshLayout.setRefreshing(true);
                onRefresh();
            }
            // jump to login page
            else if (TextUtils.equals("http://app:login", url)) {
                startActivity(new Intent(mContext, LoginActivity.class));
                finish();
            }
            // start to share
            else if (url.startsWith("http://app:share")) {
                String shareUrl = Uri.parse(url).getQueryParameter("url");
                String shareTitle = Uri.parse(url).getQueryParameter("title");

                if (TextUtils.isEmpty(shareUrl)) shareUrl = mWebView.getUrl();
                if (TextUtils.isEmpty(shareTitle)) shareTitle = title;

                share(shareUrl, shareTitle);
            }
            // start to call phone
            else if (url.startsWith("http://app:service")) {
                String hint = Uri.parse(url).getQueryParameter("hint");
                phone = Uri.parse(url).getQueryParameter("phone");

                alert(hint, phone, "ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int buttonId) {
                        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                            if (BuildUtility.isRequired(Build.VERSION_CODES.M))
                                requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CHECK_REQUEST_CODE);
                            else
                                mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
                        else
                            mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
                    }
                });
            }
            // show share icon in this page if canShare=1, hide else
            else if (TextUtils.equals("1", Uri.parse(url).getQueryParameter("canShare"))) {
                // TODO: share biz
                toast("this page can be shared.");
                mWebView.loadUrl(url);
            }
            // common load
            else mWebView.loadUrl(url);
        } catch (Exception e) {
            toast(R.string.web_invalid_url);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CHECK_REQUEST_CODE
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + phone)));
        else toast(R.string.permission_deny);
    }

    protected void share(String shareUrl, String shareTitle) {
        // TODO:
    }

    @Override
    public void toast(int msgRes) {
        Snackbar.make(mProgressBar, msgRes, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void toast(String message) {
        Snackbar.make(mProgressBar, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
