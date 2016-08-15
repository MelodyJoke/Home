package com.teamsolo.home.structure.page;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputEditText;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.Toolbar;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.TextView;

import com.melody.base.template.activity.HandlerActivity;
import com.melody.base.util.BuildUtility;
import com.teamsolo.home.R;
import com.teamsolo.home.bean.User;
import com.teamsolo.home.constant.NetConstant;
import com.teamsolo.home.constant.PreferenceConst;
import com.teamsolo.home.structure.database.UserDbHelper;
import com.teamsolo.home.structure.widget.LoadingView;

import org.jetbrains.annotations.NotNull;

/**
 * description: register page
 * author: Melody
 * date: 2016/8/14
 * version: 0.0.0.1
 */
public class RegisterActivity extends HandlerActivity {

    private LoadingView mLoadingView;

    private TextInputEditText mPhoneEdit, mPasswordEdit, mConfirmEdit;

    private Button mRegisterButton;

    private View mAgreementButton;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getBundle(getIntent());
        initViews();
        bindListeners();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                loadPhone();
            }
        }, 300);
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {

    }

    @Override
    protected void initViews() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
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
        }

        mLoadingView = (LoadingView) findViewById(R.id.loading);

        mPhoneEdit = (TextInputEditText) findViewById(R.id.phone);
        mPasswordEdit = (TextInputEditText) findViewById(R.id.password);
        mConfirmEdit = (TextInputEditText) findViewById(R.id.confirm);

        mRegisterButton = (Button) findViewById(R.id.register);
        mAgreementButton = findViewById(R.id.agreement);

        mLoadingView.setReactView(findViewById(R.id.content));
        mLoadingView.configHint(getString(R.string.collect_info));
    }

    @Override
    protected void bindListeners() {
        mRegisterButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                attemptRegister();
            }
        });

        mConfirmEdit.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int actionId, KeyEvent keyEvent) {
                if (actionId == EditorInfo.IME_ACTION_DONE) attemptRegister();
                return true;
            }
        });

        mAgreementButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                view.setClickable(false);

                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("title", getString(R.string.web_agreement_title));
                intent.putExtra("url", NetConstant.AGREEMENT_PAGE);
                intent.putExtra("canShare", false);
                startActivity(intent);

                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mAgreementButton.setClickable(true);
                    }
                }, 500);
            }
        });
    }

    /**
     * load phone number from sim card
     * not always, can be loaded only while the info is written in the sim card
     */
    @SuppressLint("HardwareIds")
    private void loadPhone() {
        mLoadingView.show(true);

        if (ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_SMS) == PackageManager.PERMISSION_GRANTED
                || ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_PHONE_STATE) == PackageManager.PERMISSION_GRANTED) {
            TelephonyManager manager = (TelephonyManager) mContext.getSystemService(Context.TELEPHONY_SERVICE);
            String phone = manager.getLine1Number();

            if (!TextUtils.isEmpty(phone)) {
                if (phone.length() > 11) phone = phone.substring(phone.length() - 11);
                mPhoneEdit.setText(phone);
            } else toast(R.string.register_load_phone_failed);
        } else toast(R.string.register_load_phone_failed);

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                mLoadingView.dismiss();
            }
        }, 1500);
    }

    /**
     * attempt register
     */
    private void attemptRegister() {
        mRegisterButton.setClickable(false);

        final String phone = mPhoneEdit.getText().toString().trim();
        final String password = mPasswordEdit.getText().toString().trim();
        final String confirm = mConfirmEdit.getText().toString().trim();

        if (!checkPhone(phone) || !checkPassword(password) || !checkConfirm(password, confirm)) {
            mRegisterButton.setClickable(true);
            return;
        }

        mLoadingView.configHint(getString(R.string.register_signing)).show(true);
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                // TODO: http request
                User user = new User(phone, password, "", true);
                UserDbHelper helper = new UserDbHelper(mContext);
                helper.insert(user);
                PreferenceManager.getDefaultSharedPreferences(mContext).edit()
                        .putString(PreferenceConst.LOGIN_PHONE, phone).apply();

                Intent intent = new Intent(mContext, LoginActivity.class);
                startActivity(intent);
                finish();
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

    /**
     * check confirm
     *
     * @param password password
     * @param confirm  confirm password
     * @return true if password equals confirm
     */
    private boolean checkConfirm(String password, String confirm) {
        if (TextUtils.equals(password, confirm)) return true;
        else {
            mConfirmEdit.setError(getString(R.string.register_error_confirm));
            mConfirmEdit.requestFocus();
            return false;
        }
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {

    }

    @Override
    public void toast(int msgRes) {
        Snackbar.make(mPhoneEdit, msgRes, Snackbar.LENGTH_INDEFINITE).show();
    }

    @Override
    public void toast(String message) {
        Snackbar.make(mPhoneEdit, message, Snackbar.LENGTH_INDEFINITE).show();
    }
}
