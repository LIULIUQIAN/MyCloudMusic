package com.example.mycloudmusic.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.text.TextUtils;
import android.widget.ImageView;

import androidx.annotation.DrawableRes;
import androidx.annotation.Nullable;
import androidx.annotation.RawRes;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.example.mycloudmusic.R;

public class ImageUtil {

    /**
     * 显示图片
     */
    public static void showAvatar(Activity activity, ImageView view, String uri) {
        if (TextUtils.isEmpty(uri)){
            show(activity,view,R.drawable.placeholder);
            return;
        }
        if (!uri.startsWith("http")) {
            uri = String.format(Constant.RESOURCE_ENDPOINT,uri);
        }
        showFull(activity, view, uri);
    }

    /**
     * 显示圆形图片
     */
    public static void showCircleAvatar(Activity activity, ImageView view, String uri) {
        if (TextUtils.isEmpty(uri)){
            showCircle(activity,view,R.drawable.placeholder);
            return;
        }
        if (!uri.startsWith("http")) {
            uri = String.format(Constant.RESOURCE_ENDPOINT,uri);
        }
        showCircleFull(activity, view, uri);
    }


    /**
     * 显示资源目录图片
     */
    public static void show(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {

        Glide.with(activity)
                .load(resourceId)
                .apply(getCommonRequestOptions())
                .into(view);
    }
    /**
     * 显示网络图片
     */
    private static void showFull(Activity activity, ImageView view, String uri) {

        Glide.with(activity)
                .load(uri)
                .apply(getCommonRequestOptions())
                .into(view);
    }

    /**
     * 显示<圆形>资源目录图片
     */
    public static void showCircle(Activity activity, ImageView view, @RawRes @DrawableRes @Nullable int resourceId) {

        Glide.with(activity)
                .load(resourceId)
                .apply(getCircleCommonRequestOptions())
                .into(view);
    }
    /**
     * 显示<圆形>网络图片
     */
    private static void showCircleFull(Activity activity, ImageView view, String uri) {

        Glide.with(activity)
                .load(uri)
                .apply(getCircleCommonRequestOptions())
                .into(view);
    }

    /**
     * 获取公共配置
     */
    @SuppressLint("CheckResult")
    public static RequestOptions getCommonRequestOptions() {

        RequestOptions options = new RequestOptions();
        options.placeholder(R.drawable.placeholder);
        options.error(R.drawable.placeholder);
        options.centerCrop();
        return options;
    }

    /**
     * 获取<圆形>通用的配置
     */
    @SuppressLint("CheckResult")
    private static RequestOptions getCircleCommonRequestOptions() {
        //获取通用配置
        RequestOptions options = getCommonRequestOptions();
        options.circleCrop();
        return options;
    }
}
