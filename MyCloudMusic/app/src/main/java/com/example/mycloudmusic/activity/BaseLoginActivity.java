package com.example.mycloudmusic.activity;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;

public class BaseLoginActivity extends BaseTitleActivity {


    /*
     * 登录
     * */
    protected void login(String phone, String email, String password) {

        User user = new User();
        user.setEmail(email);
        user.setPhone(phone);
        user.setPassword(password);

        Api.getInstance()
                .login(user)
                .subscribe(new HttpObserver<DetailResponse<Session>>(getMainActivity(), true) {
                    @Override
                    public void onSucceeded(DetailResponse<Session> data) {

                        AppContext.getInstance().login(data.getData());

                        //跳转到主界面
                        startActivityAfterFinishThis(MainActivity.class);
                    }
                });
    }
}


