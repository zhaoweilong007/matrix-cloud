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
 * @author ZhaoWeiLong
 * @since 2023/9/24
 **/
@AutoConfiguration
public class SentinelConfig {

    private final ObjectMapper objectMapper = new ObjectMapper();

    public SentinelConfig() {
        this.objectMapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false);
    }

    @Bean({"sentinel-json-flow-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-flow-converter")
    public CustomJsonConvert jsonFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, FlowRule.class);
    }

    @Bean({"sentinel-json-degrade-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-degrade-converter")
    public CustomJsonConvert jsonDegradeConverter() {
        return new CustomJsonConvert(this.objectMapper, DegradeRule.class);
    }

    @Bean({"sentinel-json-system-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-system-converter")
    public CustomJsonConvert jsonSystemConverter() {
        return new CustomJsonConvert(this.objectMapper, SystemRule.class);
    }

    @Bean({"sentinel-json-authority-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-authority-converter")
    public CustomJsonConvert jsonAuthorityConverter() {
        return new CustomJsonConvert(this.objectMapper, AuthorityRule.class);
    }

    @Bean({"sentinel-json-param-flow-converter"})
    @ConditionalOnMissingBean(name = "sentinel-json-param-flow-converter")
    public CustomJsonConvert jsonParamFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, ParamFlowRule.class);
    }
}
