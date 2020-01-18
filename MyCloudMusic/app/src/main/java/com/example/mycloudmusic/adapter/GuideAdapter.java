package com.example.mycloudmusic.adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.fragment.GuideFragment;

import java.util.ArrayList;
import java.util.List;

public class GuideAdapter extends FragmentPagerAdapter {

    /**
     * 列表数据源
     */
    protected List<Integer> datum = new ArrayList<>();

    public GuideAdapter(FragmentManager fm) {
        super(fm);
    }

    @Override
    public Fragment getItem(int position) {
        return GuideFragment.newInstance(datum.get(position));
    }

    @Override
    public int getCount() {
        return datum.size();
    }

    public void setDatum(List<Integer> datum) {
        if (datum != null && datum.size() > 0){
            this.datum.clear();
            this.datum.addAll(datum);

            notifyDataSetChanged();
        }
    }
}
