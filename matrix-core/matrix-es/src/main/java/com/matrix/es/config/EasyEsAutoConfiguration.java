package com.matrix.es.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;

/**
 * Easy-Es Elasticsearch 自动配置。
 *
 * <p>当 classpath 中存在 Easy-Es 时自动启用，
 * 提供 Elasticsearch ORM 操作的基础配置支持。</p>
 *
 */
@AutoConfiguration
@ConditionalOnClass(name = "org.dromara.easyes.annotation.Interceptor")
public class EasyEsAutoConfiguration {

    private static final Logger log = LoggerFactory.getLogger(EasyEsAutoConfiguration.class);

    public EasyEsAutoConfiguration() {
        log.info("Easy-Es detected, Elasticsearch support enabled");
    }
}
