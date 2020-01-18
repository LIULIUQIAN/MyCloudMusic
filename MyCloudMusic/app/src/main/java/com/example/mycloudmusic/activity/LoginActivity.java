package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mycloudmusic.R;

import butterknife.BindView;
import butterknife.OnClick;

public class LoginActivity extends BaseTitleActivity {

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.et_password)
    EditText et_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }

    @OnClick(R.id.bt_login)
    public void onLoginClick(){

        System.out.println("=============="+et_username.getText().toString().trim());

    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick(){

        System.out.println("=============="+et_password.getText().toString().trim());

    }
}
