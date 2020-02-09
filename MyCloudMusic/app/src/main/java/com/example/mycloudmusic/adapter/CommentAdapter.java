package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.TimeUtil;

import butterknife.BindView;
import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends BaseRecyclerViewAdapter<Comment,CommentAdapter.CommentViewHolder> {

    public CommentAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(context, inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        super.onBindViewHolder(holder,position);
        holder.bindData(getData(position));
    }

    public class CommentViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.iv_avatar)
        CircleImageView iv_avatar;

        @BindView(R.id.tv_nickname)
        TextView tv_nickname;

        @BindView(R.id.tv_time)
        TextView tv_time;

        @BindView(R.id.tv_like_count)
        TextView tv_like_count;

        @BindView(R.id.iv_like)
        ImageView iv_like;

        @BindView(R.id.tv_content)
        TextView tv_content;

        Context context;

        public CommentViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
        }

        public void bindData(Comment comment) {

            ImageUtil.showAvatar((Activity) context, iv_avatar, comment.getUser().getAvatar());
            tv_nickname.setText(comment.getUser().getNickname());
            tv_like_count.setText(String.valueOf(comment.getLikes_count()));
            iv_like.setImageResource(comment.isLiked() ? R.drawable.ic_comment_liked : R.drawable.ic_comment_like);
            tv_content.setText(comment.getContent());
            //时间
            tv_time.setText(TimeUtil.commonFormat(comment.getCreated_at()));
        }
    }
}
