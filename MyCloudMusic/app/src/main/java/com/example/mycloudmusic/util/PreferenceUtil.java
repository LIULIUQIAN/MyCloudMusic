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

    /**
     * 最后播放音乐id key
     */
    private static final String LAST_PLAY_SONG_ID = "LAST_PLAY_SONG_ID";

    /**
     * 是否显示全局歌词 key
     */
    private static final String KEY_SHOW_GLOBAL_LYRIC = "KEY_SHOW_GLOBAL_LYRIC";

    /**
     * 全局歌词大小 key
     */
    private static final String KEY_GLOBAL_LYRIC_TEXT_SIZE = "KEY_GLOBAL_LYRIC_TEXT_SIZE";

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

    /**
     * 获取最后播放的音乐Id
     */
    public String getLastPlaySongId() {
        return preference.getString(LAST_PLAY_SONG_ID, null);
    }

    /**
     * 设置当前播放音乐的id
     */
    public void setLastPlaySongId(String data) {
        putString(LAST_PLAY_SONG_ID, data);
    }


    /**
     * 是否显示全局歌词
     *
     * @return
     */
    public boolean isShowGlobalLyric() {
        return getBoolean(KEY_SHOW_GLOBAL_LYRIC, false);
    }

    /**
     * 设置是否显示全局歌词
     *
     * @param data
     */
    public void setShowGlobalLyric(boolean data) {
        putBoolean(KEY_SHOW_GLOBAL_LYRIC, data);
    }

    /**
     * 获取全局歌词大小
     * 默认16sp
     *
     * @return
     */
    public int getGlobalLyricTextSize() {
        return getInt(KEY_GLOBAL_LYRIC_TEXT_SIZE, DensityUtil.dipTopx(context, 16));
    }

    /**
     * 设置全局歌词大小
     *
     * @param data
     */
    public void setGlobalLyricTextSize(int data) {
        putInt(KEY_GLOBAL_LYRIC_TEXT_SIZE, data);
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
