package com.example.mycloudmusic.domain;

import java.util.List;

public class MeGroup {

    private String title;

    private List<Sheet> datum;

    private boolean isMore;

    public MeGroup(String title, List<Sheet> datum, boolean isMore) {
        this.title = title;
        this.datum = datum;
        this.isMore = isMore;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<Sheet> getDatum() {
        return datum;
    }

    public void setDatum(List<Sheet> datum) {
        this.datum = datum;
    }

    public boolean isMore() {
        return isMore;
    }

    public void setMore(boolean more) {
        isMore = more;
    }
}
