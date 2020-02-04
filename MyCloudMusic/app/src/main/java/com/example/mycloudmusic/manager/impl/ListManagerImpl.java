package com.example.mycloudmusic.manager.impl;

import android.content.Context;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;

import java.util.LinkedList;
import java.util.List;

public class ListManagerImpl implements ListManager {


    private static ListManagerImpl instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;

    List<Song> datum = new LinkedList<>();
    private Song data;

    /**
     * 是否播放了
     */
    private boolean isPlay;

    private ListManagerImpl(Context context) {
        this.context = context;
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
    }

    public static synchronized ListManager getInstance(Context context) {

        if (instance == null) {
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    @Override
    public void setDatum(List<Song> datum) {
        this.datum.clear();
        this.datum.addAll(datum);
    }

    @Override
    public List<Song> getDatum() {
        return datum;
    }

    @Override
    public void play(Song data) {

        this.data = data;

        String uri = data.getUri();
        if (!uri.startsWith("http")) {
            uri = String.format(Constant.RESOURCE_ENDPOINT, uri);
        }
        musicPlayerManager.play(uri, data);
        //标记已经播放了
        isPlay = true;
    }

    @Override
    public void pause() {
        musicPlayerManager.pause();
    }

    @Override
    public void resume() {
        if (isPlay){
            musicPlayerManager.resume();
        }else {
            play(data);
        }
    }

}
