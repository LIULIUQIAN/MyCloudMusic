package com.example.mycloudmusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.widget.RemoteViews;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.NotificationCompat;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.mycloudmusic.R;
import com.example.mycloudmusic.domain.Song;

import static com.example.mycloudmusic.util.Constant.NOTIFICATION_MUSIC_ID;

public class NotificationUtil {

    /**
     * 通知渠道
     */
    private static final String IMPORTANCE_LOW_CHANNEL_ID = "IMPORTANCE_LOW_CHANNEL_ID";
    private static NotificationManager notificationManager;

    /**
     * 获取一个设置service为前台的通知
     * 测试通知
     */

    public static Notification getServiceForeground(Context context) {
        //渠道是8.0中新增的

        getNotificationManager(context);

        createNotificationChannel();

        Notification notification = new NotificationCompat.Builder(context, IMPORTANCE_LOW_CHANNEL_ID)
                .setContentText("通知内容")
                .setContentTitle("通知标题")
                .setSmallIcon(R.mipmap.ic_launcher)
                .setLargeIcon(BitmapFactory.decodeResource(context.getResources(), R.mipmap.ic_launcher))
                .build();
        return notification;
    }

    /**
     * 获取通知管理器
     */
    private static void getNotificationManager(Context context) {
        if (notificationManager == null) {
            notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
        }
    }

    /*
     * 创建通知渠道
     * */
    private static void createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel channel = new NotificationChannel(IMPORTANCE_LOW_CHANNEL_ID, "重要通知", NotificationManager.IMPORTANCE_LOW);
            notificationManager.createNotificationChannel(channel);
        }
    }

    /**
     * 显示通知
     */
    public static void showNotification(int id, Notification notification) {
        notificationManager.notify(id, notification);
    }

    /**
     * 显示音乐通知
     */
    public static void showMusicNotification(Context context, Song data, boolean isPlaying) {

        String url = String.format(Constant.RESOURCE_ENDPOINT, data.getBanner());
        RequestOptions options = ImageUtil.getCommonRequestOptions();

        Glide.with(context)
                .asBitmap()
                .load(url)
                .apply(options)
                .into(new CustomTarget<Bitmap>() {
                    @Override
                    public void onResourceReady(@NonNull Bitmap resource, @Nullable Transition<? super Bitmap> transition) {
                        showMusicNotification(context, data, isPlaying, resource);
                    }

                    @Override
                    public void onLoadCleared(@Nullable Drawable placeholder) {

                    }
                });


    }

    /**
     * 显示音乐通知
     */
    public static void showMusicNotification(Context context, Song data, boolean isPlaying, Bitmap banner) {

        createNotificationChannel();

        RemoteViews contentView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play);
        RemoteViews contentBigView = new RemoteViews(context.getPackageName(), R.layout.notification_music_play_large);

        setData(data, contentView, isPlaying, banner);
        setData(data, contentBigView, isPlaying, banner);

        //点赞
        PendingIntent likePendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_LIKE.hashCode(),
                new Intent(Constant.ACTION_LIKE),
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentBigView.setOnClickPendingIntent(R.id.iv_like, likePendingIntent);
        //上一曲
        PendingIntent previousPendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_PREVIOUS.hashCode(),
                new Intent(Constant.ACTION_PREVIOUS),
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentBigView.setOnClickPendingIntent(R.id.iv_previous, previousPendingIntent);

        setClick(context, contentView);
        setClick(context, contentBigView);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(context, IMPORTANCE_LOW_CHANNEL_ID)
                .setAutoCancel(false)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setStyle(new NotificationCompat.DecoratedCustomViewStyle())
                .setCustomContentView(contentView)
                .setCustomBigContentView(contentBigView);

        getNotificationManager(context);
        notificationManager.notify(NOTIFICATION_MUSIC_ID, builder.build());

    }

    /*
     * 设置通知显示数据
     * */
    private static void setData(Song data, RemoteViews contentView, boolean isPlaying, Bitmap banner) {

        contentView.setTextViewText(R.id.tv_title, data.getTitle());
        contentView.setTextViewText(R.id.tv_info, data.getSinger().getNickname());

        int playButtonResourceId = isPlaying ? R.drawable.ic_music_notification_pause : R.drawable.ic_music_notification_play;
        contentView.setImageViewResource(R.id.iv_play, playButtonResourceId);
        contentView.setImageViewBitmap(R.id.iv_banner, banner);
    }

    /*
     * 设置通知点击事件
     * */
    private static void setClick(Context context, RemoteViews contentView) {

        //播放
        PendingIntent playPendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_PLAY.hashCode(),
                new Intent(Constant.ACTION_PLAY),
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_play, playPendingIntent);

        //下一曲
        PendingIntent nextPendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_NEXT.hashCode(),
                new Intent(Constant.ACTION_NEXT),
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_next, nextPendingIntent);
        //歌词
        PendingIntent lyricPendingIntent = PendingIntent.getBroadcast(context,
                Constant.ACTION_LYRIC.hashCode(),
                new Intent(Constant.ACTION_LYRIC),
                PendingIntent.FLAG_UPDATE_CURRENT);
        contentView.setOnClickPendingIntent(R.id.iv_lyric, lyricPendingIntent);

    }
}
