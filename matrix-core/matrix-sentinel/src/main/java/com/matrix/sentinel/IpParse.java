package com.matrix.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc_v6x.callback.RequestOriginParser;
import com.matrix.common.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 针对ip的解析器
 *
 **/
public class IpParse implements RequestOriginParser {
    /**
     * 解析请求来源 IP
     *
     * @param httpServletRequest HTTP 请求
     * @return 客户端 IP 地址
     */
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        return ServletUtils.getClientIP();
    }
}
