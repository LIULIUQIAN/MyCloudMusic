package com.example.mycloudmusic.domain;

import static com.example.mycloudmusic.util.Constant.TYPE_SONG;

public class Song extends BaseMultiItemEntity {
    @Override
    public int getItemType() {
        return TYPE_SONG;
    }
}
