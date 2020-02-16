package com.example.mycloudmusic.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.view.View;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.adapter.DownloadAdapter;
import com.example.mycloudmusic.adapter.DownloadedAdapter;
import com.example.mycloudmusic.fragment.DownloadedFragment;
import com.example.mycloudmusic.fragment.DownloadingFragment;

import net.lucode.hackware.magicindicator.MagicIndicator;
import net.lucode.hackware.magicindicator.ViewPagerHelper;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.CommonNavigator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.CommonNavigatorAdapter;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.abs.IPagerTitleView;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.indicators.LinePagerIndicator;
import net.lucode.hackware.magicindicator.buildins.commonnavigator.titles.SimplePagerTitleView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class DownloadActivity extends BaseTitleActivity {

    @BindView(R.id.view_pager)
    ViewPager view_pager;

    @BindView(R.id.magic_indicator)
    MagicIndicator magicIndicator;

    private DownloadAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_download);
    }

    @Override
    protected void initDatum() {
        super.initDatum();

        adapter = new DownloadAdapter(getMainActivity(), getSupportFragmentManager());
        view_pager.setAdapter(adapter);

        List<Fragment> list = new ArrayList<>();
        list.add(DownloadedFragment.newInstance());
        list.add(DownloadingFragment.newInstance());
        adapter.setDatum(list);


        CommonNavigator commonNavigator = new CommonNavigator(getMainActivity());
        commonNavigator.setAdapter(new CommonNavigatorAdapter() {
            @Override
            public int getCount() {
                return adapter.getCount();
            }

            @Override
            public IPagerTitleView getTitleView(Context context, int index) {
                SimplePagerTitleView titleView = new SimplePagerTitleView(context);
                titleView.setNormalColor(getResources().getColor(R.color.tab_normal));
                titleView.setSelectedColor(getResources().getColor(R.color.white));
                titleView.setText(adapter.getPageTitle(index));
                titleView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        view_pager.setCurrentItem(index,false);
                    }
                });
                return titleView;
            }

            @Override
            public IPagerIndicator getIndicator(Context context) {
                LinePagerIndicator indicator = new LinePagerIndicator(context);
                indicator.setMode(LinePagerIndicator.MODE_WRAP_CONTENT);
                indicator.setColors(Color.WHITE);

                return indicator;
            }
        });

        commonNavigator.setAdjustMode(true);
        magicIndicator.setNavigator(commonNavigator);
        ViewPagerHelper.bind(magicIndicator,view_pager);

    }
}
