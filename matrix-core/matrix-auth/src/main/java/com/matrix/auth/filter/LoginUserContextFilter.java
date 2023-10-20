package com.matrix.auth.filter;

import cn.dev33.satoken.stp.StpUtil;
import com.matrix.auth.utils.LoginHelper;
import com.matrix.common.constant.WebFilterOrderConstants;
import com.matrix.common.context.LoginUserContextHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * @author ZhaoWeiLong
 * @since 2023/8/22
 **/
@Order(WebFilterOrderConstants.USER_CONTEXT_FILTER)
public class LoginUserContextFilter extends OncePerRequestFilter {
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        try {
            if (StpUtil.isLogin()) {
                LoginUserContextHolder.setUser(LoginHelper.getLoginUser());
            }
            filterChain.doFilter(request, response);
        } finally {
            LoginUserContextHolder.clear();
        }
    }
}
