package com.example.mycloudmusic.api;


import com.example.mycloudmusic.domain.Session;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;

import java.util.List;

import io.reactivex.Observable;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface Service {

    /**
     * 歌单列表
     *
     * @return
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
     * 登录
     *
     * @param data
     * @return
     */
    @POST("v1/sessions")
    Observable<DetailResponse<Session>> login(@Body User data);


}
