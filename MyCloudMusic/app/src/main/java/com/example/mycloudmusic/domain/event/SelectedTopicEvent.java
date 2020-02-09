package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.Topic;

public class SelectedTopicEvent {

    private Topic data;

    public SelectedTopicEvent(Topic data) {
        this.data = data;
    }

    public Topic getData() {
        return data;
    }

    public void setData(Topic data) {
        this.data = data;
    }
}
