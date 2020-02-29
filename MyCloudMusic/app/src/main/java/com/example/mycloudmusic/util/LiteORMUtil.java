package com.example.mycloudmusic.util;

import android.content.Context;

import com.example.mycloudmusic.BuildConfig;
import com.example.mycloudmusic.domain.SearchHistory;
import com.litesuits.orm.LiteOrm;
import com.litesuits.orm.db.assit.QueryBuilder;

import java.util.List;

public class LiteORMUtil {

    private static LiteORMUtil instance;
    private final LiteOrm orm;
    private final Context context;

    private LiteORMUtil(Context context) {
        this.context = context;

        PreferenceUtil sp = PreferenceUtil.getInstance(context);

        String dbName = String.format("%s.db", sp.getUserId());
        orm = LiteOrm.newCascadeInstance(context, dbName);

        orm.setDebugged(BuildConfig.DEBUG);
    }

    public static LiteORMUtil getInstance(Context context) {
        if (instance == null) {
            instance = new LiteORMUtil(context);
        }
        return instance;
    }

    /**
     * 创建或更新搜索历史
     */
    public void createOrUpdate(SearchHistory data) {
        orm.save(data);
    }

    /**
     * 查询所有搜索历史
     */
    public List<SearchHistory> querySearchHistory() {
        QueryBuilder<SearchHistory> queryBuilder = new QueryBuilder<>(SearchHistory.class).appendOrderDescBy("created_at");
        return orm.query(queryBuilder);
    }

    /**
     * 删除搜索历史
     */
    public void deleteSearchHistory(SearchHistory data) {
        orm.delete(data);
    }

}
