package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.Song;

public class OnStopRecordEvent {

    private Song data;

    public OnStopRecordEvent(Song data) {
        this.data = data;
    }

    public Song getData() {
        return data;
    }

    public void setData(Song data) {
        this.data = data;
    }
}
