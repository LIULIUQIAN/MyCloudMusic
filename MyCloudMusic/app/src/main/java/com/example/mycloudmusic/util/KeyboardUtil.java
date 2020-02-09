package com.example.mycloudmusic.util;

import android.app.Activity;
import android.content.Context;
import android.view.inputmethod.InputMethodManager;

import com.example.mycloudmusic.activity.BaseCommonActivity;

public class KeyboardUtil {
    public static void hideKeyboard(Activity activity) {

        InputMethodManager inputMethodManager = (InputMethodManager) activity.getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(activity.getCurrentFocus().getWindowToken(),
                InputMethodManager.HIDE_NOT_ALWAYS);

    }
}
