package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.widget.Button;

import com.example.mycloudmusic.MainActivity;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;
import butterknife.OnClick;

public class AdActivity extends BaseCommonActivity {

    @BindView(R.id.bt_skip_ad)
    Button bt_skip_ad;

    private CountDownTimer downTimer;

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

    @Override
    protected void onResume() {
        super.onResume();
        startCountDown();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (downTimer != null) {
            downTimer.cancel();
        }
    }

    @OnClick(R.id.bt_skip_ad)
    public void onSkipAd() {
        if (downTimer != null) {
            downTimer.cancel();
        }
        next();
    }

    @OnClick(R.id.bt_ad)
    public void onAdClick() {

        if (downTimer != null) {
            downTimer.cancel();
        }

        Intent intent = new Intent(getMainActivity(),MainActivity.class);
        intent.putExtra(Constant.URL,"https://www.baidu.com");
        intent.setAction(Constant.ACTION_AD);
        startActivity(intent);
        finish();
    }

    /*
     * 广告倒计时
     * */
    private void startCountDown() {

        downTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                bt_skip_ad.setText(getString(R.string.count_second, millisUntilFinished / 1000 + 1));
            }

            @Override
            public void onFinish() {
                next();
            }
        };

        downTimer.start();
    }

    private void next() {

        startActivityAfterFinishThis(MainActivity.class);
    }
}
