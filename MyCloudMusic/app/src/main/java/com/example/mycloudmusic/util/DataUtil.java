package com.example.mycloudmusic.util;

import com.example.mycloudmusic.domain.Song;
import com.example.mycloudmusic.domain.User;
import com.example.mycloudmusic.domain.UserResult;
import com.google.common.collect.Ordering;

import org.checkerframework.checker.nullness.compatqual.NullableDecl;

import java.util.ArrayList;
import java.util.List;

public class DataUtil {

    /**
     * 更高是否在播放列表字段
     */
    public static void changePlayListFlag(List<Song> datum, boolean value) {
        for (Song data : datum) {
            data.setPlayList(value);
        }
    }

    /**
     * 返回用户测试数据
     *
     * @return
     */
    public static List<User> getTestUserData() {
        //创建一个列表
        ArrayList<User> results = new ArrayList<>();

        //全中文
        User user = null;
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("我的云音乐" + i);
            results.add(user);
        }

        //有单词
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("爱学啊smile" + i);
            results.add(user);
        }

        //全中文
        for (int i = 0; i < 50; i++) {
            user = new User();
            user.setNickname("爱学啊李薇" + i);
            results.add(user);
        }

        return results;
    }

    /**
     * 根据用户首字母分组
     * 数据的格式化和评论格式
     * <p>
     * 标题
     * 真实数据
     * 标题
     * 真实数据
     *
     * @param datum
     * @return
     */
    public static UserResult processUser(List<User> datum) {

        //创建结果数组
        List<Object> results = new ArrayList<>();

        //创建字母列表
        ArrayList<String> letters = new ArrayList<>();

        //字母索引列表
        ArrayList<Integer> indexes = new ArrayList<>();

        //按照第一个字母排序
        //这里使用的Guava提供的排序方法
        //也可以使用Java的排序方法
        Ordering<User> byFirst = new Ordering<User>() {
            @Override
            public int compare(@NullableDecl User left, @NullableDecl User right) {
                //根据第一个字母排序
                return left.getFirst().compareTo(right.getFirst());
            }
        };

        //按照拼音首字母的第一个字母分组
        //这些操作都可以使用响应式编程方法
        //这里为了简单使用了最普通的方法
        //因为只要明白了原理
        //使用其他方法就是语法不同而已

        //排序
        datum = byFirst.immutableSortedCopy(datum);

        //循环所有数据

        //上一次用户
        User lastUser = null;

        for (User data :datum) {
            if (lastUser != null && lastUser.getFirst().endsWith(data.getFirst())) {
                //相等
            } else {
                //添加标题

                String letter = data.getFirst().toUpperCase();

                //添加字母索引
                indexes.add(results.size());

                //添加标题
                results.add(letter);

                //添加字母
                letters.add(letter);
            }

            //添加用
            results.add(data);

            lastUser = data;
        }

        //字母列表转为数组
        String[] letterArray = letters.toArray(new String[letters.size()]);

        //字母索引转为数组
        Integer[] indexArray = indexes.toArray(new Integer[indexes.size()]);

        return new UserResult(results, letterArray, indexArray);
    }

    /**
     * 根据用户昵称计算出拼音
     *
     * @param datum
     * @return
     */
    public static List<User> processUserPinyin(List<User> datum) {

        //循环所有数据
        for (User data : datum) {
            //获取全拼
            data.setPinyin(PinyinUtil.pinyin(data.getNickname()));

            //获取拼音首字母
            //例如："爱学啊"
            //结果为：axa
            data.setPinyinFirst(PinyinUtil.pinyinFirst(data.getNickname()));

            //拼音首字母的首字母
            //例如："爱学啊"
            //结果为：a
            data.setFirst(data.getPinyinFirst().substring(0, 1));
        }

        return datum;
    }
}
