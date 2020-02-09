package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SelectLyricAdapter;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.lyric.Line;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ShareUtil;
import com.example.mycloudmusic.util.TextUtil;
import com.example.mycloudmusic.util.ToastUtil;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class SelectLyricActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Song data;
    private SelectLyricAdapter adapter;

    public static void start(Activity activity, Song data) {

        Intent intent = new Intent(activity, SelectLyricActivity.class);
        intent.putExtra(Constant.DATA, data);
        activity.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_lyric);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(getMainActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        data = (Song) getIntent().getSerializableExtra(Constant.DATA);

        adapter = new SelectLyricAdapter(R.layout.item_select_lyric);
        recyclerView.setAdapter(adapter);

        adapter.replaceData(data.getParsedLyric().getDatum());
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter a, View view, int position) {

                Line line = (Line) a.getItem(position);
                line.setChooseState(!line.isChooseState());
                adapter.notifyDataSetChanged();


            }
        });
    }

    @OnClick(R.id.bt_share_lyric)
    public void onShareLyricClick() {
        String lyricString = getSelectLyricString("，");
        if (TextUtils.isEmpty(lyricString)) {
            ToastUtil.errorShortToast(R.string.hint_select_lyric);
            return;
        }

        //分享
        ShareUtil.shareLyricText(getMainActivity(), data, lyricString);

    }


    @OnClick(R.id.bt_share_lyric_image)
    public void onShareLyricImageClick() {

        String lyricString = getSelectLyricString("\n");
        if (TextUtils.isEmpty(lyricString)) {
            ToastUtil.errorShortToast(R.string.hint_select_lyric);
            return;
        }

        ShareLyricImageActivity.start(getMainActivity(), data, lyricString);
    }

    private String getSelectLyricString(String separator) {

        List<String> lines = new ArrayList<>();

        for (int i = 0; i < adapter.getData().size(); i++) {
            Line line = adapter.getItem(i);
            if (line.isChooseState()) {
                lines.add(line.getData());
            }
        }

        return StringUtils.join(lines, separator);
    }
}
