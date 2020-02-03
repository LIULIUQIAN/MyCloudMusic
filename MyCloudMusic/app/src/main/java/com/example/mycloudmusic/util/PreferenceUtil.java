package com.example.mycloudmusic.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;

public class PreferenceUtil {

    /**
     * 偏好设置文件名称
     */
    private static final String NAME = "ixuea_my_cloud_music";

    /**
     * 是否显示引导界面key
     */
    private static final String SHOW_GUIDE = "SHOW_GUIDE";

    /**
     * 用户登录session key
     */
    private static final String SESSION = "SESSION";

    /**
     * 用户UserId key
     */
    private static final String USER_ID = "USER_ID";

    private static PreferenceUtil instance;
    private final Context context;
    private final SharedPreferences preference;

    public PreferenceUtil(Context context) {
        this.context = context.getApplicationContext();

        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(Context context) {
        if (instance == null) {
            instance = new PreferenceUtil(context);
        }
        return instance;
    }

    /**
     * 是否显示引导界面
     *
     * @return
     */
    public boolean isShowGuide() {
        return getBoolean(SHOW_GUIDE, true);
    }

    /**
     * 设置是否显示引导界面
     *
     * @param value
     */
    public void setShowGuide(boolean value) {
        putBoolean(SHOW_GUIDE, value);
    }

    /**
     * 保存登录session
     */
    public void setSession(String value) {
        putString(SESSION, value);
    }

    /**
     * 获取登录session
     */
    public String getSession() {
        return preference.getString(SESSION, null);
    }

    /**
     * 设置用户Id
     */
    public void setUserId(String value) {
        putString(USER_ID, value);
    }

    /**
     * 获取用户Id
     */
    public String getUserId() {
        return preference.getString(USER_ID, null);
    }

    /*
     * 判断用户是否登录
     * */
    public boolean isLogin() {
        return !TextUtils.isEmpty(getSession());
    }

    /*
    * 退出
    * */
    public void logout() {
        delete(USER_ID);
        delete(SESSION);
    }

    //辅助方法

    /**
     * 保存字符串
     */
    private void putString(String key, String value) {
        preference.edit().putString(key, value).apply();
    }

    /**
     * 保存boolean
     */
    private void putBoolean(String key, boolean value) {
        preference.edit().putBoolean(key, value).apply();
    }

    /**
     * 获取boolean
     */
    private boolean getBoolean(String key, boolean defaultValue) {
        return preference.getBoolean(key, defaultValue);
    }

    /**
     * 删除内容
     */
    private void delete(String key) {
        preference.edit().remove(key).apply();
    }

    /**
     * 获取一个int
     */
    private int getInt(String key, int defaultValue) {
        return preference.getInt(key, defaultValue);
    }

    /**
     * 设置一个int
     */
    private void putInt(String key, int data) {
        preference.edit().putInt(key, data).apply();
    }



    //end 辅助方法

}
