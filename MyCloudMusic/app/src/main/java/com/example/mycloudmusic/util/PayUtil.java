package com.example.mycloudmusic.util;

import android.app.Activity;
import android.util.Log;

import com.alipay.sdk.app.PayTask;
import com.example.mycloudmusic.domain.alipay.PayResult;
import com.example.mycloudmusic.domain.event.OnAlipayStatusChangedEvent;


import org.greenrobot.eventbus.EventBus;

import java.util.Map;

public class PayUtil {
    public static void alipay(Activity activity, String data) {

        //创建运行对象
        Runnable runnable = new Runnable() {
            /**
             * 子线程执行
             */
            @Override
            public void run() {
                //创建支付宝支付任务
                PayTask alipay = new PayTask(activity);

                //支付结果
                Map<String, String> result = alipay.payV2(data, true);

                //解析支付结果
                PayResult resultData = new PayResult(result);

                Log.e("PayUtil", "alipay:" + resultData);

                //发布状态
                EventBus.getDefault().post(new OnAlipayStatusChangedEvent(resultData));
            }
        };

        //创建一个线程
        Thread thread = new Thread(runnable);

        //启动线程
        thread.start();
    }
}
