package com.example.mycloudmusic.manager.impl;

import android.content.Context;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.manager.ListManager;

import java.util.LinkedList;
import java.util.List;

public class ListManagerImpl implements ListManager {


    private static ListManagerImpl instance;
    private final Context context;

    List<Song> datum = new LinkedList<>();

    private ListManagerImpl(Context context) {
        this.context = context;
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
        System.out.println("============play");
    }

    @Override
    public void pause() {
        System.out.println("============pause");
    }

    @Override
    public void resume() {
        System.out.println("============resume");
    }
}
