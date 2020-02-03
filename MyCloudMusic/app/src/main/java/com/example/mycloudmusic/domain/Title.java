package com.example.mycloudmusic.domain;

import static com.example.mycloudmusic.util.Constant.TYPE_TITLE;

public class Title extends BaseMultiItemEntity {

    private String title;

    public Title(String title) {
        this.title = title;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TYPE_TITLE;
    }
}
