package com.matrix.sentinel;

import com.alibaba.csp.sentinel.adapter.spring.webmvc.callback.RequestOriginParser;
import com.matrix.common.util.servlet.ServletUtils;
import jakarta.servlet.http.HttpServletRequest;

/**
 * 针对ip的解析器
 *
 * @author ZhaoWeiLong
 * @since 2023/9/24
 **/
public class IpParse implements RequestOriginParser {
    @Override
    public String parseOrigin(HttpServletRequest httpServletRequest) {
        return ServletUtils.getClientIP();
    }
}
