package com.teamsolo.home.structure;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AutoCompleteTextView;
import android.widget.Button;

import com.facebook.drawee.view.SimpleDraweeView;
import com.melody.base.template.activity.HandlerActivity;
import com.melody.base.util.DisplayUtility;
import com.teamsolo.home.R;
import com.teamsolo.home.constant.NetConstant;
import com.teamsolo.home.constant.PreferenceConst;
import com.teamsolo.home.structure.widget.LoadingView;

import org.jetbrains.annotations.NotNull;

/**
 * description: login page
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
public class LoginActivity extends HandlerActivity {

    private LoadingView mLoadingView;

    private View mContentView;

    private SimpleDraweeView mPortraitImage;

    private AutoCompleteTextView mPhoneEdit;

    private TextInputEditText mPasswordEdit;

    private Button mLoginButton, mRegisterButton;

    private View mSkipButton, mServiceButton, mHelpButton;

    private String phone;

    private boolean loadPassword;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getBundle(getIntent());
        initViews();
        bindListeners();

        loadPassword();
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {
        phone = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceConst.LOGIN_PHONE, "");
        loadPassword = PreferenceManager.getDefaultSharedPreferences(mContext).getBoolean(PreferenceConst.LOGIN_PASSWORD_REMEMBER, false);
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadingView = (LoadingView) findViewById(R.id.loading);
        mContentView = findViewById(R.id.content);

        mPortraitImage = (SimpleDraweeView) findViewById(R.id.portrait);
        mPhoneEdit = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordEdit = (TextInputEditText) findViewById(R.id.password);

        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.register);
        mSkipButton = findViewById(R.id.skip);
        mServiceButton = findViewById(R.id.service);
        mHelpButton = findViewById(R.id.help);

        mContentView.setVisibility(View.GONE);
        mLoadingView.show();

        mPhoneEdit.setText(DisplayUtility.showString(phone, ""));
    }

    @Override
    protected void bindListeners() {
        mLoginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptLogin();
            }
        });

        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        mSkipButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        mServiceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
            }
        });

        mHelpButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("title", getString(R.string.login_help));
                intent.putExtra("url", NetConstant.HELP_CENTER);
                startActivity(intent);
            }
        });
    }

    /**
     * load password from db if {@link #loadPassword} is true
     */
    private void loadPassword() {
        if (loadPassword) {
            // TODO:
        } else {
            mLoadingView.dismiss();
            mContentView.setVisibility(View.VISIBLE);
        }
    }

    /**
     * attempt login
     */
    private void attemptLogin() {
        // TODO:
        mLoginButton.setClickable(false);
        mContentView.setVisibility(View.GONE);
        mLoadingView.show();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoginButton.setClickable(true);
                mLoadingView.dismiss();
                mContentView.setVisibility(View.VISIBLE);
            }
        }, 1500);
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {

    }
}
