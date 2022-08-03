package com.matrix.filter;

import cn.dev33.satoken.router.SaRouter;
import com.alibaba.fastjson2.JSON;
import com.matrix.context.TenantContextHold;
import com.matrix.context.UserContextHolder;
import com.matrix.entity.vo.LoginUser;
import com.matrix.entity.vo.Result;
import com.matrix.exception.GlobalExceptionHandler;
import com.matrix.properties.TenantProperties;
import com.matrix.utils.LoginHelper;
import com.matrix.utils.ServletUtils;
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
    private GlobalExceptionHandler globalExceptionHandler;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        if (tenantProperties.getEnable()) {
            if (SaRouter.match(tenantProperties.getIgnoreUrls()).isHit()) {
                TenantContextHold.setIgnore(true);
            } else {
                try {
                    LoginUser loginUser = LoginHelper.getLoginUser();
                    UserContextHolder.setLoginUser(loginUser);
                    TenantContextHold.setTenantId(loginUser.getTenantId());
                } catch (Exception e) {
                    Result<?> result = globalExceptionHandler.allExceptionHandler(request, e);
                    ServletUtils.renderString(response, JSON.toJSONString(result));
                    return;
                }
            }
        }
        filterChain.doFilter(request, response);
    }
}
