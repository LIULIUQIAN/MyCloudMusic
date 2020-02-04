package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SongAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;

import butterknife.BindView;

public class SheetDetailActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    //歌单详情数据
    private Sheet data;

    private SongAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recyclerView.setLayoutManager(new LinearLayoutManager(getMainActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

         adapter = new SongAdapter(R.layout.item_song_detail);
         recyclerView.setAdapter(adapter);

        fetchData();
    }
    /*
    * 请求歌单详情数据
    * */
    private void fetchData(){
        Api.getInstance().sheetDetail(extraId()).subscribe(new HttpObserver<DetailResponse<Sheet>>() {
            @Override
            public void onSucceeded(DetailResponse<Sheet> data) {
                next(data.getData());
            }
        });
    }

    /*
    * 显示歌单数据
    * */
    private void next(Sheet sheet){
        this.data = sheet;

        if (sheet.getSongs() != null && sheet.getSongs().size() > 0){
            adapter.replaceData(sheet.getSongs());
        }

    }
}
