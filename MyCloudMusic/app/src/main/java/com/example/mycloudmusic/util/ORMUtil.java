package com.example.mycloudmusic.util;

import android.content.Context;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.SongLocal;

import java.util.ArrayList;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;

public class ORMUtil {

    private static ORMUtil instance;
    private final Context context;
    private final PreferenceUtil sp;

    private ORMUtil(Context context) {
        this.context = context.getApplicationContext();
        sp = PreferenceUtil.getInstance(this.context);
    }

    public static ORMUtil getInstance(Context context) {

        if (instance == null) {
            instance = new ORMUtil(context);
        }
        return instance;
    }

    /*
     * 保存音乐对象
     * */
    public void saveSong(Song song) {

        SongLocal songLocal = song.toSongLocal();

        Realm realm = getRealm();

        realm.beginTransaction();

        realm.copyToRealmOrUpdate(songLocal);

        realm.commitTransaction();
        realm.close();
    }

    /**
     * 保存所有音乐
     */
    public void saveAll(List<Song> datum) {

        Realm realm = getRealm();
        realm.beginTransaction();
        for (Song data : datum) {
            realm.copyToRealmOrUpdate(data.toSongLocal());
        }
        realm.commitTransaction();
        realm.close();
    }


    /**
     * 从数据库中查询播放列表
     *
     * @return
     */
    public List<Song> queryPlayList() {
        Realm realm = getRealm();

        RealmResults<SongLocal> playList = realm.where(SongLocal.class).equalTo("playList", true).findAll();

        List<Song> songs = new ArrayList<>();
        for (SongLocal songLocal : playList) {
            songs.add(songLocal.toSong());
        }
        realm.close();
        return songs;
    }

    /**
     * 获取数据库对象
     */
    private Realm getRealm() {

        RealmConfiguration configuration = new RealmConfiguration.Builder()
                .name(String.format("%s.realm", sp.getUserId()))
                .build();
        return Realm.getInstance(configuration);

    }

    public static void destroy() {
        instance = null;
    }

}
