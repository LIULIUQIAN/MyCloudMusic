package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;

public class SplashActivity extends BaseCommonActivity {

    private static final int MESSAGE_NEXT = 100;
    private static final int DEFAULT_DELAY_TIME = 3000;

    Handler handler = new Handler() {
        @Override
        public void handleMessage(@NonNull Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MESSAGE_NEXT:
                    next();
                    break;
            }
        }
    };

    private void next() {

        if (sp.isShowGuide()) {
            startActivityAfterFinishThis(GuideActivity.class);
        } else if (sp.isLogin()) {
            startActivityAfterFinishThis(AdActivity.class);
        } else {
            startActivityAfterFinishThis(LoginOrRegisterActivity.class);
        }
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
        }, DEFAULT_DELAY_TIME);
    }


}
