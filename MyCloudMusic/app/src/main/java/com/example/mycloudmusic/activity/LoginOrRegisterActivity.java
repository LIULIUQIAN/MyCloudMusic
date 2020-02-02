package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.event.LoginSuccessEvent;
import com.example.mycloudmusic.util.Constant;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import butterknife.OnClick;

public class LoginOrRegisterActivity extends BaseCommonActivity {


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
    public void onLoginClick(){
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegisterClick(){
        startActivity(RegisterActivity.class);
    }

    /*
    * 登录成功事件
    * */
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onLoginSuccessEvent(LoginSuccessEvent event){
        finish();
    }
}
