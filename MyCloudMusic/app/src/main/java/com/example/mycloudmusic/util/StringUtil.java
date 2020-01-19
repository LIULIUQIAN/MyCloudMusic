package com.example.mycloudmusic.util;

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
}
