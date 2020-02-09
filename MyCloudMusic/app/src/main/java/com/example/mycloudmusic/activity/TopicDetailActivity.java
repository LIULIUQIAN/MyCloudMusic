package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

public class TopicDetailActivity extends BaseTitleActivity {

    public static void start(Activity activity, String title) {

        Intent intent = new Intent(activity, TopicDetailActivity.class);
        if (!TextUtils.isEmpty(title)) {
            intent.putExtra(Constant.TITLE, title);
        }
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_topic_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();


    }
}
