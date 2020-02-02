package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.BaseResponse;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

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

        String userName = et_username.getText().toString().trim();
        if (StringUtils.isEmpty(userName)) {
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        if (!(StringUtil.isPhone(userName) || StringUtil.isEmail(userName))) {
            ToastUtil.errorShortToast(R.string.error_username_format);
            return;
        }

        String code = et_code.getText().toString().trim();
        if (TextUtils.isEmpty(code)) {
            ToastUtil.errorShortToast(R.string.enter_code);
            return;
        }

        String password = et_password.getText().toString().trim();
        if (TextUtils.isEmpty(password)) {
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        if (!StringUtil.isPassword(password)) {
            ToastUtil.errorShortToast(R.string.error_password_format);
            return;
        }

        String confirm_password = et_confirm_password.getText().toString().trim();
        if (TextUtils.isEmpty(confirm_password)) {
            ToastUtil.errorShortToast(R.string.enter_confirm_password);
            return;
        }

        if (!password.equals(confirm_password)) {
            ToastUtil.errorShortToast(R.string.error_confirm_password);
            return;
        }

        User data = new User();
        if (StringUtil.isPhone(userName)) {
            data.setPhone(userName);
        } else {
            data.setEmail(userName);
        }
        data.setCode(code);
        data.setPassword(password);

        Api.getInstance()
                .resetPassword(data)
                .subscribe(new HttpObserver<BaseResponse>() {
                    @Override
                    public void onSucceeded(BaseResponse data) {
                        ToastUtil.successShortToast(R.string.pwd_set_successful);
                        finish();
                    }
                });
    }
}
