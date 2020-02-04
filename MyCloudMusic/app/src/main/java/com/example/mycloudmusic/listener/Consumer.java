package com.example.mycloudmusic.listener;

public interface Consumer<T> {

    void accept(T t);
}
