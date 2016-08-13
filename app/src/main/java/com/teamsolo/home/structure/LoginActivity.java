package com.teamsolo.home.structure;

import android.Manifest;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;

import com.facebook.drawee.view.SimpleDraweeView;
import com.melody.base.template.activity.HandlerActivity;
import com.melody.base.util.BuildUtility;
import com.melody.base.util.DisplayUtility;
import com.teamsolo.home.R;
import com.teamsolo.home.bean.User;
import com.teamsolo.home.constant.NetConstant;
import com.teamsolo.home.constant.PreferenceConst;
import com.teamsolo.home.structure.database.UserDbHelper;
import com.teamsolo.home.structure.widget.LoadingView;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * description: login page
 * author: Melody
 * date: 2016/8/12
 * version: 0.0.0.1
 */
public class LoginActivity extends HandlerActivity {

    private static final int PERMISSION_CHECK_REQUEST_CODE = 128;

    private LoadingView mLoadingView;

    private SimpleDraweeView mPortraitImage;

    private AutoCompleteTextView mPhoneEdit;

    private TextInputEditText mPasswordEdit;

    private Button mLoginButton, mRegisterButton;

    private View mSkipButton, mServiceButton, mHelpButton;

    private ArrayAdapter<String> mPhoneEditAdapter;

    private UserDbHelper helper;

    private List<String> phones = new ArrayList<>();

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
        helper = new UserDbHelper(mContext);
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

        mPhoneEditAdapter = new ArrayAdapter<>(mContext, android.R.layout.simple_dropdown_item_1line, phones);

        mLoadingView.setReactView(findViewById(R.id.content));
        mLoadingView.configHint(getString(R.string.login_signing));
        mLoadingView.show(true);
        mPhoneEdit.setText(DisplayUtility.showString(phone, ""));
        mPhoneEdit.setAdapter(mPhoneEditAdapter);
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

        mPhoneEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (start + count == 11 && checkPhone(s.toString())) {
                    User user = helper.getUser(s.toString());
                    if (user != null) {
                        if (user.rememberPassword) mPasswordEdit.setText(user.password);
                        if (!TextUtils.isEmpty(user.portrait))
                            mPortraitImage.setImageURI(Uri.parse(user.portrait));
                    }
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

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
                alert(getString(R.string.login_call_for_service), getString(R.string.service_phone),
                        "ok", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int buttonId) {
                                if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) != PackageManager.PERMISSION_GRANTED)
                                    if (BuildUtility.isRequired(Build.VERSION_CODES.M))
                                        requestPermissions(new String[]{Manifest.permission.CALL_PHONE}, PERMISSION_CHECK_REQUEST_CODE);
                                    else
                                        mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.service_phone))));
                                else
                                    mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.service_phone))));
                            }
                        });
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
        if (!phones.isEmpty()) phones.clear();
        phones.addAll(helper.getPhones(5));
        mPhoneEditAdapter.notifyDataSetChanged();

        if (TextUtils.isEmpty(phone)) {
            mLoadingView.dismiss();
            return;
        }

        mPhoneEdit.setText(phone);
        User user = helper.getUser(phone);

        if (user != null) {
            if (user.rememberPassword) mPasswordEdit.setText(user.password);
            if (!TextUtils.isEmpty(user.portrait))
                mPortraitImage.setImageURI(Uri.parse(user.portrait));
        }

        mLoadingView.dismiss();
    }

    /**
     * attempt login
     */
    private void attemptLogin() {
        mLoginButton.setClickable(false);
        mLoadingView.show(true);

        final String phone = mPhoneEdit.getText().toString().trim();
        final String password = mPasswordEdit.getText().toString().trim();

        if (!checkPhone(phone) || !checkPassword(password)) {
            mLoginButton.setClickable(true);
            mLoadingView.dismiss();
            return;
        }

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                User user = new User(phone, password, "", true);
                helper.insert(user);

                PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                        .putString(PreferenceConst.LOGIN_PHONE, phone).apply();

                mLoginButton.setClickable(true);
                mLoadingView.dismiss();
            }
        }, 1500);
    }

    /**
     * check phone number
     *
     * @param phone phone number
     * @return true if phone number is valid
     */
    private boolean checkPhone(String phone) {
        if (!TextUtils.isEmpty(phone) && phone.length() == 11
                && (phone.startsWith("13") || phone.startsWith("14") || phone.startsWith("15") || phone.startsWith("18")))
            return true;
        else {
            mPhoneEdit.setError(getString(R.string.login_error_phone));
            mPhoneEdit.requestFocus();
            return false;
        }
    }

    /**
     * check password
     *
     * @param password password
     * @return true if password is valid
     */
    private boolean checkPassword(String password) {
        if (!TextUtils.isEmpty(password) && password.length() >= 6) return true;
        else {
            mPasswordEdit.setError(getString(R.string.login_error_password));
            mPasswordEdit.requestFocus();
            return false;
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == PERMISSION_CHECK_REQUEST_CODE
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED)
            mContext.startActivity(new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + getString(R.string.service_phone))));
        else toast(R.string.permission_deny);
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {

    }

    @Override
    protected void toast(int msgRes) {
        Snackbar.make(mPortraitImage, msgRes, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    protected void toast(String message) {
        Snackbar.make(mPortraitImage, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
