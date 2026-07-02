package com.matrix.crypto.util;

import jakarta.servlet.ReadListener;
import jakarta.servlet.ServletInputStream;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletRequestWrapper;
import java.io.ByteArrayInputStream;
import java.io.IOException;

/**
 * 请求体包装器，用于替换请求体内容
 *
 */
public class CryptoRequestBodyWrapper extends HttpServletRequestWrapper {

    /**
     * 解密后的请求体内容
     */
    private byte[] body;

    public CryptoRequestBodyWrapper(HttpServletRequest request) {
        super(request);
        this.body = new byte[0];
    }

    /**
     * 设置替换后的请求体
     *
     * @param body 新的请求体字节数组
     */
    public void setBody(byte[] body) {
        this.body = body;
    }

    @Override
    public ServletInputStream getInputStream() throws IOException {
        return new CachedServletInputStream(body);
    }

    @Override
    public int getContentLength() {
        return body.length;
    }

    @Override
    public long getContentLengthLong() {
        return body.length;
    }

    /**
     * 缓存请求输入流，基于 ByteArrayInputStream 实现
     */
    private static class CachedServletInputStream extends ServletInputStream {

        private final ByteArrayInputStream inputStream;

        public CachedServletInputStream(byte[] body) {
            this.inputStream = new ByteArrayInputStream(body);
        }

        @Override
        public boolean isFinished() {
            return inputStream.available() == 0;
        }

        @Override
        public boolean isReady() {
            return true;
        }

        @Override
        public void setReadListener(ReadListener listener) {
            throw new UnsupportedOperationException();
        }

        @Override
        public int read() throws IOException {
            return inputStream.read();
        }
    }
}
