package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.Song;

public class OnStartRecordEvent {

    private Song data;

    public OnStartRecordEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
