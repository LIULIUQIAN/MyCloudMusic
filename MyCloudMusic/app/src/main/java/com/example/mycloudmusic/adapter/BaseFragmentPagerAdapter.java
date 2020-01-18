package com.example.mycloudmusic.adapter;

import android.content.Context;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseFragmentPagerAdapter<T> extends FragmentStatePagerAdapter {

    protected final Context context;
    /**
     * 列表数据源
     */
    protected List<T> datum = new ArrayList<>();


    public BaseFragmentPagerAdapter(Context context, FragmentManager fm) {
        super(fm);
        this.context = context;
    }

    @Override
    public int getCount() {
        return datum.size();
    }

    public void setDatum(List<T> datum) {
        if (datum != null && datum.size() > 0){
            this.datum.clear();
            this.datum.addAll(datum);

            notifyDataSetChanged();
        }
    }

    /**
     * 添加数据
     *
     * @param datum
     */
    public void addDatum(List<T> datum) {
        if (datum != null && datum.size() > 0) {
            this.datum.addAll(datum);

            //通知数据改变了
            notifyDataSetChanged();
        }
    }

    /**
     * 获取当前位置的数据
     *
     * @param position
     * @return
     */
    public T getData(int position) {
        return datum.get(position);
    }
}
