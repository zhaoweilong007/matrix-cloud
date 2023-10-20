package com.matrix.tenant.core.security;

import cn.dev33.satoken.stp.StpUtil;
import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.util.StrUtil;
import com.matrix.auto.properties.TenantProperties;
import com.matrix.common.constant.Constants;
import com.matrix.common.context.LoginUserContextHolder;
import com.matrix.common.context.TenantContextHolder;
import com.matrix.common.enums.SystemErrorTypeEnum;
import com.matrix.common.model.login.LoginUser;
import com.matrix.common.result.R;
import com.matrix.common.util.servlet.ServletUtils;
import com.matrix.tenant.core.service.ITenantFrameworkService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Objects;
import java.util.Set;

/**
 * 多租户 Security Web 过滤器
 * 1. 如果是登陆的用户，校验是否有权限访问该租户，避免越权问题。
 * 2. 如果请求未带租户的编号，检查是否是忽略的 URL，否则也不允许访问。
 * 3. 校验租户是合法，例如说被禁用、到期
 * <p>
 * 校验用户访问的租户，是否是其所在的租户，
 */
@Slf4j
@RequiredArgsConstructor
public class TenantSecurityWebFilter extends OncePerRequestFilter {

    private final TenantProperties tenantProperties;
    private final ITenantFrameworkService ITenantFrameworkService;

    private final Set<String> ignorePaths = Set.of("/actuator/**");
    private final AntPathMatcher pathMatcher = new AntPathMatcher();

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        final String header = request.getHeader(Constants.IGNORE_TENANT);
        if (StrUtil.isNotEmpty(header) && Objects.equals(header, "true")) {
            if (isIgnoreUrl(request)) {
                TenantContextHolder.setIgnore(true);
            }
            chain.doFilter(request, response);
            return;
        }

        // 如果非允许忽略租户的 URL，则校验租户是否合法
        if (isIgnoreUrl(request) || !StpUtil.isLogin()) {
            TenantContextHolder.setIgnore(true);
            chain.doFilter(request, response);
            return;
        }

        Long tenantId = TenantContextHolder.getTenantId();

        // 1. 登陆的用户，校验是否有权限访问该租户，避免越权问题。
        LoginUser user = LoginUserContextHolder.getUser();
        // 如果获取不到租户编号，则尝试使用登陆用户的租户编号
        if (tenantId == null) {
            tenantId = user.getTenantId();
            TenantContextHolder.setTenantId(tenantId);
            // 如果传递了租户编号，则进行比对租户编号，避免越权问题
        } else if (!Objects.equals(user.getTenantId(), TenantContextHolder.getTenantId())) { // Cloud 特殊逻辑：如果是 RPC 请求，就不校验了。主要考虑，一些场景下，会调用 TenantUtils 去切换租户
            log.error("[doFilterInternal][租户({}) User({}/{}) 越权访问租户({}) URL({}/{})]",
                    user.getTenantId(), user.getUserId(), user.getDeviceType(),
                    TenantContextHolder.getTenantId(), request.getRequestURI(), request.getMethod());
            ServletUtils.writeJSON(response, R.fail(SystemErrorTypeEnum.FORBIDDEN));
            return;
        }

        // 2. 如果请求未带租户的编号，不允许访问。
        if (tenantId == null) {
            log.error("[doFilterInternal][URL({}/{}) 未传递租户编号]", request.getRequestURI(), request.getMethod());
            ServletUtils.writeJSON(response, R.fail(SystemErrorTypeEnum.TENANT_NOT_FOUND));
            return;
        }
        // 3. 校验租户是合法，例如说被禁用、到期
        try {
            //默认值过滤
            if (!tenantId.equals(0L)) {
                ITenantFrameworkService.validTenant(tenantId);
            }
        } catch (Exception ex) {
            log.warn("校验tenantId失败:{}", ex.getMessage());
            ServletUtils.writeJSON(response, R.fail(SystemErrorTypeEnum.VALID_TENANT_FAIL));
            return;
        }


        // 继续过滤
        chain.doFilter(request, response);
    }

    private boolean isIgnoreUrl(HttpServletRequest request) {
        for (String ignorePath : ignorePaths) {
            if (pathMatcher.match(ignorePath, request.getRequestURI())) {
                return true;
            }
        }
        // 快速匹配，保证性能
        if (CollUtil.contains(tenantProperties.getIgnoreUrls(), request.getRequestURI())) {
            return true;
        }
        // 逐个 Ant 路径匹配
        for (String url : tenantProperties.getIgnoreUrls()) {
            if (pathMatcher.match(url, request.getRequestURI())) {
                return true;
            }
        }
        return false;
    }

}
