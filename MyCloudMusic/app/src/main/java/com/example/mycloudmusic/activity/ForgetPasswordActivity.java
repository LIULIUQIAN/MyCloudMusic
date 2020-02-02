package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycloudmusic.R;

import butterknife.BindView;
import butterknife.OnClick;

public class ForgetPasswordActivity extends BaseLoginActivity {

    @BindView(R.id.et_username)
    EditText et_username;

    @BindView(R.id.bt_send_code)
    Button bt_send_code;

    @BindView(R.id.et_code)
    EditText et_code;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    @OnClick(R.id.bt_send_code)
    public void onSendCodeClick() {
        System.out.println("onSendCodeClick");
    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick() {
        System.out.println("onForgetPasswordClick");
    }
}
