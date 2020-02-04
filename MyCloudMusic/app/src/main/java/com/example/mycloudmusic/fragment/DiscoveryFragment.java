package com.example.mycloudmusic.fragment;

import android.content.Context;
import android.content.Intent;
import android.icu.util.Calendar;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.SheetDetailActivity;
import com.example.mycloudmusic.activity.WebViewActivity;
import com.example.mycloudmusic.adapter.DiscoveryAdapter;
import com.example.mycloudmusic.api.Api;
import com.example.mycloudmusic.domain.Advert;
import com.example.mycloudmusic.domain.BaseMultiItemEntity;
import com.example.mycloudmusic.domain.Sheet;
import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.Title;
import com.example.mycloudmusic.domain.response.ListResponse;
import com.example.mycloudmusic.listener.HttpObserver;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;
import com.youth.banner.loader.ImageLoader;
import com.youth.banner.loader.ImageLoaderInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DiscoveryFragment extends BaseCommonFragment implements OnBannerListener {

    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;

    private Banner banner;

    private DiscoveryAdapter adapter;

    /**
     * 轮播图数据
     */
    private List<Advert> bannerData;

    public static DiscoveryFragment newInstance() {

        DiscoveryFragment fragment = new DiscoveryFragment();
        Bundle args = new Bundle();
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    protected View getLayoutView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_discovery, null);
    }

    /**
     * 当前界面展示了
     */
    @Override
    public void onResume() {
        super.onResume();

        if (bannerData != null) {
            banner.startAutoPlay();
        }
    }

    /**
     * 当界面看不见了执行
     * <p>
     * 包括开启新界面，弹窗，后台
     */
    @Override
    public void onPause() {
        super.onPause();

        //结束轮播图滚动
        banner.stopAutoPlay();
    }


    @Override
    protected void initViews() {
        super.initViews();

        GridLayoutManager layoutManager = new GridLayoutManager(getMainActivity(), 3);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setHasFixedSize(true);
    }

    /**
     * 创建头部布局
     *
     * @return
     */
    private View createHeaderView() {
        View view = getLayoutInflater().inflate(R.layout.header_discovery, (ViewGroup) recyclerView.getParent(), false);

        TextView tv_day = view.findViewById(R.id.tv_day);

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            Calendar calendar = Calendar.getInstance();
            int day = calendar.get(Calendar.DAY_OF_MONTH);
            tv_day.setText(String.valueOf(day));
        }

        banner = view.findViewById(R.id.banner);
        banner.setOnBannerListener(this);
        banner.setImageLoader(new GlideImageLoader());

        return view;

    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new DiscoveryAdapter();
        //设置列宽度
        adapter.setSpanSizeLookup((gridLayoutManager, i) -> adapter.getItem(i).getSpanSize());
        adapter.setHeaderView(createHeaderView());
        recyclerView.setAdapter(adapter);

        fetchData();
        fetchBannerData();

    }

    @Override
    protected void initListeners() {
        super.initListeners();
        adapter.setOnItemClickListener(new BaseQuickAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                Object item = adapter.getItem(position);
                if (item instanceof Sheet){
                    Sheet sheet = (Sheet) item;
                    Intent intent = new Intent(getMainActivity(), SheetDetailActivity.class);
                    intent.putExtra(Constant.ID,sheet.getId());
                    startActivity(intent);
                }
            }
        });
    }

    /*
     * 请求轮播图数据
     * */
    private void fetchBannerData() {
        Api.getInstance().adverts().subscribe(new HttpObserver<ListResponse<Advert>>() {
            @Override
            public void onSucceeded(ListResponse<Advert> data) {
                bannerData = data.getData();

                banner.setImages(bannerData);
                banner.start();
                banner.startAutoPlay();

            }
        });
    }

    /*
     * 请求数据
     * */
    private void fetchData() {

        List<BaseMultiItemEntity> datum = new ArrayList<>();
        datum.add(new Title("推荐歌单"));

        //添加歌单数据
        Api.getInstance().sheets()
                .subscribe(new HttpObserver<ListResponse<Sheet>>() {
                    @Override
                    public void onSucceeded(ListResponse<Sheet> data) {
                        datum.addAll(data.getData());

                        Api.getInstance().songs()
                                .subscribe(new HttpObserver<ListResponse<Song>>() {
                                    @Override
                                    public void onSucceeded(ListResponse<Song> data) {
                                        datum.add(new Title("推荐单曲"));
                                        datum.addAll(data.getData());

                                        adapter.replaceData(datum);
                                    }
                                });
                    }
                });

    }

    /*
     * banner点击事件
     * */
    @Override
    public void OnBannerClick(int position) {
        Advert advert = bannerData.get(position);
        WebViewActivity.start(getMainActivity(), advert.getTitle(), "https://www.baidu.com");
    }

    /**
     * Banner框架显示图片的实现类
     */
    private class GlideImageLoader extends ImageLoader {

        @Override
        public void displayImage(Context context, Object path, ImageView imageView) {
            Advert advert = (Advert) path;
            ImageUtil.showAvatar(getMainActivity(), imageView, advert.getBanner());
        }
    }
}
