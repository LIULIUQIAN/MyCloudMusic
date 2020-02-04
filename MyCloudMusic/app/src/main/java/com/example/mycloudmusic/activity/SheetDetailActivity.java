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
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
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
    /**
     * 头部容器
     */
    private LinearLayout ll_header;

    /**
     * 收藏按钮
     */
    private Button bt_collection;


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

        if (data.isCollection()){
            bt_collection.setText(getResources().getString(R.string.cancel_collection,data.getCollections_count()));
            bt_collection.setBackground(null);
            bt_collection.setTextColor(getResources().getColor(R.color.light_grey));
        }else {
            bt_collection.setText(getResources().getString(R.string.collection,data.getCollections_count()));
            bt_collection.setBackgroundResource(R.drawable.selector_color_primary);
            bt_collection.setTextColor(getResources().getColorStateList(R.drawable.selector_text_color_primary_reverse));
        }
    }
}
