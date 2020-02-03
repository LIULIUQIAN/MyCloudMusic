package com.example.mycloudmusic;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import android.content.Intent;
import android.os.Bundle;

import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.activity.BaseTitleActivity;
import com.example.mycloudmusic.activity.WebViewActivity;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;

public class MainActivity extends BaseTitleActivity {

    @BindView(R.id.dl)
    DrawerLayout dl;

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

    @Override
    protected void initViews() {
        super.initViews();

        //侧滑配置
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, dl,toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        dl.addDrawerListener(toggle);
        toggle.syncState();
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
