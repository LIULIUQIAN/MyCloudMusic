package com.example.mycloudmusic.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.palette.graphics.Palette;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.media.MediaPlayer;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.SongAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.TimeUtil;
import com.example.mycloudmusic.domain.response.DetailResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.listener.MusicPlayerListener;
import com.example.mycloudmusic.manager.ListManager;
import com.example.mycloudmusic.manager.MusicPlayerManager;
import com.example.mycloudmusic.service.MusicPlayerService;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.ToastUtil;

import butterknife.BindView;
import butterknife.OnClick;
import retrofit2.Response;

import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_LIST;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_ONE;
import static com.example.mycloudmusic.util.Constant.MODEL_LOOP_RANDOM;

public class SheetDetailActivity extends BaseTitleActivity implements View.OnClickListener, MusicPlayerListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private ImageView iv_banner;
    private TextView tv_title;
    private ImageView iv_avatar;
    private TextView tv_nickname;
    private TextView tv_count;

    ///
    /**
     * 迷你播放控制器 容器
     */
    @BindView(R.id.ll_play_control_small)
    LinearLayout ll_play_control_small;

    /**
     * 迷你播放控制器 封面
     */
    @BindView(R.id.iv_banner_small_control)
    ImageView iv_banner_small_control;

    /**
     * 迷你播放控制器 标题
     */
    @BindView(R.id.tv_title_small_control)
    TextView tv_title_small_control;

    /**
     * 迷你播放控制器 歌词控件
     */
    @BindView(R.id.llv_small_control)
    TextView llv;

    /**
     * 迷你播放控制器 播放暂停按钮
     */
    @BindView(R.id.iv_play_small_control)
    ImageView iv_play_small_control;

    /**
     * 迷你播放控制器 进度条
     */
    @BindView(R.id.pb_progress_small_control)
    ProgressBar pb_progress_small_control;
    ///
    /**
     * 评论容器
     */
    private View ll_comment_container;

    /**
     * 评论数
     */
    private TextView tv_comment_count;
    /**
     * 头部容器
     */
    private LinearLayout ll_header;

    /**
     * 收藏按钮
     */
    private Button bt_collection;

    private LinearLayout ll_user;


    //歌单详情数据
    private Sheet data;

    private SongAdapter adapter;
    private ListManager listManager;
    private MusicPlayerManager musicPlayerManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sheet_detail);
    }

    /*
     * 界面显示
     * */
    @Override
    protected void onResume() {
        super.onResume();

        musicPlayerManager.addMusicPlayerListener(this);
        //显示迷你播放控制器数据
        showSmallPlayControlData();
    }

    @Override
    protected void onPause() {
        super.onPause();
        musicPlayerManager.removeMusicPlayerListener(this);
    }

    @Override
    protected void initViews() {
        super.initViews();

        recyclerView.setLayoutManager(new LinearLayoutManager(getMainActivity()));
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        musicPlayerManager = MusicPlayerService.getMusicPlayerManager(getApplicationContext());
        listManager = MusicPlayerService.getListManager(getApplicationContext());
        musicPlayerManager.addMusicPlayerListener(this);

        adapter = new SongAdapter(R.layout.item_song_detail);
        adapter.setHeaderView(createHeaderView());
        recyclerView.setAdapter(adapter);

        fetchData();

        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {
//                SimplePlayerActivity.start(getMainActivity());
                play(position);
            }
        });
    }

    /**
     * 播放当前位置的音乐
     */
    private void play(int position) {
        Song item = adapter.getItem(position);
        listManager.setDatum(adapter.getData());
        listManager.play(item);

        SimplePlayerActivity.start(getMainActivity());
    }


    /*
     * 头部 view
     * */
    private View createHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_sheet_detail, (ViewGroup) recyclerView.getParent(), false);

        ll_header = view.findViewById(R.id.ll_header);
        iv_banner = view.findViewById(R.id.iv_banner);
        tv_title = view.findViewById(R.id.tv_title);
        iv_avatar = view.findViewById(R.id.iv_avatar);
        tv_nickname = view.findViewById(R.id.tv_nickname);
        tv_count = view.findViewById(R.id.tv_count);
        //评论容器
        ll_comment_container = view.findViewById(R.id.ll_comment_container);
        //评论数据
        tv_comment_count = view.findViewById(R.id.tv_comment_count);
        bt_collection = view.findViewById(R.id.bt_collection);
        ll_user = view.findViewById(R.id.ll_user);
        bt_collection.setOnClickListener(this);
        ll_comment_container.setOnClickListener(this);
        ll_user.setOnClickListener(this);
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
            tv_count.setText(getResources().getString(R.string.music_count, sheet.getSongs().size()));
        } else {
            tv_count.setText(getResources().getString(R.string.music_count, 0));
        }

//        ImageUtil.showAvatar(this, iv_banner, sheet.getBanner());
        tv_title.setText(sheet.getTitle());
        ImageUtil.showCircleAvatar(this, iv_avatar, sheet.getUser().getAvatar());
        tv_nickname.setText(sheet.getUser().getNickname());
        tv_comment_count.setText(String.valueOf(sheet.getComments_count()));

        showCollectionStatus();

        if (!TextUtils.isEmpty(sheet.getBanner())) {
            //获取图片绝对路径
            String uri = String.format(Constant.RESOURCE_ENDPOINT, sheet.getBanner());
            Glide.with(this).asBitmap().load(uri).into(new CustomTarget<Bitmap>() {
                @Override
                public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                    iv_banner.setImageBitmap(resource);

                    Palette.from(resource).generate(new Palette.PaletteAsyncListener() {
                        @Override
                        public void onGenerated(@Nullable Palette palette) {
                            Palette.Swatch swatch = palette.getVibrantSwatch();
                            if (swatch != null) {
                                int rgb = swatch.getRgb();
                                toolbar.setBackgroundColor(rgb);
                                ll_header.setBackgroundColor(rgb);

                                Window window = getWindow();
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                    window.setNavigationBarColor(rgb);
                                    window.setStatusBarColor(rgb);
                                }

                            }
                        }
                    });
                }

                @Override
                public void onLoadCleared(@Nullable Drawable placeholder) {

                }
            });
        }


    }

    /**
     * 显示收藏状态
     */

    @SuppressLint("ResourceType")
    private void showCollectionStatus() {

        if (data.isCollection()) {
            bt_collection.setText(getResources().getString(R.string.cancel_collection, data.getCollections_count()));
            bt_collection.setBackground(null);
            bt_collection.setTextColor(getResources().getColor(R.color.light_grey));
        } else {
            bt_collection.setText(getResources().getString(R.string.collection, data.getCollections_count()));
            bt_collection.setBackgroundResource(R.drawable.selector_color_primary);
            bt_collection.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary_reverse));
        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_collection:
                processCollectionClick();
                break;
            case R.id.ll_comment_container:
                CommentActivity.start(getMainActivity(), extraId());
                break;
            case R.id.ll_user:
                startActivityExtraId(UserDetailActivity.class, data.getUser().getId());
                break;

        }
    }

    /**
     * 处理收藏和取消收藏逻辑
     */
    private void processCollectionClick() {

        if (data.isCollection()) {
            Api.getInstance().deleteCollect(extraId()).subscribe(new HttpObserver<Response<Void>>() {
                @Override
                public void onSucceeded(Response<Void> item) {
                    ToastUtil.successShortToast(R.string.cancel_success);
                    data.setCollection_id(null);
                    data.setCollections_count(data.getCollections_count() - 1);
                    showCollectionStatus();
                }
            });
        } else {
            Api.getInstance().collect(extraId()).subscribe(new HttpObserver<Response<Void>>() {
                @Override
                public void onSucceeded(Response<Void> item) {
                    ToastUtil.successShortToast(R.string.collection_success);
                    data.setCollection_id(1);
                    data.setCollections_count(data.getCollections_count() + 1);
                    showCollectionStatus();
                }
            });
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_sheet_detail, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.action_search) {
            System.out.println("============1");
            return true;
        } else if (id == R.id.action_sort) {
            System.out.println("============2");
            return true;
        } else if (id == R.id.action_report) {
            System.out.println("============3");
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    /////////////////////////////////////////////////////////////////

    /**
     * 迷你播放控制器 容器点击
     */
    @OnClick(R.id.ll_play_control_small)
    public void onPlayControlSmallClick() {
        SimplePlayerActivity.start(getMainActivity());
    }

    /**
     * 迷你播放控制器 播放暂停按钮点击
     */
    @OnClick(R.id.iv_play_small_control)
    public void onPlaySmallClick() {

        if (musicPlayerManager.isPlaying()){
            listManager.pause();
        }else {
            listManager.resume();
        }
    }

    /**
     * 迷你播放控制器 下一曲按钮点击
     */
    @OnClick(R.id.iv_next_small_control)
    public void onNextSmallClick() {

        listManager.play(listManager.next());
    }

    /**
     * 迷你播放控制器 播放列表按钮点击
     */
    @OnClick(R.id.iv_list_small_control)
    public void onListSmallClick() {
        System.out.println("onListSmallClick");

    }

    /*
     * 显示迷你播放控制器数据
     * */
    private void showSmallPlayControlData() {

        if (listManager.getDatum() != null && listManager.getDatum().size() > 0) {
            ll_play_control_small.setVisibility(View.VISIBLE);
            Song data = listManager.getData();
            if (data == null) {
                return;
            }
            showInitData(data);
            showDuration(data);
            showProgress(data);
            showMusicPlayStatus();

        }else {
            ll_play_control_small.setVisibility(View.GONE);
        }

    }

    /*
    * 显示播放状态
    * */
    private void showMusicPlayStatus() {
        iv_play_small_control.setSelected(musicPlayerManager.isPlaying());
    }

    /*
    * 显示播放进度
    * */
    private void showProgress(Song data) {
        pb_progress_small_control.setProgress((int) data.getProgress());
    }

    /*
    * 显示音乐时长
    * */
    private void showDuration(Song data) {
        pb_progress_small_control.setMax((int) data.getDuration());
    }

    /*
     * 显示初始化数据
     * */
    private void showInitData(Song data) {
        ImageUtil.showAvatar(getMainActivity(), iv_banner_small_control, data.getBanner());
        tv_title_small_control.setText(data.getTitle());

    }

    //音乐播放器回调
    @Override
    public void onPaused(Song data) {
        showMusicPlayStatus();
    }

    @Override
    public void onPlaying(Song data) {
        showMusicPlayStatus();
        showInitData(data);
    }

    @Override
    public void onPrepared(MediaPlayer mp, Song data) {
        showDuration(data);
    }

    @Override
    public void onProgress(Song data) {
        showProgress(data);
    }

    //end音乐播放器回调


}
