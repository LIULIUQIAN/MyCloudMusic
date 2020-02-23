package com.example.mycloudmusic;

import android.app.Activity;
import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.multidex.MultiDex;

import com.example.mycloudmusic.activity.LoginOrRegisterActivity;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.event.LoginSuccessEvent;
import com.example.mycloudmusic.manager.impl.ActivityManager;
import com.example.mycloudmusic.util.ORMUtil;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.facebook.stetho.Stetho;
import com.ixuea.android.downloader.DownloadService;
import com.ixuea.android.downloader.callback.DownloadManager;
import com.ixuea.android.downloader.config.Config;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import es.dmoral.toasty.Toasty;
import io.realm.Realm;

public class AppContext extends Application {

    private static AppContext context;

    /**
     * 偏好设置
     */
    private PreferenceUtil sp;
    private DownloadManager downloadManager;
    private ActivityManager activityManager;

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

        //偏好设置工具类
        sp = PreferenceUtil.getInstance(getApplicationContext());

        //初始化第三方toast工具类
        Toasty.Config.getInstance().apply();
        //初始化toast工具类
        ToastUtil.init(getApplicationContext());

        //初始化stetho抓包
        //使用了默认参数初始化
        Stetho.initializeWithDefaults(this);

        //初始化 sharesdk
        MobSDK.init(this);

        //初始化Realm数据库
        Realm.init(context);
    //初始化Activity管理器
        activityManager = ActivityManager.getInstance();

        //注册界面声明周期监听
        activityLifecycleCallbacks();
    }

    /**
     * 获取当前上下文
     *
     * @return
     */
    public static AppContext getInstance() {
        return context;
    }

    /**
     * 当用户登录了
     *
     * @param data
     */
    public void login(Session data) {

        sp.setSession(data.getSession());
        sp.setUserId(data.getUser());

        onLogin();

        //发送登录成功通知
        EventBus.getDefault().post(new LoginSuccessEvent());
    }

    /**
     * 初始化其他需要登录后初始化的内容
     */
    private void onLogin() {

    }

    /*
     * 退出
     * */
    public void logout() {

        sp.logout();

        otherLogout(QQ.NAME);
        otherLogout(SinaWeibo.NAME);

        activityManager.clear();

        Intent intent = new Intent(getApplicationContext(), LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

        onLogout();

    }

    private void onLogout() {
        ORMUtil.destroy();

        if (downloadManager != null){
            downloadManager.destroy();
            downloadManager = null;
        }
    }

    /**
     * 第三方平台退出
     */
    private void otherLogout(String name) {
        Platform platform = ShareSDK.getPlatform(name);
        if (platform.isAuthValid()) {
            platform.removeAccount(true);
        }
    }

    /**
     * 获取下载管理器
     */
    public DownloadManager getDownloadManager() {

        if (downloadManager == null) {

            PreferenceUtil sp = PreferenceUtil.getInstance(getApplicationContext());
            Config config = new Config();
            config.setDatabaseName(String.format("%s_download_info.db", sp.getUserId()));

            downloadManager = DownloadService.getDownloadManager(getApplicationContext(), config);

        }

        return downloadManager;

    }
    /**
     * 注册界面声明周期监听
     */
    public void activityLifecycleCallbacks(){

       registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
           @Override
           public void onActivityCreated(@NonNull Activity activity, @Nullable Bundle savedInstanceState) {
               activityManager.add(activity);
           }

           @Override
           public void onActivityStarted(@NonNull Activity activity) {

           }

           @Override
           public void onActivityResumed(@NonNull Activity activity) {

           }

           @Override
           public void onActivityPaused(@NonNull Activity activity) {

           }

           @Override
           public void onActivityStopped(@NonNull Activity activity) {

           }

           @Override
           public void onActivitySaveInstanceState(@NonNull Activity activity, @NonNull Bundle outState) {

           }

           @Override
           public void onActivityDestroyed(@NonNull Activity activity) {
               activityManager.remove(activity);
           }
       });
    }
}
