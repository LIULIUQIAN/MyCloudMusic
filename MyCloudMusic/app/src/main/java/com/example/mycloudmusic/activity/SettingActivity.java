package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mycloudmusic.AppContext;
import com.example.mycloudmusic.R;

import butterknife.OnClick;

public class SettingActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
    }

    @OnClick(R.id.bt_logout)
    public void onLogoutClick(){

        AppContext.getInstance().logout();

    }
}
