package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SongAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;

import butterknife.BindView;

public class SheetDetailActivity extends BaseTitleActivity {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageView iv_banner;
    private TextView tv_title;
    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_count;
    /**
     * 评论容器
     */
    private View ll_comment_container;

    /**
     * 评论数
     */
    private TextView tv_comment_count;


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
        adapter.setHeaderView(createHeaderView());
        recyclerView.setAdapter(adapter);

        fetchData();
    }

    /*
     * 头部 view
     * */
    private View createHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_sheet_detail, (ViewGroup) recyclerView.getParent(), false);

        iv_banner = view.findViewById(R.id.iv_banner);
        tv_title = view.findViewById(R.id.tv_title);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_count = view.findViewById(R.id.tv_count);
        //评论容器
        ll_comment_container = view.findViewById(R.id.ll_comment_container);
        //评论数据
        tv_comment_count = view.findViewById(R.id.tv_comment_count);

        return view;
    }

    /*
     * 请求歌单详情数据
     * */
    private void fetchData() {
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
    private void next(Sheet sheet) {
        this.data = sheet;

        if (sheet.getSongs() != null && sheet.getSongs().size() > 0) {
            adapter.replaceData(sheet.getSongs());
            tv_count.setText(getResources().getString(R.string.music_count,sheet.getSongs().size()));
        }else {
            tv_count.setText(getResources().getString(R.string.music_count,0));
        }

        ImageUtil.showAvatar(this, iv_banner, sheet.getBanner());
        tv_title.setText(sheet.getTitle());
        ImageUtil.showCircleAvatar(this, iv_avatar, sheet.getUser().getAvatar());
        tv_nickname.setText(sheet.getUser().getNickname());
        tv_comment_count.setText(String.valueOf(sheet.getComments_count()));


    }
}
