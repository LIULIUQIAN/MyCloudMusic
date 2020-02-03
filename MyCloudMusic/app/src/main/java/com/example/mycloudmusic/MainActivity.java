package com.example.mycloudmusic;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.activity.WebViewActivity;
import com.example.mycloudmusic.util.Constant;

public class MainActivity extends BaseCommonActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //处理动作
        processIntent(getIntent());

    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);

        processIntent(intent);
    }

    /*
     * 处理动作
     * */
    private void processIntent(Intent intent) {

        //广告点击
        if (Constant.ACTION_AD.equals(intent.getAction())) {
            WebViewActivity.start(getMainActivity(), "广告详情", intent.getStringExtra(Constant.URL));
        }
    }
}
