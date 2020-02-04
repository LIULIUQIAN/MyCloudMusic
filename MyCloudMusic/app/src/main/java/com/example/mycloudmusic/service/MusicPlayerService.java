package com.example.mycloudmusic.service;

import android.app.Notification;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.IBinder;

import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.manager.impl.ListManagerImpl;
import com.example.mycloudmusic.manager.impl.MusicPlayerManagerImpl;
import com.example.mycloudmusic.util.NotificationUtil;
import com.example.mycloudmusic.util.ServiceUtil;

public class MusicPlayerService extends Service {
    public MusicPlayerService() {
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public void onCreate() {
        super.onCreate();

        System.out.println("MusicPlayerService-onCreate");
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        //停止前台服务
        stopForeground(true);
        System.out.println("MusicPlayerService-onDestroy");
    }

    /**
     * 启动service调用
     * 多次启动也调用该方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        //设置service为前台service,提高应用的优先级,8.0新特性
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Notification notification = NotificationUtil.getServiceForeground(getApplicationContext());
            //Id写0：这个通知就不会显示
            NotificationUtil.showNotification(0, notification);
        }
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取音乐播放Manager
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context) {
        context = context.getApplicationContext();
        ServiceUtil.startService(context, MusicPlayerService.class);

        return MusicPlayerManagerImpl.getInstance(context);
    }

    /**
     * 获取音乐播放列表Manager
     */
    public static ListManager getListManager(Context context) {
        context = context.getApplicationContext();
        ServiceUtil.startService(context, MusicPlayerService.class);

        return ListManagerImpl.getInstance(context);
    }

}
