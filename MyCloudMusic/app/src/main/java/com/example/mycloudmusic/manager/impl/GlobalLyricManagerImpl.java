package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.graphics.Color;
import android.graphics.PixelFormat;
import android.os.Build;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.WindowManager;
import android.widget.TextView;

import com.example.mycloudmusic.manager.GlobalLyricManager;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.example.mycloudmusic.view.GlobalLyricView;

public class GlobalLyricManagerImpl implements GlobalLyricManager {

    private static GlobalLyricManagerImpl instance;
    private final Context context;
    private WindowManager windowManager;
    private WindowManager.LayoutParams layoutParams;
    private GlobalLyricView globalLyricView;
    private final PreferenceUtil sp;

    private GlobalLyricManagerImpl(Context context) {
        this.context = context.getApplicationContext();

        initWindowManager();


        sp = PreferenceUtil.getInstance(this.context);
    }


    public static GlobalLyricManagerImpl getInstance(Context context) {

        if (instance == null) {
            instance = new GlobalLyricManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void show() {

        initGlobalLyricView();

        sp.setShowGlobalLyric(true);
    }

    @Override
    public void hide() {

        windowManager.removeView(globalLyricView);
        globalLyricView = null;

        sp.setShowGlobalLyric(false);

    }

    @Override
    public boolean isShowing() {
        return globalLyricView != null;
    }

    @Override
    public void tryHide() {

    }

    @Override
    public void tryShow() {

    }

    /*
     * 初始化窗口管理器
     * */
    private void initWindowManager() {

        if (windowManager == null) {
            windowManager = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);

            layoutParams = new WindowManager.LayoutParams();

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP_MR1) {
                layoutParams.type = WindowManager.LayoutParams.TYPE_APPLICATION_OVERLAY;
            } else {
                layoutParams.type = WindowManager.LayoutParams.TYPE_SYSTEM_ALERT;
            }

            layoutParams.format = PixelFormat.RGB_888;
            layoutParams.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;

            DisplayMetrics dm = new DisplayMetrics();
            windowManager.getDefaultDisplay().getMetrics(dm);
            layoutParams.width = dm.widthPixels;
            layoutParams.height = WindowManager.LayoutParams.WRAP_CONTENT;
            layoutParams.y = 100;

        }


    }

    /*
     * 创建全局歌词View
     * */
    private void initGlobalLyricView() {


        if (globalLyricView == null){
            globalLyricView = new GlobalLyricView(context);
        }

        if (globalLyricView.getParent() == null){
            windowManager.addView(globalLyricView,layoutParams);
        }

    }
}
