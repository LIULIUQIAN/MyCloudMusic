package com.example.mycloudmusic.domain.event;

public class CreateSheetEvent {

    private String data;

    public CreateSheetEvent(String data) {
        this.data = data;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
