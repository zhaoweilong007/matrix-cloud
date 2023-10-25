package com.matrix.common.request;

import cn.hutool.extra.servlet.JakartaServletUtil;
import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Request Body 缓存 Wrapper
 */
public class CacheRequestBodyWrapper extends HttpServletRequestWrapper {

    /**
     * 缓存的内容
     */
    private final byte[] body;
    @Getter
    private final Map<String, String> headerMap = new HashMap<>();

    public CacheRequestBodyWrapper(HttpServletRequest request) {
        super(request);
        body = JakartaServletUtil.getBodyBytes(request);
        final Enumeration<String> headerNames = request.getHeaderNames();
        while (headerNames.hasMoreElements()) {
            String headerName = headerNames.nextElement();
            headerMap.put(headerName, request.getHeader(headerName));
        }
    }

    @Override
    public BufferedReader getReader() throws IOException {
        return new BufferedReader(new InputStreamReader(this.getInputStream()));
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        final ByteArrayInputStream inputStream = new ByteArrayInputStream(body);
        // 返回 ServletInputStream
        return new ServletInputStream() {

            @Override
            public int read() {
                return inputStream.read();
            }

            @Override
            public boolean isFinished() {
                return false;
            }

            @Override
            public boolean isReady() {
                return false;
            }

            @Override
            public void setReadListener(ReadListener readListener) {
            }

            @Override
            public int available() {
                return body.length;
            }

        };
    }

}
