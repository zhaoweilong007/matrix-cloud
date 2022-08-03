package com.matrix.properties;

import cn.hutool.core.util.StrUtil;
import cn.hutool.extra.spring.SpringUtil;
import com.alibaba.cloud.nacos.NacosConfigManager;
import com.alibaba.cloud.nacos.NacosConfigProperties;
import com.alibaba.nacos.api.config.ConfigService;
import com.alibaba.nacos.api.config.listener.Listener;
import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.introspector.BeanAccess;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.stream.Collectors;

/**
 * 描述：权限配置
 *
 * @author zwl
 * @since 2022/7/8 15:03
 **/
@Data
@ConfigurationProperties(prefix = SecurityProperties.PREFIX)
@Slf4j
public class SecurityProperties implements InitializingBean {

    public static final String PREFIX = "matrix.security";

    /**
     * 放行白名单
     */
    List<String> whiteUrls = Lists.newArrayList();


    private final NacosConfigManager configManager = SpringUtil.getBean(NacosConfigManager.class);

    public void addAll(List<String> urls) {
        this.whiteUrls.addAll(urls);
        this.whiteUrls = this.whiteUrls.stream().distinct().collect(Collectors.toList());
        log.info("security whiteUrls: {}", this.whiteUrls);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        ConfigService configService = configManager.getConfigService();
        NacosConfigProperties nacosConfigProperties = configManager.getNacosConfigProperties();
        String config = configService.getConfig(PREFIX, nacosConfigProperties.getGroup(), 3000);
        updateConfig(config);
        configService.addListener(PREFIX, nacosConfigProperties.getGroup(), new Listener() {
            @Override
            public Executor getExecutor() {
                return null;
            }

            @Override
            public void receiveConfigInfo(String configInfo) {
                updateConfig(configInfo);
            }
        });
    }

    private void updateConfig(String configInfo) {
        if (StrUtil.isEmpty(configInfo)) return;
        Yaml yaml = new Yaml();
        yaml.setBeanAccess(BeanAccess.FIELD);
        SecurityProperties properties = yaml.loadAs(configInfo, SecurityProperties.class);
        addAll(properties.getWhiteUrls());
    }
}
