package com.example.mycloudmusic.activity;

import android.os.Bundle;
import android.widget.EditText;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Sheet;
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

        ToastUtil.successShortToast(R.string.login_success);

    }

    @OnClick(R.id.bt_forget_password)
    public void onForgetPasswordClick() {

        System.out.println("==============" + et_password.getText().toString().trim());
        testApi();

    }

    private void testApi() {

//        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();
//
//        Retrofit retrofit = new Retrofit.Builder()
//                .client(okHttpClientBuilder.build())
//                .baseUrl(Constant.ENDPOINT)
//                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
//                .addConverterFactory(GsonConverterFactory.create())
//                .build();
//
//        Service service = retrofit.create(Service.class);
//        service.sheetDetail("1")
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<DetailResponse>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(DetailResponse s) {
//                        System.out.println("请求成功====" + s.getData().getTitle());
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        e.printStackTrace();
//                        System.out.println("请求失败====" + e.getLocalizedMessage());
//                    }
//
//                    @Override
//                    public void onComplete() {
//
//                    }
//                });


        Api.getInstance()
                .sheets()
                .subscribe(new ObserverAdapter<ListResponse<Sheet>>() {
                    @Override
                    public void onNext(ListResponse<Sheet> sheetListResponse) {
                        super.onNext(sheetListResponse);
                        System.out.println("======" + sheetListResponse.getData().size());
                    }
                });

//        Api.getInstance()
//                .sheetDetail("1")
//                .subscribe(new ObserverAdapter<DetailResponse<Sheet>>() {
//                    @Override
//                    public void onNext(DetailResponse<Sheet> sheetDetailResponse) {
//                        super.onNext(sheetDetailResponse);
//                        System.out.println("请求成功====" + sheetDetailResponse.getData().getTitle());
//                    }
//                });

        Api.getInstance()
                .sheetDetail("-11111")
                .subscribe(new HttpObserver<DetailResponse<Sheet>>(getMainActivity(),true) {
                    @Override
                    public void onSucceeded(DetailResponse<Sheet> data) {
                        System.out.println("请求成功====" + data.getData().getTitle());
                    }


                });

    }
}
