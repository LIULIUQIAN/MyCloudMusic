package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.User;

public class SelectedFriendEvent {
    private User data;

    public SelectedFriendEvent(User data) {
        this.data = data;
    }

    public User getData() {
        return data;
    }

    public void setData(User data) {
        this.data = data;
    }
}
