package com.matrix.sentinel;

import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Primary;

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
    @Primary
    public CustomJsonConvert jsonFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, FlowRule.class);
    }

    @Bean({"sentinel-json-degrade-converter"})
    @Primary
    public CustomJsonConvert jsonDegradeConverter() {
        return new CustomJsonConvert(this.objectMapper, DegradeRule.class);
    }

    @Bean({"sentinel-json-system-converter"})
    @Primary
    public CustomJsonConvert jsonSystemConverter() {
        return new CustomJsonConvert(this.objectMapper, SystemRule.class);
    }

    @Bean({"sentinel-json-authority-converter"})
    @Primary
    public CustomJsonConvert jsonAuthorityConverter() {
        return new CustomJsonConvert(this.objectMapper, AuthorityRule.class);
    }

    @Bean({"sentinel-json-param-flow-converter"})
    @Primary
    public CustomJsonConvert jsonParamFlowConverter() {
        return new CustomJsonConvert(this.objectMapper, ParamFlowRule.class);
    }

}
