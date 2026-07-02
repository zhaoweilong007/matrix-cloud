package com.matrix.web.xss;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * XSS 过滤 Filter
 *
 */
@Order(Ordered.HIGHEST_PRECEDENCE + 50)
public class XssFilter extends OncePerRequestFilter {

    /**
     * 排除的 URL 列表
     */
    private final List<String> excludeUrls;

    /**
     * 构造 XSS 过滤器
     *
     * @param excludeUrls 排除的 URL 列表
     */
    public XssFilter(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls != null ? excludeUrls : Collections.emptyList();
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {
        // 判断是否排除过滤
        if (isExcluded(request.getRequestURI())) {
            filterChain.doFilter(request, response);
            return;
        }

        // XSS 过滤
        XssRequestWrapper wrapper = new XssRequestWrapper(request);
        filterChain.doFilter(wrapper, response);
    }

    private boolean isExcluded(String requestUri) {
        if (StrUtil.isBlank(requestUri)) {
            return true;
        }
        AntPathMatcher matcher = new AntPathMatcher();
        for (String pattern : excludeUrls) {
            if (matcher.match(pattern, requestUri)) {
                return true;
            }
        }
        return false;
    }
}
