package com.example.mycloudmusic.util;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;

import java.util.List;

public class ServiceUtil {

    /**
     * 启动service
     */
    public static void startService(Context context, Class<?> clazz) {

        if (!ServiceUtil.isServiceRunning(context,clazz)){
            Intent intent = new Intent(context,clazz);
            context.startService(intent);
        }
    }

    /**
     * service是否在运行
     */
    public static boolean isServiceRunning(Context context, Class<?> clazz) {

        ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);

        List<ActivityManager.RunningServiceInfo> services = activityManager.getRunningServices(Integer.MAX_VALUE);

        if (services == null || services.size() == 0){
            return false;
        }

        for (ActivityManager.RunningServiceInfo service : services){

            if (service.service.getClassName().equals(clazz.getName())){
                return true;
            }
        }

        return false;
    }
}
