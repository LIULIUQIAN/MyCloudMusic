package com.example.mycloudmusic.adapter;

import android.text.TextUtils;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Feed;
import com.example.mycloudmusic.domain.Resource;
import com.example.mycloudmusic.listener.FeedListener;
import com.example.mycloudmusic.util.Constant;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.TimeUtil;
import com.google.common.base.Function;
import com.google.common.collect.Lists;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.List;

public class FeedAdapter extends BaseQuickAdapter<Feed, BaseViewHolder> {

    private final FeedListener feedListener;

    public FeedAdapter(int layoutResId, FeedListener feedListener) {
        super(layoutResId);
        this.feedListener = feedListener;
    }

    @Override
    protected void convert(@NonNull BaseViewHolder helper, Feed item) {

        ImageUtil.showAvatar(mContext, helper.getView(R.id.iv_avatar), item.getUser().getAvatar());
        helper.setText(R.id.tv_nickname, item.getUser().getNickname());

        helper.setText(R.id.tv_time, TimeUtil.commonFormat(item.getCreated_at()));

        StringBuilder builder = new StringBuilder();
        builder.append(item.getContent());
        if (!TextUtils.isEmpty(item.getCity())) {
            builder.append("\n 来自：");
            builder.append(item.getProvince());
            builder.append(item.getCity());
        }
        helper.setText(R.id.tv_content, builder.toString());

        //显示图片
        RecyclerView recyclerView = helper.getView(R.id.recycler_view);
        if (item.getImages() != null && item.getImages().size() > 0) {
            recyclerView.setVisibility(View.VISIBLE);
            recyclerView.setHasFixedSize(true);
            recyclerView.setNestedScrollingEnabled(false);
            int spanCount = 3;
            if (item.getImages().size() == 1){
                spanCount = 2;
            }
            GridLayoutManager layoutManager = new GridLayoutManager(mContext, spanCount);
            recyclerView.setLayoutManager(layoutManager);

            ImageAdapter adapter = new ImageAdapter(R.layout.item_image);
            recyclerView.setAdapter(adapter);
            adapter.replaceData(item.getImages());

            adapter.setOnItemClickListener(new OnItemClickListener() {
                @Override
                public void onItemClick(BaseQuickAdapter adapter, View view, int position) {

                    List<String> imagUrls = Lists.transform(item.getImages(), new Function<Resource, String>() {
                        @NullableDecl
                        @Override
                        public String apply(@NullableDecl Resource input) {

                            return String.format(Constant.RESOURCE_ENDPOINT, input.getUri());
                        }
                    });

                    feedListener.onImageClick(recyclerView,imagUrls,position);

                }
            });

        } else {
            recyclerView.setVisibility(View.GONE);
        }
    }
}
