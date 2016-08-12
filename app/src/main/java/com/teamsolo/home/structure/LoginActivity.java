package com.teamsolo.home.structure;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.TextInputEditText;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.melody.base.template.activity.HandlerActivity;
import com.melody.base.util.DisplayUtility;
import com.teamsolo.home.R;
import com.teamsolo.home.bean.User;
import com.teamsolo.home.constant.NetConstant;
import com.teamsolo.home.constant.PreferenceConst;
import com.teamsolo.home.structure.database.UserDbHelper;
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

    private SimpleDraweeView mPortraitImage;

    private AutoCompleteTextView mPhoneEdit;

    private TextInputEditText mPasswordEdit;

    private Button mLoginButton, mRegisterButton;

    private View mSkipButton, mServiceButton, mHelpButton;

    private String phone;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        getBundle(getIntent());
        initViews();
        bindListeners();

        loadUserInfo();
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {
        phone = PreferenceManager.getDefaultSharedPreferences(mContext).getString(PreferenceConst.LOGIN_PHONE, "");
    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        mLoadingView = (LoadingView) findViewById(R.id.loading);

        mPortraitImage = (SimpleDraweeView) findViewById(R.id.portrait);
        mPhoneEdit = (AutoCompleteTextView) findViewById(R.id.phone);
        mPasswordEdit = (TextInputEditText) findViewById(R.id.password);

        mLoginButton = (Button) findViewById(R.id.login);
        mRegisterButton = (Button) findViewById(R.id.register);
        mSkipButton = findViewById(R.id.skip);
        mServiceButton = findViewById(R.id.service);
        mHelpButton = findViewById(R.id.help);

        mLoadingView.setReactView(findViewById(R.id.content));
        mLoadingView.configHint(getString(R.string.login_signing));
        mLoadingView.show(true);
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

        mPasswordEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) attemptLogin();
                return true;
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
     * load password from db
     * load portrait
     */
    private void loadUserInfo() {
        if (TextUtils.isEmpty(phone)) {
            mLoadingView.dismiss();
            return;
        }

        mPhoneEdit.setText(phone);

        UserDbHelper helper = new UserDbHelper(mContext);
        User user = helper.getUser(phone);

        if (user.rememberPassword) mPasswordEdit.setText(user.password);
        if (!TextUtils.isEmpty(user.portrait)) mPortraitImage.setImageURI(Uri.parse(user.portrait));

        mLoadingView.dismiss();
    }

    /**
     * attempt login
     */
    private void attemptLogin() {
        mLoginButton.setClickable(false);
        mLoadingView.show(true);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                String phone = mPhoneEdit.getText().toString();
                String password = mPasswordEdit.getText().toString();

                User user = new User(phone, password, "", true);
                UserDbHelper helper = new UserDbHelper(mContext);
                helper.insert(user);

                PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                        .putString(PreferenceConst.LOGIN_PHONE, phone).apply();

                mLoginButton.setClickable(true);
                mLoadingView.dismiss();
            }
        }, 1500);
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {

    }
}
