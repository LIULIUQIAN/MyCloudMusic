package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import com.example.mycloudmusic.R;

import static com.example.mycloudmusic.util.Constant.SHEET_ID;

public class CommentActivity extends BaseTitleActivity {

    /**
     * 启动评论界面
     */
    public static void start(Activity activity, String sheetId) {

        Intent intent = new Intent(activity, CommentActivity.class);
        //歌单id
        intent.putExtra(SHEET_ID, sheetId);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        System.out.println("============"+extraString(SHEET_ID));
    }
}
