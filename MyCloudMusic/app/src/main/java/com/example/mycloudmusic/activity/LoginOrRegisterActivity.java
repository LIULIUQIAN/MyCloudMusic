package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

import butterknife.OnClick;

public class LoginOrRegisterActivity extends BaseCommonActivity {


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    @Override
    protected void initViews() {
        super.initViews();

        lightStatusBar();
    }


    @OnClick(R.id.bt_login)
    public void onLoginClick(){
        startActivity(LoginActivity.class);
    }

    @OnClick(R.id.bt_register)
    public void onRegisterClick(){
        startActivity(RegisterActivity.class);
    }
}
