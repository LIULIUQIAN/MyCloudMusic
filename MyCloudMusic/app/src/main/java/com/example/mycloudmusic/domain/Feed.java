package com.example.mycloudmusic.domain;

import java.util.List;

/**
 * 动态
 */
public class Feed extends BaseModel {
    /**
     * 动态内容
     */
    private String content;

    /**
     * 省
     */
    private String province;

    /**
     * 市
     */
    private String city;

    /**
     * 创建用户
     */
    private User user;

    /**
     * 图片
     */
    private List<Resource> images;

    /**
     * 经度
     */
    private double longitude;

    /**
     * 纬度
     */
    private double latitude;

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public List<Resource> getImages() {
        return images;
    }

    public void setImages(List<Resource> images) {
        this.images = images;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Feed{");
        sb.append("content='").append(content).append('\'');
        sb.append(", province='").append(province).append('\'');
        sb.append(", city='").append(city).append('\'');
        sb.append(", user=").append(user);
        sb.append(", images=").append(images);
        sb.append('}');
        return sb.toString();
    }
}
