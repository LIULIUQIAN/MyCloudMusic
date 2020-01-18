package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.mycloudmusic.R;

public class LoginOrRegisterActivity extends BaseCommonActivity implements View.OnClickListener {

    private Button bt_login;
    private Button bt_register;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_or_register);
    }

    @Override
    protected void initViews() {
        super.initViews();
        bt_login = findViewById(R.id.bt_login);
        bt_register = findViewById(R.id.bt_register);
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        bt_login.setOnClickListener(this);
        bt_register.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.bt_login:
                System.out.println("================1111111");
                break;
            case R.id.bt_register:
                System.out.println("================222222");
                break;

        }

    }
}
