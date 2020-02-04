package com.example.mycloudmusic.util;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.graphics.BitmapFactory;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.example.mycloudmusic.R;

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
}
