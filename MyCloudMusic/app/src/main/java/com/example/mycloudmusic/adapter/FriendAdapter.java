package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.util.ImageUtil;

import butterknife.BindView;

import static com.example.mycloudmusic.util.Constant.TYPE_TITLE;
import static com.example.mycloudmusic.util.Constant.TYPE_USER;


public class FriendAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewAdapter.ViewHolder> {

    public FriendAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == TYPE_TITLE) {
            return new TitleViewHolder(inflater.inflate(R.layout.item_title_small, parent, false));
        } else {
            return new UserViewHolder(inflater.inflate(R.layout.item_user, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        holder.bindData(getData(position));
    }

    @Override
    public int getItemViewType(int position) {

        Object data = getData(position);
        if (data instanceof String) {
            return TYPE_TITLE;
        }
        return TYPE_USER;
    }

    public class TitleViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        public TitleViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {
            super.bindData(data);

            tv_title.setText((String) data);
        }
    }

    public class UserViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_info)
        TextView tv_info;

        @BindView(R.id.iv_banner)
        ImageView iv_banner;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {
            super.bindData(data);

            User item = (User) data;
            tv_title.setText(item.getNickname());
            tv_info.setText(item.getDescription());
            ImageUtil.showAvatar((Activity) context, iv_banner, item.getAvatar());

        }
    }

}
