package com.example.mycloudmusic.util;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;

import com.example.mycloudmusic.activity.BaseCommonActivity;

public class ClipboardUtil {
    public static void copyText(Context context, String data) {

        ClipboardManager clipboardManager = (ClipboardManager) context.getSystemService(Context.CLIPBOARD_SERVICE);

        ClipData clipData = ClipData.newPlainText("text", data);

        clipboardManager.setPrimaryClip(clipData);
    }
}
