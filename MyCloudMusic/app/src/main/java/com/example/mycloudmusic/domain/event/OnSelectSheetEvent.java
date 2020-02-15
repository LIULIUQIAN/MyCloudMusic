package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.Sheet;

public class OnSelectSheetEvent {

    private Sheet data;
    public OnSelectSheetEvent(Sheet data) {
        this.data = data;
    }

    public Sheet getData() {
        return data;
    }

    public void setData(Sheet data) {
        this.data = data;
    }
}
