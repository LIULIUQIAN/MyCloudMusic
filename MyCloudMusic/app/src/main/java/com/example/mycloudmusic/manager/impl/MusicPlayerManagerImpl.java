package com.example.mycloudmusic.manager.impl;

import android.content.Context;

import com.example.mycloudmusic.manager.MusicPlayerManager;

public class MusicPlayerManagerImpl implements MusicPlayerManager {

    private static MusicPlayerManagerImpl instance;
    private final Context context;

    public MusicPlayerManagerImpl(Context context) {
        this.context = context;
    }

    public static synchronized MusicPlayerManagerImpl getInstance(Context context){

        if (instance == null){
            instance = new MusicPlayerManagerImpl(context);
        }
        return instance;
    }


}
