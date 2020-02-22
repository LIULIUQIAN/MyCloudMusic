package com.example.mycloudmusic.util;

import android.content.Context;

import com.alibaba.sdk.android.oss.ClientConfiguration;
import com.alibaba.sdk.android.oss.OSSClient;
import com.alibaba.sdk.android.oss.common.auth.OSSPlainTextAKSKCredentialProvider;


public class OSSUtil {

    private static OSSClient instance;

    public static OSSClient getInstance(Context context) {
        if (instance == null) {
            init(context);
        }
        return instance;
    }

    private static void init(Context context) {

        OSSPlainTextAKSKCredentialProvider credentialProvider = new OSSPlainTextAKSKCredentialProvider(Constant.ALIYUN_OSS_AK, Constant.ALIYUN_OSS_SK);

        ClientConfiguration config = new ClientConfiguration();
        //连接超时，默认15秒
        config.setConnectionTimeout(15 * 1000);
        //socket超时，默认15秒
        config.setSocketTimeout(15 * 1000);
        //最大并发请求数，默认5个
        config.setMaxConcurrentRequest(5);
        //失败后最大重试次数，默认2次
        config.setMaxErrorRetry(2);

        instance = new OSSClient(context.getApplicationContext(), String.format(Constant.RESOURCE_ENDPOINT, ""), credentialProvider);
    }
}
