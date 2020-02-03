package com.example.mycloudmusic;

import android.app.Application;
import android.content.Context;
import android.content.Intent;

import androidx.multidex.MultiDex;

import com.example.mycloudmusic.activity.LoginOrRegisterActivity;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.event.LoginSuccessEvent;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.example.mycloudmusic.util.ToastUtil;
import com.facebook.stetho.Stetho;
import com.mob.MobSDK;

import org.greenrobot.eventbus.EventBus;

import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import es.dmoral.toasty.Toasty;

public class AppContext extends Application {

    private static AppContext context;

    /**
     * 偏好设置
     */
    private PreferenceUtil sp;

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

        Intent intent = new Intent(getApplicationContext(), LoginOrRegisterActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);

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
}
