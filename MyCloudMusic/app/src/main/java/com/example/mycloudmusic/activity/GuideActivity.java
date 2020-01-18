package com.example.mycloudmusic.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;

public class GuideActivity extends BaseCommonActivity implements View.OnClickListener {

    private Button bt_login_or_register;
    private Button bt_enter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);

    }

    @Override
    protected void initViews() {
        super.initViews();
        //隐藏状态栏
        hideStatusBar();

        bt_login_or_register = findViewById(R.id.bt_login_or_register);
        bt_enter = findViewById(R.id.bt_enter);
    }

    @Override
    protected void initListeners() {
        super.initListeners();
        bt_login_or_register.setOnClickListener(this);
        bt_enter.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_login_or_register:
                startActivityAfterFinishThis(LoginOrRegisterActivity.class);
                break;
            case R.id.bt_enter:
                startActivityAfterFinishThis(MainActivity.class);
                break;
        }
    }
}
