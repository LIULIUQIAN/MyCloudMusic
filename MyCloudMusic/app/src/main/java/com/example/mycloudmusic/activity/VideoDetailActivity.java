package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.VideoView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.VideoDetailAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.TimeUtil;
import com.github.jdsjlzx.recyclerview.LRecyclerView;
import com.github.jdsjlzx.recyclerview.LRecyclerViewAdapter;
import com.google.android.material.appbar.AppBarLayout;
import com.zhy.view.flowlayout.FlowLayout;
import com.zhy.view.flowlayout.TagAdapter;
import com.zhy.view.flowlayout.TagFlowLayout;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import butterknife.BindView;
import butterknife.OnClick;

public class VideoDetailActivity extends BaseTitleActivity {

    @BindView(R.id.vv)
    VideoView vv;

    @BindView(R.id.abl)
    AppBarLayout abl;

    @BindView(R.id.control_container)
    LinearLayout control_container;

    @BindView(R.id.tv_start)
    TextView tv_start;

    @BindView(R.id.sb)
    SeekBar sb;

    @BindView(R.id.tv_end)
    TextView tv_end;

    @BindView(R.id.ib_screen)
    ImageButton ib_screen;

    @BindView(R.id.ib_play)
    ImageButton ib_play;

    @BindView(R.id.tv_info)
    TextView tv_info;

    @BindView(R.id.rv)
    LRecyclerView rv;
    private String id;
    private Video data;
    private CountDownTimer downTimer;
    private VideoDetailAdapter adapter;
    private DividerItemDecoration decoration;
    private LRecyclerViewAdapter adapterWrapper;
    private TextView tv_title;
    private TextView tv_created_at;
    private TextView tv_count;
    private TagFlowLayout fl;
    private ImageView iv_avatar;
    private TextView tv_nickname;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video_detail);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

        vv.stopPlayback();

    }

    @Override
    protected void initViews() {
        super.initViews();

        rv.setHasFixedSize(true);
        rv.setLayoutManager(new LinearLayoutManager(getMainActivity()));

        decoration = new DividerItemDecoration(getMainActivity(), DividerItemDecoration.VERTICAL);
        rv.addItemDecoration(decoration);

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        id = extraId();

        adapter = new VideoDetailAdapter(getMainActivity());
        adapterWrapper = new LRecyclerViewAdapter(adapter);
        rv.setAdapter(adapterWrapper);

        rv.setPullRefreshEnabled(false);
        rv.setLoadMoreEnabled(false);

        adapterWrapper.addHeaderView(createHeaderView());


        Api.getInstance().videoDetail(id).subscribe(new HttpObserver<DetailResponse<Video>>() {
            @Override
            public void onSucceeded(DetailResponse<Video> data) {
                next(data.getData());
            }
        });
    }

    private View createHeaderView() {

        View view = getLayoutInflater().inflate(R.layout.header_video_detail, (ViewGroup) rv.getParent(), false);
        tv_title = view.findViewById(R.id.tv_title);
        tv_created_at = view.findViewById(R.id.tv_created_at);
        tv_count = view.findViewById(R.id.tv_count);
        fl = view.findViewById(R.id.fl);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        return view;
    }

    @Override
    protected void initListeners() {
        super.initListeners();

        //视频准备播放
        vv.setOnPreparedListener(new MediaPlayer.OnPreparedListener() {
            @Override
            public void onPrepared(MediaPlayer mp) {
                sb.setMax(mp.getDuration());
                tv_end.setText(TimeUtil.ms2ms(mp.getDuration()));
            }
        });
        //视频播放完成
        vv.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {

                ib_play.setImageResource(R.drawable.ic_video_play);
                tv_info.setVisibility(View.VISIBLE);
            }
        });
    }

    /**
     * 显示数据
     */
    private void next(Video data) {
        this.data = data;

        setTitle(data.getTitle());

        tv_title.setText(data.getTitle());
        String createdAt = TimeUtil.yyyyMMddHHmm(data.getCreated_at());
        tv_created_at.setText(getResources().getString(R.string.video_created_at, createdAt));
        String clicksCount = getResources().getString(R.string.video_clicks_count, data.getClicks_count());
        tv_count.setText(getResources().getString(R.string.video_created_at, clicksCount));
        ImageUtil.showAvatar(getMainActivity(), iv_avatar, data.getUser().getAvatar());
        tv_nickname.setText(data.getUser().getNickname());

        ArrayList<String> tags = new ArrayList<>();
        tags.add("爱学啊");
        tags.add("测试");
        tags.add("测试标签1");
        tags.add("标签1");
        tags.add("测试标签1标签1");
        tags.add("标签1");


        fl.setAdapter(new TagAdapter<String>(tags) {

            @Override
            public View getView(FlowLayout parent, int position, String s) {

                View view = getLayoutInflater().inflate(R.layout.item_tag, null);
                TextView tv_title = view.findViewById(R.id.tv_title);
                tv_title.setText(s);
                return view;
            }
        });

        fl.setOnTagClickListener(new TagFlowLayout.OnTagClickListener() {
            @Override
            public boolean onTagClick(View view, int position, FlowLayout parent) {
                Log.e("setOnTagClickListener",tags.get(position));
                return true;
            }
        });


        String path = String.format(Constant.RESOURCE_ENDPOINT, data.getUri());

        vv.setVideoURI(Uri.parse(path));

        List<Object> datum = new ArrayList<>();
        datum.add("相关推荐");
        Api.getInstance().videos().subscribe(new HttpObserver<ListResponse<Video>>() {
            @Override
            public void onSucceeded(ListResponse<Video> data) {
                datum.addAll(data.getData());

                Api.getInstance().comments(new HashMap<>()).subscribe(new HttpObserver<ListResponse<Comment>>() {
                    @Override
                    public void onSucceeded(ListResponse<Comment> d) {
                        datum.add("精彩评论");
                        datum.addAll(d.getData());

                        adapter.setDatum(datum);
                    }
                });
            }
        });

    }

    @OnClick(R.id.ib_play)
    public void onPlayClick() {

        if (vv.isPlaying()) {

            pause();
        } else {
            resume();
        }

    }

    @OnClick(R.id.video_touch_container)
    public void onContainerClick() {

        if (ib_play.getVisibility() == View.VISIBLE) {
            //隐藏
            hideController();

        } else {
            //显示
            abl.setVisibility(View.VISIBLE);
            ib_play.setVisibility(View.VISIBLE);
            control_container.setVisibility(View.VISIBLE);

            startShowProgress();

        }

    }

    public void hideController() {
        abl.setVisibility(View.GONE);
        ib_play.setVisibility(View.GONE);
        control_container.setVisibility(View.GONE);

        cancelTask();
    }

    /**
     * 暂停
     */
    private void pause() {
        vv.pause();
        ib_play.setImageResource(R.drawable.ic_video_play);
    }

    /**
     * 播放
     */
    private void resume() {
        vv.start();
        ib_play.setImageResource(R.drawable.ic_video_pause);

        tv_info.setVisibility(View.GONE);

        startShowProgress();

    }

    /**
     * 开始显示进度
     */
    private void startShowProgress() {

        cancelTask();

        downTimer = new CountDownTimer(5000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                tv_start.setText(TimeUtil.ms2ms(vv.getCurrentPosition()));
                sb.setProgress(vv.getCurrentPosition());
            }

            @Override
            public void onFinish() {
                hideController();
            }
        };

        downTimer.start();

    }

    private void cancelTask() {
        if (downTimer != null) {
            downTimer.cancel();
            downTimer = null;
        }
    }

}
