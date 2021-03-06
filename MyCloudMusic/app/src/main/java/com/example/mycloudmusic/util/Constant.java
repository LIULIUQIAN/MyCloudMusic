package com.example.mycloudmusic.util;

import android.os.Bundle;
import android.provider.MediaStore;

import com.example.mycloudmusic.BuildConfig;

public class Constant {

    public static final String ENDPOINT = BuildConfig.ENDPOINT;

    public static final String RESOURCE_ENDPOINT = BuildConfig.RESOURCE_ENDPOINT;

    /**
     * 二维码地址
     */
    public static final String QRCODE_URL = String.format("%s/v1/monitors/version?u=", ENDPOINT);


    public static final String ID = "ID";

    /**
     * int值id
     */
    public static final String INT = "int";


    /**
     * 手机号正则表达式
     * 移动：134 135 136 137 138 139 147 150 151 152 157 158 159 178 182 183 184 187 188 198
     * 联通：130 131 132 145 155 156 166 171 175 176 185 186
     * 电信：133 149 153 173 177 180 181 189 199
     * 虚拟运营商: 170
     */
    public static final String REGEX_PHONE = "^(13[0-9]|14[579]|15[0-3,5-9]|16[6]|17[0135678]|18[0-9]|19[89])\\d{8}$";

    /**
     * 邮箱正则表达式
     */
    public static final String REGEX_EMAIL = "^([a-z0-9_\\.-]+)@([\\da-z\\.-]+)\\.([a-z\\.]{2,6})$";
    /**
     * 传递data key
     */
    public static final String DATA = "DATA";
    public static final String TITLE = "TITLE";
    public static final String URL = "URL";
    /*
    * 广告点击
    * */
    public static final String ACTION_AD = "com.example.mycloudmusic.ACTION_AD";
    /**
     * 用户详情昵称查询字段
     */
    public static final String NICKNAME = "nickname";

    /**
     * 标题
     */
    public static final int TYPE_TITLE = 0;

    /**
     * 歌单
     */
    public static final int TYPE_SHEET = 1;

    /**
     * 单曲
     */
    public static final int TYPE_SONG = 2;

    /**
     * 用户
     */
    public static final int TYPE_USER = 3;

    /**
     * 评论
     */
    public static final int TYPE_COMMENT = 4;

    /**
     * 视频
     */
    public static final int TYPE_VIDEO = 5;

    /**
     * 歌单 id
     */
    public static final String SHEET_ID = "SHEET_ID";

    /**
     * 播放进度通知
     */
    public static final int MESSAGE_PROGRESS = 0;


    /**
     * 列表循环
     */
    public static final int MODEL_LOOP_LIST = 0;

    /**
     * 单曲循环
     */
    public static final int MODEL_LOOP_ONE = 1;

    /**
     * 随机循环
     */
    public static final int MODEL_LOOP_RANDOM = 2;

    /**
     * 歌单
     */
    public static final String SHEET = "SHEET";

    /**
     * 音乐
     */
    public static final String SONG = "SONG";

    /**
     * 音乐播放通知id
     */
    public static final int NOTIFICATION_MUSIC_ID = 10000;
    /**
     * 解锁全局歌词Id
     */
    public static final int NOTIFICATION_UNLOCK_LYRIC_ID = 10001;

    /**
     * 音乐播放通知-播放
     */
    public static final String ACTION_PLAY = "com.example.mycloudmusic.ACTION_PLAY";
    public static final String ACTION_PREVIOUS = "com.example.mycloudmusic.ACTION_PREVIOUS";
    public static final String ACTION_NEXT = "com.example.mycloudmusic.ACTION_NEXT";
    public static final String ACTION_LIKE = "com.example.mycloudmusic.ACTION_LIKE";
    public static final String ACTION_LYRIC = "com.example.mycloudmusic.ACTION_LYRIC";
    public static final String ACTION_UNLOCK_LYRIC = "com.example.mycloudmusic.ACTION_UNLOCK_LYRIC";

    /**
     * 音乐播放通知 点击
     */
    public static final String ACTION_MUSIC_PLAY_CLICK = "com.example.mycloudmusic.ACTION_MUSIC_PLAY_CLICK";

    /**
     * 保持播放进度间隔（毫秒）
     */
    public static final int SAVE_PROGRESS_TIME = 1000;

    //黑胶唱片指针旋转
    /**
     * 黑胶唱片指针暂停的角度
     */
    public static final float THUMB_ROTATION_PAUSE = -25F;

    /**
     * 黑胶唱片指针播放的角度
     */
    public static final float THUMB_ROTATION_PLAY = 0F;

    /**
     * 黑胶唱片指针动画时间
     */
    public static final long THUMB_DURATION = 300;

    /**
     * LRC歌词
     */
    public static final int LRC = 0;

    /**
     * KSC歌词
     */
    public static final int KSC = 10;

    /**
     * 隐藏歌词拖拽时间
     */
    public static final long LYRIC_HIDE_DRAG_TIME = 4000;

    /**
     * 请求获取overlay权限请求码
     */
    public static final int REQUEST_OVERLAY_PERMISSION = 100;

    /**
     * mention开始
     */
    public static final String MENTION = "@";

    /**
     * hashTag开始
     */
    public static final String HAST_TAG = "#";

    public static final String MEDIA_AUDIO_SELECTION = MediaStore.Audio.AudioColumns.IS_MUSIC + " != 0 AND " +
            MediaStore.Audio.AudioColumns.SIZE + " >= ? AND " +
            MediaStore.Audio.AudioColumns.DURATION + " >= ?";

    /**
     * 1M
     */
    public static final int MUSIC_FILTER_SIZE = 1 * 1024 * 1024;

    /**
     * 60s
     */
    public static final int MUSIC_FILTER_DURATION = 60 * 1000;

    /**
     * 扫描本地音乐放大镜圆周半径
     */
    public static final double DEFAULT_RADIUS = 30;

    /**
     * 阿里云OSS Bucket
     */
    public static final String ALIYUN_OSS_BUCKET_NAME = "dev-courses-misuc";

    /**
     * 阿里云OSS AK
     */
    public static final String ALIYUN_OSS_AK = "LTAIvvrRFHYlwoEj";

    /**
     * 阿里云OSS SK
     */
    public static final String ALIYUN_OSS_SK = "xDpTSeBM0PqDUWtlVPQfTfMb97CbPD";
}
