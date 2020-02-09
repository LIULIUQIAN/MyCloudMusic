package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

public class UserDetailActivity extends BaseTitleActivity {

    public static void start(Activity activity, String id, String nickname) {
        Intent intent = new Intent(activity, UserDetailActivity.class);
        if (!TextUtils.isEmpty(id)) {
            intent.putExtra(Constant.ID, id);
        }
        if (!TextUtils.isEmpty(nickname)) {
            intent.putExtra(Constant.NICKNAME, nickname);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_detail);
    }
}
