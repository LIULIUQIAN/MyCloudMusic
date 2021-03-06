package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.ToastUtil;

import java.io.Serializable;

import butterknife.BindView;
import butterknife.OnClick;

public class RegisterActivity extends BaseLoginActivity {


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

    @BindView(R.id.bt_register)
    Button bt_register;


    /**
     * 第三方登录完成后的用户信息
     */
    private User data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        data = (User) getIntent().getSerializableExtra(Constant.DATA);
        if (data != null && (!TextUtils.isEmpty(data.getQq_id()) || !TextUtils.isEmpty(data.getWeibo_id()))){
            setTitle(R.string.register2);
            et_nickname.setText(data.getNickname());
            bt_register.setText(R.string.complete_register);
        }
    }

    /*
     * 注册按钮
     * */
    @OnClick(R.id.bt_register)
    public void onRegisterClick() {

        String nickname = et_nickname.getText().toString().trim();
        if (TextUtils.isEmpty(nickname)) {
            ToastUtil.errorShortToast(R.string.enter_nickname);
            return;
        }

        if (!StringUtil.isNickname(nickname)) {
            ToastUtil.errorShortToast(R.string.error_nickname_format);
            return;
        }

        String phone = et_phone.getText().toString().trim();
        if (TextUtils.isEmpty(phone)) {
            ToastUtil.errorShortToast(R.string.enter_phone);
            return;
        }

        if (!StringUtil.isPhone(phone)) {
            ToastUtil.errorShortToast(R.string.error_phone_format);
            return;
        }

        String email = et_email.getText().toString().trim();
        if (TextUtils.isEmpty(email)) {
            ToastUtil.errorShortToast(R.string.enter_email);
            return;
        }

        if (!StringUtil.isEmail(email)) {
            ToastUtil.errorShortToast(R.string.error_email_format);
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

        User data = getData();
        data.setNickname(nickname);
        data.setPhone(phone);
        data.setEmail(email);
        data.setPassword(password);

        Api.getInstance()
                .register(data)
                .subscribe(new HttpObserver<DetailResponse<BaseModel>>(getMainActivity(), true) {
                    @Override
                    public void onSucceeded(DetailResponse<BaseModel> data) {
                        ToastUtil.successShortToast("注册成功");

                        login(phone,email,password);
                    }
                });
    }

    public User getData() {
        if (data == null){
            data = new User();
        }
        return data;
    }
}
