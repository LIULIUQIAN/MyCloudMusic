package com.example.mycloudmusic.util;

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
}
