package com.example.mycloudmusic.domain;

/**
 * 话题模型
 */
public class Topic extends BaseModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String banner;

    /**
     * 描述
     */
    private String description;

    /**
     * 参与人数
     */
    private int joins_count;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getJoins_count() {
        return joins_count;
    }

    public void setJoins_count(int joins_count) {
        this.joins_count = joins_count;
    }
}
