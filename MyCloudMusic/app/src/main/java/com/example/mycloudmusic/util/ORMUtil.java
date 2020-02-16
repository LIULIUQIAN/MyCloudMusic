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
     * 删除音乐
     */
    public void deleteSong(Song song) {

        Realm realm = getRealm();
        SongLocal songLocal = realm.where(SongLocal.class)
                .equalTo("playList", true)
                .and()
                .equalTo("id", song.getId())
                .findFirst();

//        //物理删除
//        realm.executeTransaction(new Realm.Transaction() {
//            @Override
//            public void execute(Realm realm) {
//                songLocal.deleteFromRealm();
//            }
//        });

        realm.beginTransaction();

        //逻辑删除
        songLocal.setPlayList(false);

        realm.commitTransaction();
        realm.close();
    }

    /**
     * 删除音乐
     */
    public void deleteAll() {
        Realm realm = getRealm();

        RealmResults<SongLocal> playList = realm.where(SongLocal.class)
                .equalTo("playList", true)
                .findAll();

        realm.beginTransaction();
        for (SongLocal songLocal : playList) {
            //逻辑删除
            songLocal.setPlayList(false);
        }
        realm.commitTransaction();
        realm.close();
    }

    /**
     * 查询本地音乐
     *
     * @param sortIndex
     * @return
     */
    public List<Song> queryLocalMusic(int sortIndex) {
        //获取数据库实例
        Realm realm = getRealm();

        //查询所有本地音乐
        RealmResults<SongLocal> songLocals = realm.where(SongLocal.class)
                .equalTo("source", SongLocal.SOURCE_LOCAL)
                .findAll()
                .sort(SongLocal.SORT_KEYS[sortIndex]);

        List<Song> songs = new ArrayList<>();
        for (SongLocal songLocal : songLocals) {
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

    /**
     * 保存本地音乐
     *
     * @param data
     */
    public void saveSongLocal(SongLocal data) {
        //获取数据库框架实例
        Realm realm = getRealm();

        try {
            //开始事务
            realm.beginTransaction();

            //信息或者更新到数据库
            realm.copyToRealmOrUpdate(data);

            //提交事务
            realm.commitTransaction();

        } catch (Exception e) {
            //出错了
            e.printStackTrace();

            //回滚事务
            realm.cancelTransaction();
        } finally {
            //关闭数据库
            realm.close();
        }

    }
}
