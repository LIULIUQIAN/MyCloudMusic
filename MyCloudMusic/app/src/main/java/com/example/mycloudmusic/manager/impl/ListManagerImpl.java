package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;

import java.util.LinkedList;
import java.util.List;
import java.util.Random;

import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_LIST;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_ONE;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_RANDOM;

public class ListManagerImpl implements ListManager, MusicPlayerListener {


    private static ListManagerImpl instance;
    private final Context context;
    private final MusicPlayerManager musicPlayerManager;

    List<Song> datum = new LinkedList<>();
    private Song data;

    /**
     * 是否播放了
     */
    private boolean isPlay;

    /**
     * 循环模式
     * 默认列表循环
     */
    private int model = MODEL_LOOP_LIST;

    private ListManagerImpl(Context context) {
        this.context = context;
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);
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
    public Song getData() {
        return data;
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
        if (isPlay) {
            musicPlayerManager.resume();
        } else {
            play(data);
        }
    }

    @Override
    public Song previous() {

        int index = 0;

        switch (model) {
            case MODEL_LOOP_RANDOM:
                index = new Random().nextInt(datum.size());
                break;
            default:
                index = datum.indexOf(data);
                if (index != -1) {
                    if (index == 0) {
                        index = datum.size() - 1;
                    } else {
                        index--;
                    }
                } else {
                    throw new IllegalArgumentException("Cant't find current song");
                }
                break;
        }
        return datum.get(index);
    }

    @Override
    public Song next() {
        int index = 0;

        switch (model) {
            case MODEL_LOOP_RANDOM:
                index = new Random().nextInt(datum.size());
                break;
            default:
                index = datum.indexOf(data);
                if (index != -1) {
                    if (index == datum.size() - 1) {
                        index = 0;
                    } else {
                        index++;
                    }
                } else {
                    throw new IllegalArgumentException("Cant't find current song");
                }
                break;
        }
        return datum.get(index);
    }

    @Override
    public int changeLoopModel() {
        model++;
        if (model > MODEL_LOOP_RANDOM) {
            model = MODEL_LOOP_LIST;
        }

        if (model == MODEL_LOOP_ONE) {
            musicPlayerManager.setLooping(true);
        } else {
            musicPlayerManager.setLooping(false);
        }
        return model;
    }

    @Override
    public int getLoopModel() {
        return model;
    }

    @Override
    public void delete(int index) {

        //获取要删除的音乐
        Song song = datum.get(index);

        if (song.getId().equals(data.getId())) {
            pause();
            Song next = next();
            if (next.getId().equals(data.getId())) {
                data = null;
            } else {
                play(next);
            }
        }
        datum.remove(song);
    }


    // 播放器回调
    @Override
    public void onCompletion(MediaPlayer mp) {
        //自动播放下一首音乐
        if (getLoopModel() != MODEL_LOOP_ONE) {
            Song song = next();
            if (song != null) {
                play(song);
            }
        }

    }

    //end播放器回调
}
