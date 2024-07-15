package com.matrix.sentinel;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.net.NetUtil;
import com.alibaba.cloud.commons.lang.StringUtils;
import com.alibaba.csp.sentinel.datasource.Converter;
import com.alibaba.csp.sentinel.slots.block.authority.AuthorityRule;
import com.alibaba.csp.sentinel.slots.block.degrade.DegradeRule;
import com.alibaba.csp.sentinel.slots.block.flow.FlowRule;
import com.alibaba.csp.sentinel.slots.block.flow.param.ParamFlowRule;
import com.alibaba.csp.sentinel.slots.system.SystemRule;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.matrix.common.util.spring.SpringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import one.util.streamex.StreamEx;

/**
 * 自定义转换器
 *
 * @author ZhaoWeiLong
 * @since 2023/9/24
 **/
@Slf4j
public class CustomJsonConvert<T extends Object> implements Converter<String, Collection<Object>> {


    private final ObjectMapper objectMapper;

    private final Class<T> ruleClass;

    private String clientIp;


    private String port;

    public CustomJsonConvert(ObjectMapper objectMapper, Class<T> ruleClass) {
        this.objectMapper = objectMapper;
        this.ruleClass = ruleClass;
        clientIp = SpringUtils.getProperty("spring.cloud.sentinel.transport.client-ip");
        if (clientIp == null) {
            clientIp = NetUtil.getLocalhost().getHostAddress();
        }
        port = SpringUtils.getProperty("spring.cloud.sentinel.transport.port");
    }


    @Override
    public Collection<Object> convert(String source) {
        Collection<Object> ruleCollection;

        // hard code
        if (ruleClass == FlowRule.class || ruleClass == DegradeRule.class
                || ruleClass == SystemRule.class || ruleClass == AuthorityRule.class
                || ruleClass == ParamFlowRule.class) {
            ruleCollection = new ArrayList<>();
        } else {
            ruleCollection = new HashSet<>();
        }

        if (StringUtils.isEmpty(source)) {
            log.info("converter can not convert rules because source is empty");
            return ruleCollection;
        }
        try {
            List<HashMap> sourceArray = objectMapper.readValue(source,
                    new TypeReference<List<HashMap>>() {
                    });

            if (CollUtil.isNotEmpty(sourceArray)) {
                final Map<String, List<HashMap>> ruleMap = StreamEx.of(sourceArray).groupingBy(object -> {
                    HashMap map = ((HashMap) object);
                    return map.get("ip").toString() + ":" + map.get("port").toString();
                });
                sourceArray = ruleMap.get(clientIp + ":" + port.toString());
            }

            for (HashMap obj : sourceArray) {
                if (obj.containsKey("rule")) {
                    obj.putAll(((Map) obj.get("rule")));
                }
                try {
                    String item = objectMapper.writeValueAsString(obj);
                    Optional.ofNullable(convertRule(item))
                            .ifPresent(ruleCollection::add);
                } catch (IOException e) {
                    log.error("sentinel rule convert error: " + e.getMessage(), e);
                    throw new IllegalArgumentException(
                            "sentinel rule convert error: " + e.getMessage(), e);
                }
            }
        } catch (Exception e) {
            if (e instanceof RuntimeException) {
                throw (RuntimeException) e;
            } else {
                throw new RuntimeException("convert error: " + e.getMessage(), e);
            }
        }
        return ruleCollection;
    }

    private Object convertRule(String ruleStr) throws IOException {
        return objectMapper.readValue(ruleStr, ruleClass);
    }

}
