package com.example.mycloudmusic.domain.param;


import com.example.mycloudmusic.domain.Order;

/**
 * 创建订单参数
 * 可以复用Order
 */
public class OrderParam {
    /**
     * 商品id
     */
    private String book_id;

    /**
     * 创建订单的平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    private final int source = Order.ANDROID;

    public String getBook_id() {
        return book_id;
    }

    public void setBook_id(String book_id) {
        this.book_id = book_id;
    }

    public int getSource() {
        return source;
    }
}
