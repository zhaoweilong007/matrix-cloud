package com.matrix.filter;

import cn.dev33.satoken.router.SaRouter;
import com.matrix.context.TenantContextHold;
import com.matrix.context.UserContextHolder;
import com.matrix.entity.vo.LoginUser;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import lombok.AllArgsConstructor;
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
@AllArgsConstructor
public class TenantServletFilter extends OncePerRequestFilter {
    private TenantProperties tenantProperties;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (tenantProperties.getEnable()) {
            if (SaRouter.match(tenantProperties.getIgnoreUrls()).isHit()) {
                TenantContextHold.setIgnore(true);
            } else {
                LoginUser loginUser = LoginHelper.getLoginUser();
                UserContextHolder.setLoginUser(loginUser);
                TenantContextHold.setTenantId(loginUser.getTenantId());
            }
        }
        filterChain.doFilter(request, response);
    }
}
