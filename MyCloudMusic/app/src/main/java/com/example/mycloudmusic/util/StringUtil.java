package com.example.mycloudmusic.util;

import java.util.ArrayList;
import java.util.List;

import static com.example.mycloudmusic.util.Constant.REGEX_EMAIL;
import static com.example.mycloudmusic.util.Constant.REGEX_PHONE;

public class StringUtil {

    /**
     * 是否是手机号
     *
     * @param value
     * @return
     */
    public static boolean isPhone(String value){
        return value.matches(REGEX_PHONE);
    }

    /**
     * 是否是邮箱
     *
     * @param value
     * @return
     */
    public static boolean isEmail(String value){
        return value.matches(REGEX_EMAIL);
    }

    /**
     * 是否是密码格式
     *
     * @param value
     * @return
     */
    public static boolean isPassword(String value){
        return value.trim().length() >=6 && value.trim().length() <=15;
    }

    /**
     * 是否符合昵称格式
     */
    public static boolean isNickname(String value) {
        return value.length() >= 2 && value.length() <= 10;
    }

    /**
     * 将一行字符串
     * 拆分为单个字
     * <p>
     * 只实现了中文
     * 英文是有问题的
     * 大家感兴趣可以在继续实现
     *
     * @param data
     * @return
     */
    public static String[] words(String data) {
        //创建一个列表
        List<String> results = new ArrayList<>();

        //转为char数组
        char[] chars = data.toCharArray();

        //循环每一个字符
        for (char c : chars
        ) {
            //转为字符串
            //冰添加到列表
            results.add(String.valueOf(c));
        }

        //转为数组
        return results.toArray(new String[results.size()]);
    }

}
