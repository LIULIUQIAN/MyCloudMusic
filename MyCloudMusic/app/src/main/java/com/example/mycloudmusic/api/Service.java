package com.example.mycloudmusic.api;


import android.graphics.PostProcessor;

import com.example.mycloudmusic.domain.Advert;
import com.example.mycloudmusic.domain.BaseModel;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Topic;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.domain.response.BaseResponse;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.ObserverAdapter;

import java.util.List;
import java.util.Map;

import io.reactivex.Observable;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;

public interface Service {

    /**
     * 歌单列表
     */
    @GET("v1/sheets")
    Observable<ListResponse<Sheet>> sheets();

    /**
     * 歌单详情
     *
     * @param id
     * @return
     */
    @GET("v1/sheets/{id}")
    Observable<DetailResponse<Sheet>> sheetDetail(@Path("id") String id);


    /**
     * 注册
     *
     * @param data
     * @return
     */
    @POST("v1/users")
    Observable<DetailResponse<BaseModel>> register(@Body User data);

    /**
     * 登录
     *
     * @param data
     * @return
     */
    @POST("v1/sessions")
    Observable<DetailResponse<Session>> login(@Body User data);

    /**
     * 重置密码
     */
    @POST("v1/users/reset_password")
    Observable<BaseResponse> resetPassword(@Body User data);

    /**
     * 发送短信验证码
     */
    @POST("v1/codes/request_sms_code")
    Observable<DetailResponse<BaseModel>> sendSMSCode(@Body User data);

    /**
     * 发送邮箱验证码
     */
    @POST("v1/codes/request_email_code")
    Observable<DetailResponse<BaseModel>> sendEmailCode(@Body User data);

    /**
     * 用户详情
     */
    @GET("v1/users/{id}")
    Observable<DetailResponse<User>> userDetail(@Path("id") String id, @QueryMap Map<String, String> data);

    /**
     * 单曲
     */
    @GET("v1/songs")
    Observable<ListResponse<Song>> songs();

    /**
     * 歌曲详情
     */
    @GET("v1/songs/{id}")
    Observable<DetailResponse<Song>> songDetail(@Path("id") String id);

    /**
     * 广告列表
     */
    @GET("v1/ads")
    Observable<ListResponse<Advert>> adverts();

    /**
     * 收藏歌单
     */
    @FormUrlEncoded
    @POST("v1/collections")
    Observable<Response<Void>> collect(@Field("sheet_id") String id);

    /**
     * 取消收藏歌单
     */
    @DELETE("v1/collections/{id}")
    Observable<Response<Void>> deleteCollect(@Path("id") String id);

    /**
     * 评论列表
     */
    @GET("v1/comments")
    Observable<ListResponse<Comment>> comments(@QueryMap Map<String, String> data);

    /**
     * 创建评论
     */
    @POST("v1/comments")
    Observable<DetailResponse<Comment>> createComment(@Body Comment data);

    /**
     * 评论点赞
     */
    @FormUrlEncoded
    @POST("v1/likes")
    Observable<DetailResponse<BaseModel>> like(@Field("comment_id") String data);

    /**
     * 取消评论点赞
     */
    @DELETE("v1/likes/{id}")
    Observable<Response<Void>> deleteLike(@Path("id") String id);

    /**
     * 话题列表
     */
    @GET("v1/topics")
    Observable<ListResponse<Topic>> topics();

    /**
     * 好友列表（我关注的人）
     */
    @GET("v1/users/{id}/following")
    Observable<ListResponse<User>> friends(@Path("id") String id);

    /**
     * 粉丝列表（关注我的人）
     */
    @GET("v1/users/{id}/followers")
    Observable<ListResponse<User>> fans(@Path("id") String id);

    /**
     * 获取用户创建的歌单
     *
     * @param userId
     * @return
     */
    @GET("v1/users/{userId}/create")
    Observable<ListResponse<Sheet>> createSheets(@Path("userId") String userId);

    /**
     * 获取用户收藏的歌单
     *
     * @param userId
     * @return
     */
    @GET("v1/users/{userId}/collect")
    Observable<ListResponse<Sheet>> collectSheets(@Path("userId") String userId);

    /**
     * 创建歌单
     */
    @POST("v1/sheets")
    Observable<DetailResponse<Sheet>> createSheet(@Body Sheet data);

    /**
     * 将歌曲收藏到歌单
     */
    @POST("v1/sheets/{sheetId}/relations")
    Observable<Response<Void>> addSongToSheet(@Path("sheetId") String sheetId, @Body Map<String, String> data);

    /**
     * 从歌单中删除音乐
     */
    @DELETE("v1/sheets/{sheetId}/relations/{songId}")
    Observable<Response<Void>> deleteSongInSheet(@Path("sheetId") String sheetId, @Path("songId") String songId);

    /**
     * 关注用户
     */
    @FormUrlEncoded
    @POST("v1/friends")
    Observable<DetailResponse<BaseModel>> follow(@Field("id") String userId);

    /**
     * 取消关注用户
     */
    @DELETE("v1/friends/{userId}")
    Observable<DetailResponse<BaseModel>> deleteFollow(@Path("userId") String userId);

    /**
     * 视频列表
     */
    @GET("v1/videos")
    Observable<ListResponse<Video>> videos();

    /**
     * 视频详情
     */
    @GET("v1/videos/{id}")
    Observable<DetailResponse<Video>> videoDetail(@Path("id") String id);

    /**
     * 动态列表
     */
    @GET("v1/feeds")
    Observable<ListResponse<Feed>> feeds(@QueryMap Map<String, String> data);

    /**
     * 发布动态
     */
    @POST("v1/feeds")
    Observable<DetailResponse<BaseModel>> createFeed(@Body Feed data);

    /**
     * 更新用户
     */
    @PATCH("v1/users/{id}")
    Observable<DetailResponse<BaseModel>> updateUser(@Path("id") String id, @Body User data);
}
