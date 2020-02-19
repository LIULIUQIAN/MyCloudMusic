package com.example.mycloudmusic.domain;

/**
 * 视频模型
 */
public class Video extends BaseModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 视频地址
     * 和图片地址一样
     * 都是相对地址
     */
    private String uri;

    /**
     * 封面地址
     */
    private String banner;

    /**
     * 视频时长
     * 单位：秒
     */
    private long duration;

    /**
     * 点击数
     */
    private long clicks_count;

    /**
     * 评论数
     */
    private long comments_count;

    /**
     * 谁发布了这个视频
     */
    private User user;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public String getBanner() {
        return banner;
    }

    public void setBanner(String banner) {
        this.banner = banner;
    }

    public long getDuration() {
        return duration;
    }

    public void setDuration(long duration) {
        this.duration = duration;
    }

    public long getClicks_count() {
        return clicks_count;
    }

    public void setClicks_count(long clicks_count) {
        this.clicks_count = clicks_count;
    }

    public long getComments_count() {
        return comments_count;
    }

    public void setComments_count(long comments_count) {
        this.comments_count = comments_count;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Video{");
        sb.append("title='").append(title).append('\'');
        sb.append(", uri='").append(uri).append('\'');
        sb.append(", banner='").append(banner).append('\'');
        sb.append(", duration=").append(duration);
        sb.append(", clicks_count=").append(clicks_count);
        sb.append(", comments_count=").append(comments_count);
        sb.append(", user=").append(user);
        sb.append('}');
        return sb.toString();
    }
}
