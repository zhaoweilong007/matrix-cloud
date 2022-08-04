package com.matrix.config;


import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosDiscoveryProperties;
import com.alibaba.cloud.nacos.NacosServiceManager;
import com.alibaba.nacos.api.annotation.NacosInjected;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.naming.NamingService;
import com.matrix.component.SecurityConfigManager;
import com.matrix.properties.MatrixProperties;
import com.matrix.properties.SecurityProperties;
import org.springframework.beans.factory.annotation.Autowired;
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
@Import(MatrixProperties.class)
public class MatrixAutoConfiguration {

    @Autowired
    private NacosConfigManager configManager;

    @Autowired
    private NacosServiceManager serviceManager;

    @Autowired
    private NacosDiscoveryProperties nacosDiscoveryProperties;

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


    @Bean
    public NamingService namingService() {
        return serviceManager.getNamingService(nacosDiscoveryProperties.getNacosProperties());
    }

    @Bean
    public ConfigService configService() {
        return configManager.getConfigService();
    }

    @Bean
    public SecurityConfigManager securityConfigManager() {
        return new SecurityConfigManager();
    }

}
