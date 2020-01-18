package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;

import com.example.mycloudmusic.R;

public class SplashActivity extends AppCompatActivity {

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
        System.out.println("aaaaaaaaaaaaa");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        fullScreen();

        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                handler.sendEmptyMessage(MESSAGE_NEXT);
            }
        },DEFAULT_DELAY_TIME);
    }

    /*
    * 全屏
    * */
    protected void fullScreen(){
        View decorView = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT > 11 && Build.VERSION.SDK_INT < 19){
            decorView.setSystemUiVisibility(View.GONE);
        }else if(Build.VERSION.SDK_INT >=19){
            //19及以上版本
            //SYSTEM_UI_FLAG_HIDE_NAVIGATION:隐藏导航栏
            //SYSTEM_UI_FLAG_IMMERSIVE_STICKY:从状态栏下拉会半透明悬浮显示一会儿状态栏和导航栏
            //SYSTEM_UI_FLAG_FULLSCREEN:全屏
            int options = View.SYSTEM_UI_FLAG_HIDE_NAVIGATION |
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY |
                    View.SYSTEM_UI_FLAG_FULLSCREEN;
            decorView.setSystemUiVisibility(options);

        }
    }

}
