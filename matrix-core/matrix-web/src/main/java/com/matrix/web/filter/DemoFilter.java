package com.matrix.web.filter;

import cn.hutool.core.util.StrUtil;
import com.matrix.common.result.R;
import com.matrix.common.util.servlet.ServletUtils;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.util.List;
import lombok.Setter;
import org.springframework.web.filter.OncePerRequestFilter;

/**
 * 演示模式过滤器，禁止用户发起写操作，避免影响测试数据。
 *
 * <p>通过 {@code matrix.demo=true} 启用，默认 false。
 * 仅拦截 POST/PUT/DELETE 请求，GET 请求不受影响。</p>
 *
 * @author matrix
 */
public class DemoFilter extends OncePerRequestFilter {

    /** 要排除的 URL 模式列表（AntPath 风格），如 /doc.html、/swagger-ui/** */
    @Setter
    private List<String> excludeUrls = List.of();

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String method = request.getMethod();
        // 仅拦截写操作
        return !StrUtil.equalsAnyIgnoreCase(method, "POST", "PUT", "DELETE");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain) {
        ServletUtils.writeJSON(response, R.fail("演示模式，不允许写操作"));
    }
}
