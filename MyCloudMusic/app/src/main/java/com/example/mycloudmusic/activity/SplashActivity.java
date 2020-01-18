package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.mycloudmusic.R;

public class SplashActivity extends BaseCommonActivity {

    private static final int MESSAGE_NEXT = 100;
    private static final int DEFAULT_DELAY_TIME = 3000;

    Handler handler = new Handler(){
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case MESSAGE_NEXT:
                    next();
                    break;
            }
        }
    };
    private void next() {

        startActivityAfterFinishThis(GuideActivity.class);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        //全屏
        fullScreen();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MESSAGE_NEXT);
            }
        },DEFAULT_DELAY_TIME);
    }



}
