package com.example.mycloudmusic.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.listener.OnItemClickListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.ButterKnife;

public abstract class BaseRecyclerViewAdapter<D,VH extends BaseRecyclerViewAdapter.ViewHolder> extends RecyclerView.Adapter<VH> {


    protected final Context context;
    protected final LayoutInflater inflater;

    /**
     * 数据列表
     */
    private List<D> datum = new ArrayList<>();

    private OnItemClickListener onItemClickListener;

    public BaseRecyclerViewAdapter(Context context) {

        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {

        if (onItemClickListener != null){
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    onItemClickListener.onItemClick(holder,position);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    /**
     * 获取当前位置数据
     *
     * @param position
     * @return
     */
    public D getData(int position) {
        return datum.get(position);
    }


    /**
     * 设置数据
     */
    public void setDatum(List<D> datum) {
        this.datum.clear();
        this.datum.addAll(datum);
        notifyDataSetChanged();
    }

    /**
     * 获取数据
     */
    public List<D> getDatum() {
        return datum;
    }

    /**
     * 添加数据列表
     */
    public void addDatum(List<D> datum) {
        this.datum.addAll(datum);
        notifyDataSetChanged();
    }

    /**
     * 添加数据
     */
    public void addData(D data) {
        this.datum.add(data);
        notifyDataSetChanged();
    }

    /**
     * 删除指定位置的数据
     */
    public void removeData(int position) {
        datum.remove(position);
        notifyItemRemoved(position);
    }

    /**
     * 清除数据
     */
    public void clearData() {
        datum.clear();
        notifyDataSetChanged();
    }

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        this.onItemClickListener = onItemClickListener;
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            ButterKnife.bind(this,itemView);
        }

        /**
         * 绑定数据
         */
        public void bindData(D data) {

        }

    }
}
