package com.example.mycloudmusic.util;

import com.example.mycloudmusic.listener.Consumer;

import java.util.List;

public class ListUtil {

    public static <T> void eachListener(List<T> datum, Consumer<T> action) {
        for (T listener : datum) {
            action.accept(listener);
        }
    }
}
