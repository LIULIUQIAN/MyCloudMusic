package com.example.mycloudmusic.adapter;

import android.app.Activity;
import android.content.Context;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.TopicDetailActivity;
import com.example.mycloudmusic.activity.UserDetailActivity;
import com.example.mycloudmusic.domain.Comment;
import com.example.mycloudmusic.domain.MatchResult;
import com.example.mycloudmusic.domain.Video;
import com.example.mycloudmusic.listener.OnTagClickListener;
import com.example.mycloudmusic.util.ImageUtil;
import com.example.mycloudmusic.util.StringUtil;
import com.example.mycloudmusic.util.TimeUtil;

import butterknife.BindView;
import butterknife.OnClick;
import de.hdodenhof.circleimageview.CircleImageView;

import static com.example.mycloudmusic.util.Constant.TYPE_COMMENT;
import static com.example.mycloudmusic.util.Constant.TYPE_TITLE;
import static com.example.mycloudmusic.util.Constant.TYPE_VIDEO;

public class VideoDetailAdapter extends BaseRecyclerViewAdapter<Object, BaseRecyclerViewAdapter.ViewHolder> {

    public VideoDetailAdapter(Context context) {
        super(context);
    }

    @NonNull
    @Override
    public BaseRecyclerViewAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        if (viewType == TYPE_TITLE) {
            return new TitleViewHolder(inflater.inflate(R.layout.item_title_small, parent, false));
        } else if (viewType == TYPE_VIDEO) {
            return new VideoViewHolder(inflater.inflate(R.layout.item_video_list, parent, false));
        }

        return new VideoCommentViewHolder(context, inflater.inflate(R.layout.item_comment, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull BaseRecyclerViewAdapter.ViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);

        Object data = getData(position);
        holder.bindData(data);

    }

    @Override
    public int getItemViewType(int position) {
        Object data = getData(position);
        if (data instanceof String) {
            return TYPE_TITLE;
        } else if (data instanceof Video) {
            return TYPE_VIDEO;
        }
        return TYPE_COMMENT;
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

    public class VideoViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

        @BindView(R.id.iv_banner)
        ImageView iv_banner;

        @BindView(R.id.tv_title)
        TextView tv_title;

        @BindView(R.id.tv_info)
        TextView tv_info;

        public VideoViewHolder(@NonNull View itemView) {
            super(itemView);
        }

        @Override
        public void bindData(Object data) {
            super.bindData(data);

            Video video = (Video) data;
            ImageUtil.showAvatar((Activity) context, iv_banner, video.getBanner());
            tv_title.setText(video.getTitle());

            String timeString = TimeUtil.s2ms((int) video.getDuration());
            String str = context.getResources().getString(R.string.video_info, timeString, video.getUser().getNickname());
            tv_info.setText(str);

        }
    }

    public class VideoCommentViewHolder extends BaseRecyclerViewAdapter.ViewHolder {

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


        @BindView(R.id.reply_container)
        View reply_container;

        @BindView(R.id.tv_reply_content)
        TextView tv_reply_content;

        Context context;
        Comment data;

        @OnClick(R.id.iv_avatar)
        public void onAvatarClick() {

//            if (commentAdapterListener != null) {
//                commentAdapterListener.onAvatarClick(data);
//            }

        }

        @OnClick(R.id.ll_like_container)
        public void onLikeContainerClick() {
//            if (commentAdapterListener != null) {
//                commentAdapterListener.onLikeClick(data);
//            }
        }

        public VideoCommentViewHolder(Context context, @NonNull View itemView) {
            super(itemView);
            this.context = context;
        }

        @Override
        public void bindData(Object data) {
            super.bindData(data);

            Comment comment = (Comment) data;

            this.data = comment;

            ImageUtil.showAvatar((Activity) context, iv_avatar, comment.getUser().getAvatar());
            tv_nickname.setText(comment.getUser().getNickname());
            tv_like_count.setText(String.valueOf(comment.getLikes_count()));
            iv_like.setImageResource(comment.isLiked() ? R.drawable.ic_comment_liked : R.drawable.ic_comment_like);
            int colorid = comment.isLiked() ? R.color.colorPrimary : R.color.light_grey;
            tv_like_count.setTextColor(context.getResources().getColor(colorid));
            //            tv_content.setText(comment.getContent());
//            tv_content.setText(StringUtil.processHighlight(context, comment.getContent()));
            tv_content.setText(processContent(comment.getContent()));
            tv_content.setLinkTextColor(context.getResources().getColor(R.color.text_highlight));
            tv_content.setMovementMethod(LinkMovementMethod.getInstance());

            //时间
            tv_time.setText(TimeUtil.commonFormat(comment.getCreated_at()));

            //被回复的评论
            if (comment.getParent() == null) {
                reply_container.setVisibility(View.GONE);
            } else {
                reply_container.setVisibility(View.VISIBLE);

                tv_reply_content.setLinkTextColor(context.getResources().getColor(R.color.text_highlight));
                tv_reply_content.setMovementMethod(LinkMovementMethod.getInstance());
                String content = context.getString(R.string.reply_comment,
                        comment.getParent().getUser().getNickname(),
                        comment.getParent().getContent());
                tv_reply_content.setText(processContent(content));
            }

        }
    }

    /**
     * 处理文本点击事件
     */
    private SpannableString processContent(String content) {
        SpannableString spannableString = StringUtil.processContent(context, content,
                new OnTagClickListener() {
                    @Override
                    public void onTagClick(String data, MatchResult matchResult) {
                        String clickText = StringUtil.removePlaceholderString(data);
                        TopicDetailActivity.start((Activity) context, clickText);
                    }
                },
                new OnTagClickListener() {
                    @Override
                    public void onTagClick(String data, MatchResult matchResult) {
                        String clickText = StringUtil.removePlaceholderString(data);
                        UserDetailActivity.start((Activity) context, null, clickText);
                    }
                });

        return spannableString;
    }
}
