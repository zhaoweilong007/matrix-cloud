package org.springframework.boot.web.client;

import org.springframework.web.client.RestTemplate;

/**
 * Spring Boot 4.0 兼容 stub。
 * RestTemplateBuilder 在 SB4 中已移除，此类提供最小 API 兼容 easy-trans 3.x。
 * 后续升级 easy-trans 后可删除。
 *
 * @author ZhaoWeiLong
 * @since 2026/6/29
 */
public class RestTemplateBuilder {

    public RestTemplate build() {
        return new RestTemplate();
    }

    public RestTemplateBuilder rootUri(String rootUri) {
        return this;
    }

    public RestTemplateBuilder setConnectTimeout(java.time.Duration connectTimeout) {
        return this;
    }

    public RestTemplateBuilder setReadTimeout(java.time.Duration readTimeout) {
        return this;
    }
}
