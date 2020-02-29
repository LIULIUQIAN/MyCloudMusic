package com.example.mycloudmusic.domain.event;

public class OnSearchEvent {

    private String data;
    private int selectedIndex;

    public OnSearchEvent(String data, int selectedIndex) {
        this.data = data;
        this.selectedIndex = selectedIndex;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public int getSelectedIndex() {
        return selectedIndex;
    }

    public void setSelectedIndex(int selectedIndex) {
        this.selectedIndex = selectedIndex;
    }
}
