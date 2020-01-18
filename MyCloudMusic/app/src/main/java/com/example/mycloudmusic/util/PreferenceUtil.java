package com.example.mycloudmusic.util;

import android.content.Context;
import android.content.SharedPreferences;

public class PreferenceUtil {

    /**
     * 偏好设置文件名称
     */
    private static final String NAME = "ixuea_my_cloud_music";

    /**
     * 是否显示引导界面key
     */
    private static final String SHOW_GUIDE = "SHOW_GUIDE";

    private static PreferenceUtil instance;
    private final Context context;
    private final SharedPreferences preference;

    public PreferenceUtil(Context context) {
        this.context = context.getApplicationContext();

        preference = this.context.getSharedPreferences(NAME, Context.MODE_PRIVATE);
    }

    public static PreferenceUtil getInstance(Context context){
        if (instance == null){
            instance = new PreferenceUtil(context);
        }
        return instance;
    }
    /**
     * 是否显示引导界面
     * @return
     */
    public boolean isShowGuide(){
       return preference.getBoolean(SHOW_GUIDE,true);
    }

    /**
     * 设置是否显示引导界面
     * @param value
     */
    public void setShowGuide(boolean value){
        preference.edit().putBoolean(SHOW_GUIDE,value).apply();
    }
}
