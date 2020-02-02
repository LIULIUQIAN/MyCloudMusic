package com.example.mycloudmusic.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.ObserverAdapter;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;

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
    public void onLoginClick() {

        String userName = et_username.getText().toString().trim();
        String password = et_password.getText().toString().trim();

        if (StringUtils.isEmpty(userName)) {
            ToastUtil.errorShortToast(R.string.enter_username);
            return;
        }

        if (!(StringUtil.isPhone(userName) || StringUtil.isEmail(userName))) {
            ToastUtil.errorShortToast(R.string.error_username_format);
            return;
        }

        if (StringUtils.isEmpty(password)) {
            ToastUtil.errorShortToast(R.string.enter_password);
            return;
        }

        if (!StringUtil.isPassword(password)) {
            ToastUtil.errorShortToast(R.string.error_password_format);
            return;
        }


        User user = new User();
        user.setEmail(userName);
        user.setPhone(userName);
        user.setPassword(password);

        Api.getInstance()
                .login(user)
                .subscribe(new HttpObserver<DetailResponse<Session>>(getMainActivity(), true) {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {

                        AppContext.getInstance().login(data.getData());
                        ToastUtil.successShortToast(R.string.login_success);

                        //跳转到主界面
                        startActivityAfterFinishThis(MainActivity.class);
                    }
                });

    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick() {


    }

}
