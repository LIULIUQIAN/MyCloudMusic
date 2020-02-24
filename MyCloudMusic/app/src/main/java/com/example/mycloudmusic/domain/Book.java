package com.example.mycloudmusic.domain;

/**
 * 图书模型
 */
public class Book extends BaseModel {
    /**
     * 标题
     */
    private String title;

    /**
     * 封面
     */
    private String banner;

    /**
     * 价格
     */
    private Double price;

    /**
     * 谁创建了该商品
     */
    private User user;

    /**
     * 有值表示已经购买
     */
    private Integer buy;

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

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getBuy() {
        return buy;
    }

    public void setBuy(Integer buy) {
        this.buy = buy;
    }

    /**
     * 是否购买了
     *
     * @return
     */
    public boolean isBuy() {
        return buy != null;
    }
}
