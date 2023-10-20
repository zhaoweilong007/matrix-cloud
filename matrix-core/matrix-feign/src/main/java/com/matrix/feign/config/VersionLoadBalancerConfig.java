package com.matrix.feign.config;

import com.matrix.auto.properties.GaryLoadBalanceProperties;
import com.matrix.common.constant.ConfigConstants;
import com.matrix.feign.chooser.IRuleChooser;
import com.matrix.feign.chooser.NacosRuleChooser;
import com.matrix.feign.loadbalancer.VersionLoadBalancerClientFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.client.ConditionalOnDiscoveryEnabled;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClientsProperties;
import org.springframework.cloud.loadbalancer.annotation.LoadBalancerClientSpecification;
import org.springframework.cloud.loadbalancer.support.LoadBalancerClientFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;
import org.springframework.core.env.Environment;
import org.springframework.util.ClassUtils;

import java.util.Collections;
import java.util.List;

/**
 * 版本控制的路由选择类配置
 */
@Slf4j
@AutoConfiguration
@EnableConfigurationProperties(GaryLoadBalanceProperties.class)
@ConditionalOnDiscoveryEnabled
public class VersionLoadBalancerConfig {

    @Bean
    @ConditionalOnMissingBean(IRuleChooser.class)
    @ConditionalOnProperty(prefix = ConfigConstants.CONFIG_LOADBALANCE_ISOLATION, value = "chooser")
    public IRuleChooser customRuleChooser(Environment environment, ApplicationContext context) {
        IRuleChooser defaultRuleChooser = defaultRuleChooser();
        if (environment.containsProperty(ConfigConstants.CONFIG_LOADBALANCE_ISOLATION_CHOOSER)) {
            String chooserRuleClassString = environment.getProperty(ConfigConstants.CONFIG_LOADBALANCE_ISOLATION_CHOOSER);
            if (StringUtils.isNotBlank(chooserRuleClassString)) {
                try {
                    Class<?> ruleClass = ClassUtils.forName(chooserRuleClassString, context.getClassLoader());
                    defaultRuleChooser = (IRuleChooser) ruleClass.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    log.warn("没有找到定义的选择器，将使用内置的选择器：com.matrix.feign.chooser.NacosRuleChooser", e);
                }
            }
        }
        return defaultRuleChooser;
    }

    @Bean
    @ConditionalOnMissingBean(value = IRuleChooser.class)
    public IRuleChooser defaultRuleChooser() {
        return new NacosRuleChooser();
    }


    @Bean
    @Primary
    @ConditionalOnProperty(prefix = ConfigConstants.CONFIG_LOADBALANCE_ISOLATION, name = "enabled", havingValue = "true")
    public LoadBalancerClientFactory versionLoadBalancerConfig(LoadBalancerClientsProperties properties,
                                                               ObjectProvider<List<LoadBalancerClientSpecification>> configurations,
                                                               IRuleChooser ruleChooser,
                                                               GaryLoadBalanceProperties loadBalanceProperties) {
        final VersionLoadBalancerClientFactory versionLoadBalancerConfig = new VersionLoadBalancerClientFactory(properties, ruleChooser, loadBalanceProperties);
        versionLoadBalancerConfig.setConfigurations(configurations.getIfAvailable(Collections::emptyList));
        return versionLoadBalancerConfig;
    }
}
