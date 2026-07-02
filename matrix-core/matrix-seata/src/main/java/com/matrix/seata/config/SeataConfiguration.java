package com.matrix.seata.config;

import com.matrix.auto.factory.YamlPropertySourceFactory;
import com.matrix.seata.interceptor.SeataRestTemplateInterceptor;
import jakarta.annotation.PostConstruct;
import java.sql.SQLException;
import javax.sql.DataSource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfiguration;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.PropertySource;

/**
 * Seata配置
 *
 */
@AutoConfiguration
@PropertySource(factory = YamlPropertySourceFactory.class, value = "classpath:seata.yml")
@ConditionalOnBean(DataSource.class)
@Slf4j
public class SeataConfiguration {

    /**
     * 创建 Seata RestTemplate 拦截器，自动传播 XID。
     */
    @Bean
    public SeataRestTemplateInterceptor seataRestTemplateInterceptor() {
        return new SeataRestTemplateInterceptor();
    }

    /** undo_log 表 DDL，Seata AT 模式记录事务 SQL 执行记录 */
    public static final String undoLogSql =
            """
            CREATE TABLE IF NOT EXISTS `undo_log`
            (
                `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
                `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
                `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context,such as serialization',
                `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
                `log_status`    INT(11)      NOT NULL COMMENT '0:normal status,1:defense status',
                `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
                `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
                UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`)
            ) ENGINE = InnoDB
              AUTO_INCREMENT = 1
              DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
            """;

    /** 数据源，用于自动创建 undo_log 表 */
    @Autowired
    public DataSource dataSource;

    /**
     * 判断当前数据库是否有undo_log 该表，如果没有，
     * 创建该表 undo_log 为seata 记录事务sql执行的记录表 第二阶段时，如果confirm会清除记录，如果是cancel 会根据记录补偿原数据
     */
    @PostConstruct
    public void detectTable() {
        try {
            dataSource.getConnection().prepareStatement(undoLogSql).execute();
        } catch (SQLException e) {
            log.error("创建[seata] undo_log表错误。", e);
        }
    }
}
