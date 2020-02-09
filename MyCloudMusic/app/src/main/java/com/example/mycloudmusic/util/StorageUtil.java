package com.example.mycloudmusic.util;

import android.app.Activity;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.os.Build;
import android.os.Environment;
import android.os.ParcelFileDescriptor;
import android.provider.MediaStore;

import com.example.mycloudmusic.activity.BaseCommonActivity;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class StorageUtil {

    public static final String JPG = "jpg";

    /**
     * MediaStore，ContentProviders内容提供者data列
     */
    private static final String COLUMN_DATA = "_data";


    /**
     * 保存图片到系统相册
     */
    public static Uri savePicture(Context context, File file) {
        //创建媒体路径
        Uri uri = insertPictureMediaStore(context, file);

        if (uri == null) {
            //获取路径失败
            return null;
        }

        //将原来的图片保存到目标uri
        //也就是保存到系统图片媒体库
        return saveFile(context, file, uri);
    }

    /**
     * 保存文件到uri对应的路径
     *
     * @param context
     * @param file
     * @param uri
     * @return
     */
    private static Uri saveFile(Context context, File file, Uri uri) {
        FileOutputStream fileOutputStream = null;
        try {
            //获取内容提供者
            ContentResolver contentResolver = context.getContentResolver();

            //获取uri对应的文件描述器
            ParcelFileDescriptor parcelFileDescriptor = contentResolver.openFileDescriptor(uri, "w");

            //创建文件输出流
            fileOutputStream = new FileOutputStream(parcelFileDescriptor.getFileDescriptor());

            //将file拷贝到输出流
            FileUtils.copyFile(file, fileOutputStream);

            return uri;
        } catch (IOException e) {
            //发送了异常
            e.printStackTrace();
        } finally {
            //判断是否需要关闭流
            if (fileOutputStream != null) {
                try {
                    //关闭流
                    fileOutputStream.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        return null;
    }

    /**
     * 创建相册图片路径
     */
    private static Uri insertPictureMediaStore(Context context, File file) {
        //创建内容集合
        ContentValues contentValues = new ContentValues();

        //媒体显示的名称
        //设置为文件的名称
        contentValues.put(MediaStore.Images.Media.DISPLAY_NAME, file.getName());

        //媒体类型
        contentValues.put(MediaStore.Images.Media.MIME_TYPE, "image/jpeg");

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            //拍摄媒体的时间
            contentValues.put(MediaStore.Images.Media.DATE_TAKEN, file.lastModified());
        }

        return context.getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, contentValues);


    }

    /**
     * 获取应用扩展sdcard中的路径
     */
    public static File getExternalPath(Context context, String userId, String title, String suffix) {

        //获取下载文件类型目录
        File dir = context.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS);

        String path = String.format("MyCloudMusic/%s/%s/%s.%s", userId, suffix, title, suffix);

        File file = new File(dir, path);
        if (!file.getParentFile().exists()) {
            file.getParentFile().mkdirs();
        }
        return file;
    }

    /**
     * 获取uri对应的data列值(文件路径)
     * 这种写法支持MediaStore，ContentProviders
     */
    public static String getMediaStorePath(Context context, Uri uri) {
        try (Cursor cursor = context.getContentResolver().query(uri, new String[]{COLUMN_DATA}, null, null, null)) {

            if (cursor != null && cursor.moveToFirst()) {

                int index = cursor.getColumnIndexOrThrow(COLUMN_DATA);
                return cursor.getString(index);
            }

        } catch (IllegalArgumentException e) {
            e.printStackTrace();
        }

        return null;
    }
}
