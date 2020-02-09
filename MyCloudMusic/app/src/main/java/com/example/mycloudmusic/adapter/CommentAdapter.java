package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.TimeUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.zip.Inflater;

import de.hdodenhof.circleimageview.CircleImageView;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    private final Context context;
    List<Comment> datum = new ArrayList<>();
    private final LayoutInflater inflater;

    public CommentAdapter(Context context) {
        this.context = context;
        inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new CommentViewHolder(context, inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull CommentViewHolder holder, int position) {
        holder.bindData(datum.get(position));
    }

    @Override
    public int getItemCount() {
        return datum.size();
    }

    public void setDatum(List<Comment> datum) {
        this.datum.clear();
        this.datum.addAll(datum);
        notifyDataSetChanged();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        private final CircleImageView iv_avatar;
        private final TextView tv_nickname;
        private final TextView tv_time;
        private final TextView tv_like_count;
        private final ImageView iv_like;
        private final TextView tv_content;
        private final Context context;

        public CommentViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
            iv_avatar = itemView.findViewById(R.id.iv_avatar);
            tv_nickname = itemView.findViewById(R.id.tv_nickname);
            tv_time = itemView.findViewById(R.id.tv_time);
            tv_like_count = itemView.findViewById(R.id.tv_like_count);
            iv_like = itemView.findViewById(R.id.iv_like);
            tv_content = itemView.findViewById(R.id.tv_content);
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
