package com.matrix.config;


import com.matrix.properties.CacheProperties;
import com.matrix.properties.MatrixProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.cloud.client.loadbalancer.LoadBalanced;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/7/8 16:22
 **/
@Import({MatrixProperties.class, CacheProperties.class})
public class MatrixAutoConfiguration {


    @Bean
    @ConfigurationProperties(prefix = "rest.template.config")
    public HttpComponentsClientHttpRequestFactory customHttpRequestFactory() {
        return new HttpComponentsClientHttpRequestFactory();
    }

    @Bean
    @LoadBalanced
    public RestTemplate restTemplate(HttpComponentsClientHttpRequestFactory customHttpRequestFactory) {
        return new RestTemplate(customHttpRequestFactory);
    }

}
