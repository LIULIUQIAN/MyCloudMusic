package com.example.mycloudmusic.service;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.os.IBinder;

import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.manager.impl.MusicPlayerManagerImpl;
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
        System.out.println("MusicPlayerService-onDestroy");
    }

    /**
     * 启动service调用
     * 多次启动也调用该方法
     */
    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        return super.onStartCommand(intent, flags, startId);
    }

    /**
     * 获取音乐播放Manager
     */
    public static MusicPlayerManager getMusicPlayerManager(Context context) {
        context = context.getApplicationContext();
        ServiceUtil.startService(context,MusicPlayerService.class);

        return MusicPlayerManagerImpl.getInstance(context);
    }
}
