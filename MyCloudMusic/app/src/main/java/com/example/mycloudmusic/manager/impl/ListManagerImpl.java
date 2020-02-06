package com.example.mycloudmusic.manager.impl;

import android.content.Context;
import android.media.MediaPlayer;
import android.text.TextUtils;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.DataUtil;
import com.example.mycloudmusic.util.ORMUtil;
import com.example.mycloudmusic.util.PreferenceUtil;

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
    private final ORMUtil orm;
    private final PreferenceUtil sp;

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
    private double lastTime;

    private ListManagerImpl(Context context) {
        this.context = context.getApplicationContext();
        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(this.context);
        musicPlayerManager.addMusicPlayerListener(this);

        orm = ORMUtil.getInstance(this.context);
        sp = PreferenceUtil.getInstance(this.context);

        initPlayList();
    }


    public static synchronized ListManager getInstance(Context context) {

        if (instance == null) {
            instance = new ListManagerImpl(context);
        }
        return instance;
    }

    /*
     * 初始化播放列表
     * */
    private void initPlayList() {

        List<Song> songs = orm.queryPlayList();
        if (songs.size() == 0) {
            return;
        }
        datum.clear();
        datum.addAll(songs);

        String lastPlaySongId = sp.getLastPlaySongId();
        if (!TextUtils.isEmpty(lastPlaySongId)) {

            for (Song song : datum) {
                if (lastPlaySongId.equals(song.getId())) {
                    data = song;
                    break;
                }
            }

            if (data == null) {
                data = datum.get(0);
            }
        } else {
            data = datum.get(0);
        }

    }


    @Override
    public void setDatum(List<Song> datum) {

        DataUtil.changePlayListFlag(this.datum, false);
        saveAll();

        this.datum.clear();
        this.datum.addAll(datum);

        DataUtil.changePlayListFlag(this.datum, true);
        saveAll();
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

        sp.setLastPlaySongId(data.getId());
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

            if (data.getProgress() > 0){
                musicPlayerManager.seekTo((int) data.getProgress());
            }
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

        orm.deleteSong(song);
    }

    @Override
    public void deleteAll() {
        if (musicPlayerManager.isPlaying()) {
            pause();
        }
        datum.clear();

        orm.deleteAll();
    }

    @Override
    public void seekTo(int progress) {

        if (!musicPlayerManager.isPlaying()){
            resume();
        }
        musicPlayerManager.seekTo(progress);
    }

    /**
     * 保存播放列表
     */
    private void saveAll() {
        orm.saveAll(datum);
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

    @Override
    public void onProgress(Song data) {
        long currentTimeMillis = System.currentTimeMillis();

        if (currentTimeMillis - lastTime > Constant.SAVE_PROGRESS_TIME) {
            orm.saveSong(data);
            lastTime = currentTimeMillis;
        }
    }

    //end播放器回调
}
