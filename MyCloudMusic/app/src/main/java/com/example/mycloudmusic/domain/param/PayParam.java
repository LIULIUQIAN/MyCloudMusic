package com.example.mycloudmusic.domain.param;


import static com.example.mycloudmusic.domain.Order.ANDROID;

public class PayParam {
    /**
     * 支付平台
     * 默认值为android
     * 且不能更改
     * 因为Android平台的来说肯定就是Android
     */
    private final int origin = ANDROID;

    /**
     * 支付渠道
     */
    private int channel;

    public int getOrigin() {
        return origin;
    }

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }
}
