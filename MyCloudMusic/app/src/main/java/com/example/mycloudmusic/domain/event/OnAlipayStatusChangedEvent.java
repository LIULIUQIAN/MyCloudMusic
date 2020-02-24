package com.example.mycloudmusic.domain.event;

import com.example.mycloudmusic.domain.alipay.PayResult;

public class OnAlipayStatusChangedEvent {

    private  PayResult data;

    public OnAlipayStatusChangedEvent(PayResult resultData) {
        data = resultData;
    }

    public PayResult getData() {
        return data;
    }

    public void setData(PayResult data) {
        this.data = data;
    }
}
