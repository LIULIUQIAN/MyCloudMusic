package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.util.Constant;

public class SheetDetailActivity extends BaseTitleActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initDatum() {
        super.initDatum();
        String id = getIntent().getStringExtra(Constant.ID);
        System.out.println("歌单 id==="+id);
    }
}
