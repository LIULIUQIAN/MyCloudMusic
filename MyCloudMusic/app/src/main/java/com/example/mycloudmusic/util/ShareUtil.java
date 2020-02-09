package com.example.mycloudmusic.util;

import android.content.Context;

import com.example.mycloudmusic.R;
import com.example.mycloudmusic.activity.BaseCommonActivity;
import com.example.mycloudmusic.domain.Song;

import cn.sharesdk.onekeyshare.OnekeyShare;

public class ShareUtil {
    public static void shareLyricText(Context context, Song data, String lyric) {

        //格式化url
        String url = String.format("http://dev-my-cloud-music-api-rails.ixuea.com/v1/songs/%s", data.getId());

        //分享的文本
        String shareContent = String.format("分享歌词：%s。分享%s的单曲《%s》：%s (来自@我的云音乐)",
                lyric,
                data.getSinger().getNickname(),
                data.getTitle(),
                url);

        //应用的名称
        String name = context.getString(R.string.app_name);

        //创建一键分享
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        // title标题，印象笔记、邮箱、信息、微信、人人网和QQ空间使用
        oks.setTitle(name);

        // text是分享文本，所有平台都需要这个字段
        oks.setText(shareContent);

        // url仅在微信（包括好友和朋友圈）中使用
        oks.setUrl(url);

        //启动分享
        oks.show(context);

    }

    public static void shareImage(Context context, String path) {

        //创建一键分享
        OnekeyShare oks = new OnekeyShare();

        //关闭sso授权
        oks.disableSSOWhenAuthorize();

        //imagePath是图片的本地路径
        //Linked-In以外的平台都支持此参数
        //确保sdcard下面存在此张图片
        oks.setImagePath(path);

        //分享
        oks.show(context);

    }
}
