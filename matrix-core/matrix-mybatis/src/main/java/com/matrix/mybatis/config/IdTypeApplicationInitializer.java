package com.matrix.mybatis.config;

import cn.hutool.core.util.StrUtil;
import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.annotation.IdType;
import com.matrix.mybatis.utils.DbTypeUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.env.ConfigurableEnvironment;
import org.springframework.core.env.MapPropertySource;

import java.util.HashMap;
import java.util.Map;

/**
 * 数据库类型自动检测初始化器。
 *
 * <p>当 mybatis-plus 的 id-type 未显式配置时，根据数据源 URL 自动判定：</p>
 * <ul>
 *   <li>Oracle / PostgreSQL / KingbaseES / H2 → INPUT（手动输入 ID）</li>
 *   <li>MySQL / SQL Server / DM 达梦 → AUTO（自增 ID）</li>
 * </ul>
 *
 * <p>通过 {@code META-INF/spring/org.springframework.context.ApplicationContextInitializer.imports} 注册，
 * 替代 Spring Boot 4.0 已弃用的 {@code EnvironmentPostProcessor}。</p>
 *
 * @author matrix
 * @since 2026/7/2
 */
@Slf4j
public class IdTypeApplicationInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

    private static final String ID_TYPE_KEY = "mybatis-plus.global-config.db-config.id-type";
    private static final String DATASOURCE_DYNAMIC_KEY = "spring.datasource.dynamic";

    @Override
    public void initialize(ConfigurableApplicationContext applicationContext) {
        ConfigurableEnvironment environment = applicationContext.getEnvironment();

        // 如果已显式配置 id-type，则不覆盖
        IdType idType = getIdType(environment);
        if (idType != null && idType != IdType.NONE) {
            return;
        }

        // 获取数据库类型
        DbType dbType = getDbType(environment);
        if (dbType == null) {
            return;
        }

        // 根据数据库类型自动设置 id-type
        IdType autoType = detectIdType(dbType);
        setIdType(environment, autoType);
        log.info("Auto-detected database type: {}, setting mybatis-plus id-type to: {}", dbType, autoType);
    }

    private IdType getIdType(ConfigurableEnvironment environment) {
        String value = environment.getProperty(ID_TYPE_KEY);
        if (StrUtil.isBlank(value)) {
            return null;
        }
        try {
            return IdType.valueOf(value);
        } catch (IllegalArgumentException ex) {
            log.warn("Invalid mybatis-plus id-type config: {}", value, ex);
            return null;
        }
    }

    private void setIdType(ConfigurableEnvironment environment, IdType idType) {
        Map<String, Object> map = new HashMap<>();
        map.put(ID_TYPE_KEY, idType.name());
        environment.getPropertySources().addFirst(new MapPropertySource("mybatisPlusIdType", map));
    }

    private IdType detectIdType(DbType dbType) {
        return switch (dbType) {
            case ORACLE, ORACLE_12C, POSTGRE_SQL, KINGBASE_ES, H2 -> IdType.INPUT;
            default -> IdType.AUTO;
        };
    }

    private DbType getDbType(ConfigurableEnvironment environment) {
        String primary = environment.getProperty(DATASOURCE_DYNAMIC_KEY + ".primary");
        if (StrUtil.isEmpty(primary)) {
            return null;
        }
        String url = environment.getProperty(DATASOURCE_DYNAMIC_KEY + ".datasource." + primary + ".url");
        if (StrUtil.isEmpty(url)) {
            return null;
        }
        return DbTypeUtils.getDbType(url);
    }
}
