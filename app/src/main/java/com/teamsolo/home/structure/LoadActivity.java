package com.teamsolo.home.structure;

import android.Manifest;
import android.animation.ArgbEvaluator;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v13.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.graphics.Palette;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;
import android.widget.ImageView;
import android.widget.TextView;

import com.melody.base.template.activity.HandlerActivity;
import com.melody.base.util.BuildUtility;
import com.melody.base.util.FileManager;
import com.teamsolo.home.R;
import com.teamsolo.home.constant.PreferenceConst;

import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * description: load page
 * author: Melody
 * date: 2016/8/10
 * version: 0.0.0.1
 * <p>
 * 加载页，应用准备过程中显示一个默认的图标，直到activity被加载
 * activity加载后，从本地sd卡加载先前下载的启动图，若先前未下载或该图不存在，则显示一张默认的启动图
 * 随后检查并获取若干必需权限（如sd卡读取权限），若权限获取失败，提示并关闭应用
 * 最后尝试自动登录，并根据登录结果完成跳转（登录成功跳转主页，否则跳转登录页）
 */
public class LoadActivity extends HandlerActivity {

    private static final int PERMISSION_CHECK_REQUEST_CODE = 126;

    /**
     * cover imageView
     */
    private ImageView mCoverImage;

    /**
     * title textView, message textView
     */
    private TextView mTitleText, mMessageText;

    /**
     * load cover image file name
     */
    private String imageFileName;

    /**
     * permissions required
     */
    private String[] requiredPermissions = new String[]{
            Manifest.permission.WRITE_EXTERNAL_STORAGE,
            Manifest.permission.CALL_PHONE
    };

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        setTheme(R.style.AppTheme);
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_load);

        getBundle(getIntent());
        initViews();
        bindListeners();

        /**
         * start to do some preparations
         */
        handler.sendEmptyMessage(0);
    }

    @Override
    protected void getBundle(@NotNull Intent intent) {

    }

    @Override
    protected void initViews() {
        mCoverImage = (ImageView) findViewById(R.id.cover);
        mTitleText = (TextView) findViewById(R.id.title);
        mMessageText = (TextView) findViewById(R.id.message);
    }

    @Override
    protected void bindListeners() {
        mCoverImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO:
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra("url", "http://www.tara-china.cn/Introduce_Members/TARA_JIYEON/JIYEON_Wallpaper/2016/05/23/1453492858.html");
                intent.putExtra("title", "测试");
                startActivity(intent);
            }
        });
    }

    @Override
    protected void handleMessage(HandlerActivity activity, Message msg) {
        if (msg == null) return;

        switch (msg.what) {
            case 0:
                // load cover image
                mMessageText.setText(R.string.load_load_cover);
                loadCoverImage();
                break;

            case 1:
                // check permissions
                mMessageText.setText(R.string.load_check_permission);
                checkPermissions();
                break;

            case 2:
                if (msg.obj == null) break;

                // finish if permission denied, auto login else
                if (!(Boolean) msg.obj) {
                    mMessageText.setText(R.string.load_permission_deny);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            finish();
                        }
                    }, 500);
                } else handler.sendEmptyMessage(3);
                break;

            case 3:
                // auto-login attempt
                mMessageText.setText(R.string.load_auto_login);
                attemptLogin();
                break;

            case 4:
                if (msg.obj == null) break;

                // jump to main page or login page base on the result of auto-login attempt
                // TODO:
                if ((Boolean) msg.obj) {
                    mMessageText.setText(R.string.load_jump_success);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO:
                        }
                    }, 500);
                } else {
                    mMessageText.setText(R.string.load_jump_failure);
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            // TODO:
                        }
                    }, 500);
                }
                break;
        }
    }

    /**
     * load cover image
     * load cover image from sdcard, else load default one from resource
     */
    private void loadCoverImage() {
        imageFileName = PreferenceManager.getDefaultSharedPreferences(mContext)
                .getString(PreferenceConst.LOAD_COVER_IMAGE_URI, "");

        if (!TextUtils.isEmpty(imageFileName)
                && FileManager.fileIsExist(FileManager.CACHE_PATH, imageFileName)
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED)
            mCoverImage.setImageBitmap(BitmapFactory.decodeFile(FileManager.CACHE_PATH + imageFileName));
        else mCoverImage.setImageResource(R.mipmap.load_cover_image_default);

        applyTheme();
        handler.sendEmptyMessage(1);
    }

    /**
     * apply theme base on cover image style
     * only work above Lollipop(Android 5.0)
     */
    private void applyTheme() {
        if (!BuildUtility.isRequired(Build.VERSION_CODES.LOLLIPOP)) return;

        Drawable drawable = mCoverImage.getDrawable();
        if (drawable != null && drawable instanceof BitmapDrawable) {
            Bitmap bitmap = ((BitmapDrawable) drawable).getBitmap();

            if (bitmap != null)
                Palette.from(bitmap).generate(new Palette.PaletteAsyncListener() {
                    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                    @Override
                    public void onGenerated(Palette palette) {
                        Palette.Swatch mutedSwatch = palette.getMutedSwatch();
                        Palette.Swatch darkMutedSwatch = palette.getDarkMutedSwatch();
                        ArgbEvaluator evaluator = new ArgbEvaluator();

                        if (mutedSwatch != null) {
                            Window window = getWindow();

                            if (window != null) {
                                ObjectAnimator animator = ObjectAnimator
                                        .ofInt(window, "statusBarColor", window.getStatusBarColor(), mutedSwatch.getRgb())
                                        .setDuration(700);
                                animator.setEvaluator(evaluator);
                                animator.start();
                            }
                        }

                        if (darkMutedSwatch != null) {
                            ObjectAnimator animator = ObjectAnimator
                                    .ofInt(mTitleText, "textColor", mTitleText.getCurrentTextColor(), darkMutedSwatch.getRgb())
                                    .setDuration(700);
                            animator.setEvaluator(evaluator);
                            animator.start();
                        }
                    }
                });
        }
    }

    /**
     * check several common permissions
     */
    private void checkPermissions() {
        List<String> temp = new ArrayList<>();
        for (String requiredPermission :
                requiredPermissions)
            if (ContextCompat.checkSelfPermission(mContext, requiredPermission) != PackageManager.PERMISSION_GRANTED)
                temp.add(requiredPermission);

        if (!temp.isEmpty())
            ActivityCompat.requestPermissions(this, temp.toArray(new String[0]), PERMISSION_CHECK_REQUEST_CODE);
        else Message.obtain(handler, 2, true).sendToTarget();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        boolean success = true;
        for (int result :
                grantResults)
            if (result != PackageManager.PERMISSION_GRANTED) {
                success = false;
                break;
            }

        Message.obtain(handler, 2, success).sendToTarget();
    }

    /**
     * attempt login
     * auto-login, send true to handler if succeed, false else
     */
    private void attemptLogin() {
        // TODO:
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                Message.obtain(handler, 4, true).sendToTarget();
            }
        }, 1000);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        // recycle the image bitmap
        if (!TextUtils.isEmpty(imageFileName)
                && ContextCompat.checkSelfPermission(mContext, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
            Bitmap bitmap = BitmapFactory.decodeFile(FileManager.CACHE_PATH + imageFileName);

            if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
        }

        // recycle the default cover image resource
        Bitmap bitmap = BitmapFactory.decodeResource(getResources(), R.mipmap.load_cover_image_default);
        if (bitmap != null && !bitmap.isRecycled()) bitmap.recycle();
    }
}
