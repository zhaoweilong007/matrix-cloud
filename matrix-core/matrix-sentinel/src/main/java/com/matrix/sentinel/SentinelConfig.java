package com.matrix.sentinel;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;

/**
 * Sentinel 规则转换器配置，提供 JSON 到各类规则的转换器 Bean
 **/
@AutoConfiguration
public class SentinelConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SentinelConfig() {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    /**
     * 流控规则 JSON 转换器
     */
    @Bean({"sentinel-json-flow-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-flow-converter")
    public CustomJsonConvert jsonFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, FlowRule.class);
    }

    /**
     * 熔断降级规则 JSON 转换器
     */
    @Bean({"sentinel-json-degrade-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-degrade-converter")
    public CustomJsonConvert jsonDegradeConverter() {
        return new CustomJsonConvert(this.objectMapper, DegradeRule.class);
    }

    /**
     * 系统规则 JSON 转换器
     */
    @Bean({"sentinel-json-system-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-system-converter")
    public CustomJsonConvert jsonSystemConverter() {
        return new CustomJsonConvert(this.objectMapper, SystemRule.class);
    }

    /**
     * 授权规则 JSON 转换器
     */
    @Bean({"sentinel-json-authority-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-authority-converter")
    public CustomJsonConvert jsonAuthorityConverter() {
        return new CustomJsonConvert(this.objectMapper, AuthorityRule.class);
    }

    /**
     * 热点参数流控规则 JSON 转换器
     */
    @Bean({"sentinel-json-param-flow-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-param-flow-converter")
    public CustomJsonConvert jsonParamFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, ParamFlowRule.class);
    }
}
