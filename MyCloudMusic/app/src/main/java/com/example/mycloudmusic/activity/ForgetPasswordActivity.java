package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.os.CountDownTimer;
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

    //倒计时
    private CountDownTimer downTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forget_password);
    }

    @OnClick(R.id.bt_send_code)
    public void onSendCodeClick() {

        String userName = et_username.getText().toString().trim();
        if (StringUtils.isEmpty(userName)) {
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        if (StringUtil.isPhone(userName)) {
            sendSMSCode(userName);
        } else if (StringUtil.isEmail(userName)) {
            sendEmailCode(userName);
        } else {
            ToastUtil.errorShortToast(R.string.error_username_format);
            return;
        }
    }

    /*
     * 发送短信验证码
     * */
    private void sendSMSCode(String value) {
        User data = new User();
        data.setPhone(value);

        Api.getInstance()
                .sendSMSCode(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        startCountDown();
                    }
                });
    }

    /*
     * 发送邮件验证码
     * */
    private void sendEmailCode(String value) {
        User data = new User();
        data.setEmail(value);

        Api.getInstance()
                .sendEmailCode(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>() {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        startCountDown();
                    }
                });
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

    /*
     * 开始倒计时
     * */
    private void startCountDown() {

        downTimer = new CountDownTimer(60000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bt_send_code.setText(getString(R.string.count_second, millisUntilFinished / 1000));
            }

            @Override
            public void onFinish() {
                bt_send_code.setEnabled(true);
                bt_send_code.setText(R.string.send_code);
            }
        };

        downTimer.start();
        bt_send_code.setEnabled(false);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        if (downTimer != null) {
            downTimer.cancel();
        }
    }
}
