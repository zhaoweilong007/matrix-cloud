package com.matrix.properties;

import com.google.common.collect.Lists;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 描述：权限配置
 *
 * @author zwl
 * @since 2022/7/8 15:03
 **/
@Data
@ConfigurationProperties(prefix = "matrix.security")
@Slf4j
public class SecurityProperties {

    /**
     * 放行白名单
     */
    List<String> whiteUrls = Lists.newArrayList();


    public void addAll(List<String> urls) {
        this.whiteUrls.addAll(urls);
        this.whiteUrls = this.whiteUrls.stream().distinct().collect(Collectors.toList());
        log.info("security whiteUrls: {}", this.whiteUrls);
    }


}
