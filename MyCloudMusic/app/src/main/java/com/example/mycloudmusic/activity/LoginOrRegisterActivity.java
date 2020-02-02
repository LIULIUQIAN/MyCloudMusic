package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.event.LoginSuccessEvent;
import com.example.mycloudmusic.util.Constant;
import com.mob.commons.SHARESDK;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.HashMap;

import butterknife.OnClick;
import cn.sharesdk.framework.Platform;
import cn.sharesdk.framework.PlatformActionListener;
import cn.sharesdk.framework.PlatformDb;
import cn.sharesdk.framework.ShareSDK;
import cn.sharesdk.sina.weibo.SinaWeibo;
import cn.sharesdk.tencent.qq.QQ;
import cn.sharesdk.wechat.friends.Wechat;

public class LoginOrRegisterActivity extends BaseCommonActivity {


    /**
     * 第三方登录后用户信息
     */
    private User data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        EventBus.getDefault().register(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        lightStatusBar();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @OnClick(R.id.bt_login)
    public void onLoginClick() {
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegisterClick() {
        data = null;
        toRegister();
    }

    @OnClick(R.id.iv_wechat)
    public void onWechatClick() {
        otherLogin(Wechat.NAME);
    }

    @OnClick(R.id.iv_qq)
    public void onQqClick() {
//        otherLogin(QQ.NAME);
//TODO 模拟三方登录
        data = new User();
        data.setNickname("qq模拟登录");
        data.setAvatar("http://a4.att.hudong.com/21/09/01200000026352136359091694357.jpg");
        data.setQq_id("qq-test-id");
        toRegister();
    }

    @OnClick(R.id.iv_weibo)
    public void onWeiboClick() {
        otherLogin(SinaWeibo.NAME);
    }

    /**
     * 跳转到注册界面
     */
    private void toRegister() {
        Intent intent = new Intent(this, RegisterActivity.class);
        if (data != null) {
            intent.putExtra(Constant.DATA, data);
        }
        startActivity(intent);
    }

    /*
     * 第三方登录
     * */
    public void otherLogin(String name) {
        Platform platform = ShareSDK.getPlatform(name);

        platform.SSOSetting(false);

        platform.setPlatformActionListener(new PlatformActionListener() {
            @Override
            public void onComplete(Platform platform, int i, HashMap<String, Object> hashMap) {

                //从数据库获取信息
                //也可以通过user参数获取
                PlatformDb db = platform.getDb();

                String nickname = db.getUserName();
                String avatar = db.getUserIcon();
                String openId = db.getUserId();
                System.out.println("other login success:" + nickname + "," + avatar + "," + openId);

            }

            @Override
            public void onError(Platform platform, int i, Throwable throwable) {
                System.out.println("other login error:" + throwable.getLocalizedMessage());
            }

            @Override
            public void onCancel(Platform platform, int i) {
                System.out.println("other login cancel:" + i);
            }
        });

        platform.showUser(null);
    }

    /*
     * 登录成功事件
     * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event) {
        finish();
    }
}
