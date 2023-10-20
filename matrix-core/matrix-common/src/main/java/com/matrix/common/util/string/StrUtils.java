package com.matrix.common.util.string;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.util.ArrayUtil;
import cn.hutool.core.util.StrUtil;

import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * 字符串工具类
 */
public class StrUtils extends StrUtil {

    public static String maxLength(CharSequence str, int maxLength) {
        Assert.isTrue(maxLength > 0);
        if (null == str) {
            return null;
        }
        if (str.length() <= maxLength) {
            return str.toString();
        }
        return StrUtil.sub(str, 0, maxLength - 3) + "..."; // -3 的原因，是该方法会补充 ... 恰好
    }

    /**
     * 给定字符串是否以任何一个字符串开始
     * 给定字符串和数组为空都返回 false
     *
     * @param str      给定字符串
     * @param prefixes 需要检测的开始字符串
     * @since 3.0.6
     */
    public static boolean startWithAny(String str, Collection<String> prefixes) {
        if (StrUtil.isEmpty(str) || ArrayUtil.isEmpty(prefixes)) {
            return false;
        }

        for (CharSequence suffix : prefixes) {
            if (StrUtil.startWith(str, suffix, false)) {
                return true;
            }
        }
        return false;
    }

    /**
     * 判断是否包含手机号
     */
    public static boolean containsPhoneNumber(String input) {
        Pattern pattern = Pattern.compile("\\d{11}");
        Matcher matcher = pattern.matcher(input);
        while (matcher.find()) {
            String match = matcher.group();
            if (isPhoneNumber(match)) {
                return true;
            }
        }
        return false;
    }

    // 判断是否是手机号
    private static boolean isPhoneNumber(String phoneNumber) {
        Pattern pattern = Pattern.compile("^1[3-9]\\d{9}$");
        Matcher matcher = pattern.matcher(phoneNumber);
        return matcher.matches();
    }

}
