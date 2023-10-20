package com.matrix.log.utils;

import cn.hutool.core.net.NetUtil;
import cn.hutool.http.HtmlUtil;
import com.matrix.common.util.SpringUtils;
import com.matrix.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import net.dreamlu.mica.ip2region.core.Ip2regionSearcher;

/**
 * 获取地址类
 */
@Slf4j
public class AddressUtils {

    // 未知地址
    public static final String UNKNOWN = "XX XX";
    private static final Ip2regionSearcher regionSearcher = SpringUtils.getBean(Ip2regionSearcher.class);

    public static String getRealAddressByIP(String ip) {
        String address = UNKNOWN;
        if (StringUtils.isBlank(ip)) {
            return address;
        }
        // 内网不查询
        ip = "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : HtmlUtil.cleanHtmlTag(ip);
        if (NetUtil.isInnerIP(ip)) {
            return "内网IP";
        }
        address = regionSearcher.getAddress(ip);
        return address;
    }
}
