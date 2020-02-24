package com.example.mycloudmusic.domain;

/**
 * 支付参数
 * 网络请求响应
 */
public class Pay {
    /**
     * 支付渠道
     */
    private int channel;

    /**
     * 支付参数
     */
    private String pay;

    public int getChannel() {
        return channel;
    }

    public void setChannel(int channel) {
        this.channel = channel;
    }

    public String getPay() {
        return pay;
    }

    public void setPay(String pay) {
        this.pay = pay;
    }

    @Override
    public String toString() {
        final StringBuilder sb = new StringBuilder("Pay{");
        sb.append("channel=").append(channel);
        sb.append(", pay='").append(pay).append('\'');
        sb.append('}');
        return sb.toString();
    }
}
