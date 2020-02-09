package com.example.mycloudmusic.listener;

import com.example.mycloudmusic.domain.Comment;

public interface CommentAdapterListener {

    /**
     * 头像点击了
     */
    void onAvatarClick(Comment data);

    /**
     * 点赞回调
     */
    void onLikeClick(Comment data);
}
