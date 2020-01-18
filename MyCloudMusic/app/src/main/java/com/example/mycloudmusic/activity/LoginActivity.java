package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

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

        String userName = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (StringUtils.isEmpty(userName)){
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        if (StringUtils.isEmpty(password)){
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        ToastUtil.successShortToast(R.string.login_success);

    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick(){

        System.out.println("=============="+et_password.getText().toString().trim());

    }
}
