package com.example.mycloudmusic.manager.impl;

import android.app.Activity;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

public class ActivityManager {

   private static Set<Activity> activities = new HashSet<>();

    private static ActivityManager instance;

    public static ActivityManager getInstance(){
        if (instance == null){
            instance = new ActivityManager();
        }
        return instance;
    }

    public void add(Activity activity){
        activities.add(activity);
    }

    public void remove(Activity activity){
        activities.remove(activity);
    }

    public void clear(){

        Iterator<Activity> iterator = activities.iterator();

        while (iterator.hasNext()){
            iterator.next().finish();
        }

    }
}
