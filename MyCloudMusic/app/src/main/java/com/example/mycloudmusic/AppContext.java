package com.example.mycloudmusic;

import android.app.Application;
import android.content.Context;

import androidx.multidex.MultiDex;

import com.example.mycloudmusic.util.ToastUtil;
import com.facebook.stetho.Stetho;

import es.dmoral.toasty.Toasty;

public class AppContext extends Application {

    private static AppContext context;

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        //初始化MultiDex
        MultiDex.install(this);
    }

    @Override
    public void onCreate() {
        super.onCreate();

        context = this;

        //初始化第三方toast工具类
        Toasty.Config.getInstance().apply();
        //初始化toast工具类
        ToastUtil.init(getApplicationContext());

        //初始化stetho抓包
        //使用了默认参数初始化
        Stetho.initializeWithDefaults(this);

    }

    /**
     * 获取当前上下文
     * @return
     */
    public static AppContext getInstance() {
        return context;
    }
}
