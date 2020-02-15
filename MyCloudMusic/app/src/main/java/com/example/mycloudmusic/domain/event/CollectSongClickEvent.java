package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.Song;

public class CollectSongClickEvent {
    private Song data;

    public CollectSongClickEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
