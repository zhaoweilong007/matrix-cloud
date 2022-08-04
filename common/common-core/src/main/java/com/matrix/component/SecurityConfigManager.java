package com.matrix.component;

import cn.hutool.core.collection.CollectionUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.alibaba.nacos.api.exception.NacosException;
import com.matrix.properties.SecurityProperties;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.discovery.DiscoveryClient;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import javax.annotation.Resource;
import java.util.List;
import java.util.Map;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * 描述：
 *
 * @author zwl
 * @since 2022/8/3 17:50
 **/
public class SecurityConfigManager implements InitializingBean {

    public static final String PREFIX = "matrix-security";


    @Value("${spring.application.name}")
    private String appName;

    @Autowired
    private SecurityProperties securityProperties;

    @Autowired
    private ConfigService configService;

    @Autowired
    NacosConfigProperties nacosConfigProperties;

    @Resource
    private DiscoveryClient discoveryClient;


    @Override
    public void afterPropertiesSet() throws Exception {
        String config = configService.getConfig(PREFIX, nacosConfigProperties.getGroup(), 3000);
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        SecurityProperties properties = yaml.loadAs(config, SecurityProperties.class);

        //先加后减
        publishConfig(configService, nacosConfigProperties, properties);
        updateConfig(securityProperties);
        configService.addListener(PREFIX, nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                Yaml yaml = new Yaml();
                yaml.setBeanAccess(BeanAccess.FIELD);
                SecurityProperties properties = yaml.loadAs(config, SecurityProperties.class);
                updateConfig(properties);
            }
        });
    }

    private void publishConfig(ConfigService configService, NacosConfigProperties nacosConfigProperties, SecurityProperties properties) throws NacosException {
        if (CollectionUtil.isNotEmpty(securityProperties.getWhiteUrls())) {
            //需要判断是否包含服务名，包含不加
            Map<String, String> serviceMaps = discoveryClient.getServices().stream().collect(Collectors.toMap(Function.identity(), Function.identity(), (o, o2) -> o2));
            List<String> list = securityProperties.getWhiteUrls().stream()
                    .filter(s -> !serviceMaps.containsKey(s))
                    .map(s -> "/" + appName + s).collect(Collectors.toList());
            securityProperties.setWhiteUrls(list);
            securityProperties.addAll(properties.getWhiteUrls());
            Yaml yaml = new Yaml();
            yaml.setBeanAccess(BeanAccess.FIELD);
            String config = yaml.dump(securityProperties);
            configService.publishConfig(PREFIX, nacosConfigProperties.getGroup(), config, "yaml");
        } else {
            this.securityProperties.addAll(properties.getWhiteUrls());
        }
    }

    private void updateConfig(SecurityProperties properties) {
        if (properties.getWhiteUrls().isEmpty()) {
            return;
        }
        List<String> result = properties.getWhiteUrls().stream().map(s -> {
            if (s.contains(appName)) {
                s = StrUtil.subAfter(s, appName, true);
            }
            return s;
        }).collect(Collectors.toList());
        securityProperties.setWhiteUrls(result);
    }
}
