package com.matrix.web.filter;

import cn.hutool.core.util.StrUtil;
import com.matrix.common.constant.CommonConstants;
import com.matrix.common.context.TerminalContextHolder;
import com.matrix.common.enums.PlatformUserTypeEnum;
import com.matrix.common.enums.PlatformsTypeEnum;
import com.matrix.common.util.json.JsonUtils;
import com.matrix.common.util.servlet.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/17
 **/
public class UserDeviceFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        //设置终端类型
        try {
            final String userDevice = ServletUtils.getHeader(request, CommonConstants.USER_DEVICE, StandardCharsets.UTF_8);
            if (StrUtil.isNotBlank(userDevice)) {
                try {
                    //获取终端平台标识
                    String platforms = Objects.requireNonNull(JsonUtils.parseMap(userDevice)).getStr(CommonConstants.PLATFORMS);
                    final PlatformsTypeEnum typeEnum = PlatformsTypeEnum.ofCode(platforms);
                    if (typeEnum != null) {
                        TerminalContextHolder.setTerminal(typeEnum);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            final String userType = ServletUtils.getHeader(request, CommonConstants.USER_TYPE, StandardCharsets.UTF_8);
            if (StrUtil.isNotBlank(userType)) {
                final PlatformUserTypeEnum userTypeEnum = PlatformUserTypeEnum.valueOf(userType);
                TerminalContextHolder.setUsertype(userTypeEnum);
            }
            filterChain.doFilter(request, response);
        } finally {
            TerminalContextHolder.clear();
        }
    }
}
