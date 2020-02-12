package com.example.mycloudmusic.util;

import net.sourceforge.pinyin4j.PinyinHelper;
import net.sourceforge.pinyin4j.format.HanyuPinyinCaseType;
import net.sourceforge.pinyin4j.format.HanyuPinyinOutputFormat;
import net.sourceforge.pinyin4j.format.HanyuPinyinToneType;
import net.sourceforge.pinyin4j.format.HanyuPinyinVCharType;
import net.sourceforge.pinyin4j.format.exception.BadHanyuPinyinOutputFormatCombination;

public class PinyinUtil {

    /**
     * 全拼
     */
    public static String pinyin(String data) {
        return pinyin(data, false);
    }

    /**
     * 首字母
     */
    public static String pinyinFirst(String data) {
        return pinyin(data, true);
    }

    /**
     * 全拼
     */
    public static String pinyin(String data, boolean isFirst) {

        HanyuPinyinOutputFormat format = new HanyuPinyinOutputFormat();

        //设置输出为小写
        //LOWERCASE:输出小写
        //UPPERCASE:输出大写
        format.setCaseType(HanyuPinyinCaseType.LOWERCASE);

        //如何显示音调
        //WITH_TONE_MARK:直接用音标符（必须设置WITH_U_UNICODE，否则会抛出异常）
        //WITH_TONE_NUMBER：1-4数字表示音标
        //WITHOUT_TONE：没有音标
        format.setToneType(HanyuPinyinToneType.WITHOUT_TONE);

        //特殊音标ü设置
        //WITH_V：用v表示ü
        //WITH_U_AND_COLON：用"u:"表示ü
        //WITH_U_UNICODE：直接用ü
        format.setVCharType(HanyuPinyinVCharType.WITH_V);

        char[] input = data.trim().toCharArray();

        StringBuilder sb = new StringBuilder();

        try {
            for (int i = 0; i < input.length; i++) {

                if (Character.toString(input[i]).matches("[\\u4E00-\\u9FA5]+")) {

                    String[] temp = PinyinHelper.toHanyuPinyinStringArray(input[i], format);
                    String pinyin = temp[0];
                    if (isFirst) {
                        sb.append(pinyin.substring(0, 1));
                    } else {
                        sb.append(pinyin);

                    }

                } else {
                    sb.append(input[i]);
                }

            }
        } catch (BadHanyuPinyinOutputFormatCombination badHanyuPinyinOutputFormatCombination) {
            badHanyuPinyinOutputFormatCombination.printStackTrace();
        }

        return sb.toString();
    }
}
