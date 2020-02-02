package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseTitleActivity {


    @BindView(R.id.et_nickname)
    EditText et_nickname;

    @BindView(R.id.et_phone)
    EditText et_phone;

    @BindView(R.id.et_email)
    EditText et_email;

    @BindView(R.id.et_password)
    EditText et_password;

    @BindView(R.id.et_confirm_password)
    EditText et_confirm_password;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    /*
    * 注册按钮
    * */
    @OnClick(R.id.bt_register)
    public void onRegisterClick(){

        String nickname = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)){
            ToastUtil.errorShortToast(R.string.enter_nickname);
            return;
        }

        if (!StringUtil.isNickname(nickname)){
            ToastUtil.errorShortToast(R.string.error_nickname_format);
            return;
        }

        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)){
            ToastUtil.errorShortToast(R.string.enter_phone);
            return;
        }

        if (!StringUtil.isPhone(phone)){
            ToastUtil.errorShortToast(R.string.error_phone_format);
            return;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)){
            ToastUtil.errorShortToast(R.string.enter_email);
            return;
        }

        if (!StringUtil.isEmail(email)){
            ToastUtil.errorShortToast(R.string.error_email_format);
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)){
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        if (!StringUtil.isPassword(password)){
            ToastUtil.errorShortToast(R.string.error_password_format);
            return;
        }

        String confirm_password = et_confirm_password.getText().toString().trim();
        if (TextUtils.isEmpty(confirm_password)){
            ToastUtil.errorShortToast(R.string.enter_confirm_password);
            return;
        }

        if (!password.equals(confirm_password)){
            ToastUtil.errorShortToast(R.string.error_confirm_password);
            return;
        }

        ToastUtil.successShortToast("注册成功");
    }
}
