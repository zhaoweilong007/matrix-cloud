package com.matrix.web.xss;

import cn.hutool.core.util.StrUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.Map;
import org.springframework.util.StringUtils;

/**
 * XSS 请求包装器
 *
 */
public class XssRequestWrapper extends HttpServletRequestWrapper {

    /**
     * 构造 XSS 请求包装器
     *
     * @param request 原始请求
     */
    public XssRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    /**
     * 获取请求参数并清理 XSS
     */
    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.hasText(value)) {
            value = XssCleaner.clean(value);
        }
        return value;
    }

    /**
     * 获取请求参数数组并清理 XSS
     */
    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return null;
        }
        String[] cleaned = new String[values.length];
        for (int i = 0; i < values.length; i++) {
            cleaned[i] = XssCleaner.clean(values[i]);
        }
        return cleaned;
    }

    /**
     * 获取请求参数 Map 并清理 XSS
     */
    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameterMap = new LinkedHashMap<>();
        Map<String, String[]> originalMap = super.getParameterMap();
        for (Map.Entry<String, String[]> entry : originalMap.entrySet()) {
            String[] values = entry.getValue();
            String[] cleaned = new String[values.length];
            for (int i = 0; i < values.length; i++) {
                cleaned[i] = XssCleaner.clean(values[i]);
            }
            parameterMap.put(entry.getKey(), cleaned);
        }
        return parameterMap;
    }

    /**
     * 获取请求头并清理 XSS
     */
    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StringUtils.hasText(value)) {
            value = XssCleaner.clean(value);
        }
        return value;
    }

    @Override
    @SuppressWarnings("deprecation")
    public ServletInputStream getInputStream() throws IOException {
        // JSON 请求体的 XSS 清理在 Filter 层处理
        return super.getInputStream();
    }
}
