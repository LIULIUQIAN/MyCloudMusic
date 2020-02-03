package com.example.mycloudmusic.domain;

import static com.example.mycloudmusic.util.Constant.TYPE_SHEET;

public class Sheet extends BaseMultiItemEntity {

    private String title;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    @Override
    public int getItemType() {
        return TYPE_SHEET;
    }

    /**
     * 占多少列
     */
    @Override
    public int getSpanSize() {
        return 1;
    }

}
