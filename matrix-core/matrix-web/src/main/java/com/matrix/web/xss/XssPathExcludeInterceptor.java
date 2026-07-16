package com.matrix.web.xss;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.Collections;
import java.util.List;
import org.springframework.util.AntPathMatcher;
import org.springframework.web.servlet.AsyncHandlerInterceptor;

/** 为 JSON 请求清理保留与表单过滤器一致的路径排除语义。 */
final class XssPathExcludeInterceptor implements AsyncHandlerInterceptor {
    private final AntPathMatcher pathMatcher = new AntPathMatcher();
    private final List<String> excludeUrls;

    XssPathExcludeInterceptor(List<String> excludeUrls) {
        this.excludeUrls = excludeUrls == null ? Collections.emptyList() : excludeUrls;
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
        boolean skipped = excludeUrls.stream().anyMatch(pattern -> pathMatcher.match(pattern, request.getRequestURI()));
        XssCleanContext.setSkipped(skipped);
        return true;
    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) {
        XssCleanContext.clear();
    }

    @Override
    public void afterConcurrentHandlingStarted(HttpServletRequest request, HttpServletResponse response, Object handler) {
        XssCleanContext.clear();
    }
}
