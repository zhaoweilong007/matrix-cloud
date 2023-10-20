package com.matrix.common.util;

import cn.hutool.core.util.DesensitizedUtil;
import cn.hutool.core.util.StrUtil;

/**
 * 自定义脱敏工具类
 *
 * @author LeonZhou
 */
public class DesensitizationUtil {


    /**
     * 【手机号码】保留前三位，其他隐藏，比如135********
     *
     * @param num 移动电话；
     * @return 脱敏后的移动电话；
     */
    public static String mobilePhone(String num) {
        if (StrUtil.isBlank(num)) {
            return StrUtil.EMPTY;
        }
        return StrUtil.hide(num, 3, num.length());
    }


    public static String chineseName(String name) {
        if (StrUtil.isBlank(name)) {
            return StrUtil.EMPTY;
        }
        return DesensitizedUtil.desensitized(name, DesensitizedUtil.DesensitizedType.CHINESE_NAME);
    }
}
