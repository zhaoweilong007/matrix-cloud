package com.matrix.filter;

import com.matrix.context.TenantContextHold;
import com.matrix.context.UserContextHolder;
import com.matrix.entity.vo.LoginUser;
import com.matrix.utils.LoginHelper;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 描述：租户过滤器
 *
 * @author zwl
 * @since 2022/7/26 16:37
 **/
public class TenantFilter extends OncePerRequestFilter {


    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        LoginUser loginUser = LoginHelper.getLoginUser();
        if (loginUser != null) {
            UserContextHolder.setContext(loginUser);
            TenantContextHold.setTenantId(loginUser.getTenantId());
        }
        filterChain.doFilter(request, response);
    }
}
