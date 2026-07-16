package org.springframework.boot.web.client;

import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.DefaultUriBuilderFactory;
import org.springframework.http.client.SimpleClientHttpRequestFactory;

/**
 * Spring Boot 4.0 兼容 stub。
 * RestTemplateBuilder 在 SB4 中已移除，此类提供最小 API 兼容 easy-trans 3.x。
 * 后续升级 easy-trans 后可删除。
 *
 */
public class RestTemplateBuilder {

    private String rootUri;
    private java.time.Duration connectTimeout;
    private java.time.Duration readTimeout;

    public RestTemplate build() {
        SimpleClientHttpRequestFactory requestFactory = new SimpleClientHttpRequestFactory();
        if (connectTimeout != null) {
            requestFactory.setConnectTimeout(connectTimeout);
        }
        if (readTimeout != null) {
            requestFactory.setReadTimeout(readTimeout);
        }
        RestTemplate restTemplate = new RestTemplate(requestFactory);
        if (rootUri != null) {
            restTemplate.setUriTemplateHandler(new DefaultUriBuilderFactory(rootUri));
        }
        return restTemplate;
    }

    public RestTemplateBuilder rootUri(String rootUri) {
        this.rootUri = rootUri;
        return this;
    }

    public RestTemplateBuilder setConnectTimeout(java.time.Duration connectTimeout) {
        this.connectTimeout = connectTimeout;
        return this;
    }

    public RestTemplateBuilder setReadTimeout(java.time.Duration readTimeout) {
        this.readTimeout = readTimeout;
        return this;
    }
}
