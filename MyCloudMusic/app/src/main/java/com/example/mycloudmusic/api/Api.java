package com.example.mycloudmusic.api;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.BuildConfig;
import com.example.mycloudmusic.domain.Advert;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.BaseResponse;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.util.Constant;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;


public class Api {

    private static Api instance;

    private final Service service;

    public static Api getInstance() {
        if (instance == null) {
            instance = new Api();
        }
        return instance;
    }

    public Api() {

        OkHttpClient.Builder okHttpClientBuilder = new OkHttpClient.Builder();

        if (BuildConfig.DEBUG) {
            //创建okhttp日志拦截器
            HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor();
            loggingInterceptor.level(HttpLoggingInterceptor.Level.BODY);
            okHttpClientBuilder.addInterceptor(loggingInterceptor);

            //添加stetho抓包拦截器
            okHttpClientBuilder.addNetworkInterceptor(new StethoInterceptor());

            //添加chucker实现应用内显示网络请求信息拦截器
            okHttpClientBuilder.addInterceptor(new ChuckerInterceptor(AppContext.getInstance()));

        }

        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClientBuilder.build())
                .baseUrl(Constant.ENDPOINT)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        service = retrofit.create(Service.class);
    }

    public Observable<ListResponse<Sheet>> sheets() {
        return service.sheets()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    public Observable<DetailResponse<Sheet>> sheetDetail(String id) {
        return service.sheetDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 注册
     */

    public Observable<DetailResponse<BaseModel>> register(User data) {
        return service.register(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /*
     * 登录
     * */
    public Observable<DetailResponse<Session>> login(User data) {
        return service.login(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 重置密码
     */
    public Observable<BaseResponse> resetPassword(User data) {
        return service.resetPassword(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发送短信验证码
     */
    public Observable<DetailResponse<BaseModel>> sendSMSCode(User data) {
        return service.sendSMSCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }
    /**
     * 发送邮箱验证码
     */
    public Observable<DetailResponse<BaseModel>> sendEmailCode(User data) {
        return service.sendEmailCode(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用户详情
     */
    public Observable<DetailResponse<User>> userDetail(String id, String nickname) {

        //添加查询参数
        HashMap<String, String> data = new HashMap<>();

        if (StringUtils.isNotBlank(nickname)) {
            data.put(Constant.NICKNAME, nickname);
        }
        return service.userDetail(id,data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用户详情
     */
    public Observable<DetailResponse<User>> userDetail(String id) {
        return userDetail(id,null);
    }

    /**
     * 单曲
     */
    public Observable<ListResponse<Song>> songs() {
        return service.songs()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 广告列表
     */
    public Observable<ListResponse<Advert>> adverts() {
        return service.adverts()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}
