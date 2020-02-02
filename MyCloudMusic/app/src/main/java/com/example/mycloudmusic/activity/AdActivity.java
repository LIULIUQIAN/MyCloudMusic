package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;

import com.example.mycloudmusic.R;

import butterknife.BindView;
import butterknife.OnClick;

public class AdActivity extends BaseCommonActivity {

    @BindView(R.id.bt_skip_ad)
    Button bt_skip_ad;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ad);
    }

    @Override
    protected void initViews() {
        super.initViews();

        fullScreen();
    }

    @OnClick(R.id.bt_skip_ad)
    public void onSkipAd() {
        System.out.println("aaaaaa");
    }

    @OnClick(R.id.bt_ad)
    public void onAdClick() {
        System.out.println("aaaaaabbb");
    }
}
