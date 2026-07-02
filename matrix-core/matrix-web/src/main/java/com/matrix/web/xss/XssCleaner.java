package com.matrix.web.xss;

import org.jsoup.Jsoup;
import org.jsoup.safety.Safelist;
import org.springframework.util.StringUtils;

/**
 * XSS 清理器（基于 Jsoup）
 *
 */
public class XssCleaner {

    /**
     * 清理 HTML 内容中的 XSS 攻击代码
     *
     * @param html 原始内容
     * @return 清理后的安全内容
     */
    public static String clean(String html) {
        if (!StringUtils.hasText(html)) {
            return html;
        }
        return Jsoup.clean(html, Safelist.relaxed()
                .addProtocols("a", "href", "http", "https", "ftp", "mailto"));
    }
}
