package com.example.mycloudmusic.api;

import android.text.TextUtils;

import com.chuckerteam.chucker.api.ChuckerInterceptor;
import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.BuildConfig;
import com.example.mycloudmusic.domain.Advert;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Book;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.Order;
import com.example.mycloudmusic.domain.Pay;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Suggest;
import com.example.mycloudmusic.domain.Topic;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.domain.param.OrderParam;
import com.example.mycloudmusic.domain.param.PayParam;
import com.example.mycloudmusic.domain.response.BaseResponse;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.PreferenceUtil;
import com.facebook.stetho.okhttp3.StethoInterceptor;

import org.apache.commons.lang3.StringUtils;

import java.util.HashMap;
import java.util.Map;

import io.reactivex.Observable;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;


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

        //公共请求参数
        okHttpClientBuilder.addNetworkInterceptor(chain -> {
            PreferenceUtil sp = PreferenceUtil.getInstance(AppContext.getInstance());

            Request request = chain.request();
            if (sp.isLogin()) {
                request = request.newBuilder()
                        .addHeader("User", sp.getUserId())
                        .addHeader("Authorization", sp.getSession())
                        .build();
            }
            return chain.proceed(request);

        });

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
        return service.userDetail(id, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 用户详情
     */
    public Observable<DetailResponse<User>> userDetail(String id) {
        return userDetail(id, null);
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
     * 歌曲详情
     */
    public Observable<DetailResponse<Song>> songDetail(String id) {
        return service.songDetail(id)
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

    /**
     * 收藏歌单
     */
    public Observable<Response<Void>> collect(String id) {
        return service.collect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消收藏歌单
     */
    public Observable<Response<Void>> deleteCollect(String id) {
        return service.deleteCollect(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 评论列表
     */
    public Observable<ListResponse<Comment>> comments(Map<String, String> data) {
        return service.comments(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建评论
     */
    public Observable<DetailResponse<Comment>> createComment(Comment data) {
        return service.createComment(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 评论点赞
     */
    public Observable<DetailResponse<BaseModel>> like(String data) {
        return service.like(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消评论点赞
     */
    public Observable<Response<Void>> deleteLike(String id) {
        return service.deleteLike(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 话题列表
     */
    public Observable<ListResponse<Topic>> topics() {
        return service.topics()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 好友列表（我关注的人）
     */
    public Observable<ListResponse<User>> friends(String id) {
        return service.friends(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 粉丝列表（关注我的人）
     */
    public Observable<ListResponse<User>> fans(String id) {

        return service.fans(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());

    }

    /**
     * 获取用户创建的歌单
     */
    public Observable<ListResponse<Sheet>> createSheets(String userId) {
        return service.createSheets(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取用户收藏的歌单
     */
    public Observable<ListResponse<Sheet>> collectSheets(String userId) {

        return service.collectSheets(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建歌单
     */
    public Observable<DetailResponse<Sheet>> createSheet(Sheet data) {
        return service.createSheet(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 将歌曲收藏到歌单
     */
    public Observable<Response<Void>> addSongToSheet(String sheetId, String songId) {

        Map<String, String> data = new HashMap<>();
        data.put("id", songId);

        return service.addSongToSheet(sheetId, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 从歌单中删除音乐
     */
    public Observable<Response<Void>> deleteSongInSheet(String sheetId, String songId) {
        return service.deleteSongInSheet(sheetId, songId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 关注用户
     */
    public Observable<DetailResponse<BaseModel>> follow(String userId) {
        return service.follow(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 取消关注用户
     */
    public Observable<DetailResponse<BaseModel>> deleteFollow(String userId) {
        return service.deleteFollow(userId)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 视频列表
     */
    public Observable<ListResponse<Video>> videos() {
        return service.videos()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 视频详情
     */
    public Observable<DetailResponse<Video>> videoDetail(String id) {
        return service.videoDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 动态列表
     */
    public Observable<ListResponse<Feed>> feeds(String userId) {
        Map<String, String> data = new HashMap<>();

        if (!TextUtils.isEmpty(userId)) {
            data.put("user_id", userId);
        }

        return service.feeds(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 发布动态
     */
    public Observable<DetailResponse<BaseModel>> createFeed(Feed data) {
        return service.createFeed(data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 更新用户
     */
    public Observable<DetailResponse<BaseModel>> updateUser(User data) {
        return service.updateUser(data.getId(), data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 商品列表
     */
    public Observable<ListResponse<Book>> shops() {
        return service.shops()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 商品详情
     */
    public Observable<DetailResponse<Book>> shopDetail(String id) {
        return service.shopDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 创建订单
     */
    public Observable<DetailResponse<BaseModel>> createOrder(OrderParam param) {
        return service.createOrder(param)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 订单详情
     */
    public Observable<DetailResponse<Order>> orderDetail(String id) {
        return service.orderDetail(id)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 获取订单支付参数
     */
    public Observable<DetailResponse<Pay>> orderPay(String id, PayParam data) {
        return service.orderPay(id, data)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索歌单
     */
    public Observable<ListResponse<Sheet>> searchSheets(String data) {
        return service.searchSheets(getSearchParams(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    /**
     * 搜索用户
     */
    public Observable<ListResponse<User>> searchUsers(String data) {
        return service.searchUsers(getSearchParams(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

    private Map<String, String> getSearchParams(String data) {
        Map<String, String> map = new HashMap<>();
        if (!TextUtils.isEmpty(data)) {
            map.put("query", data);
        }
        return map;
    }

    /**
     * 搜索建议
     */
    public Observable<DetailResponse<Suggest>> searchSuggest(String data) {
        return service.searchSuggest(getSearchParams(data))
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }





}
