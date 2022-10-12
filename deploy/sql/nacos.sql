-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: nacos_config
-- ------------------------------------------------------
-- Server version	8.0.30

/*!40101 SET @OLD_CHARACTER_SET_CLIENT = @@CHARACTER_SET_CLIENT */;
/*!40101 SET @OLD_CHARACTER_SET_RESULTS = @@CHARACTER_SET_RESULTS */;
/*!40101 SET @OLD_COLLATION_CONNECTION = @@COLLATION_CONNECTION */;
/*!50503 SET NAMES utf8mb4 */;
/*!40103 SET @OLD_TIME_ZONE = @@TIME_ZONE */;
/*!40103 SET TIME_ZONE = '+00:00' */;
/*!40014 SET @OLD_UNIQUE_CHECKS = @@UNIQUE_CHECKS, UNIQUE_CHECKS = 0 */;
/*!40014 SET @OLD_FOREIGN_KEY_CHECKS = @@FOREIGN_KEY_CHECKS, FOREIGN_KEY_CHECKS = 0 */;
/*!40101 SET @OLD_SQL_MODE = @@SQL_MODE, SQL_MODE = 'NO_AUTO_VALUE_ON_ZERO' */;
/*!40111 SET @OLD_SQL_NOTES = @@SQL_NOTES, SQL_NOTES = 0 */;

create database if not exists `nacos_config`;

use `nacos_config`;
--
-- Table structure for table `config_info`
--

DROP TABLE IF EXISTS `config_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info`
(
    `id`                 bigint                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
    `group_id`           varchar(255) COLLATE utf8mb3_bin          DEFAULT NULL,
    `content`            longtext COLLATE utf8mb3_bin     NOT NULL COMMENT 'content',
    `md5`                varchar(32) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text COLLATE utf8mb3_bin COMMENT 'source user',
    `src_ip`             varchar(50) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'source ip',
    `app_name`           varchar(128) COLLATE utf8mb3_bin          DEFAULT NULL,
    `tenant_id`          varchar(128) COLLATE utf8mb3_bin          DEFAULT '' COMMENT '租户字段',
    `c_desc`             varchar(256) COLLATE utf8mb3_bin          DEFAULT NULL,
    `c_use`              varchar(64) COLLATE utf8mb3_bin           DEFAULT NULL,
    `effect`             varchar(64) COLLATE utf8mb3_bin           DEFAULT NULL,
    `type`               varchar(64) COLLATE utf8mb3_bin           DEFAULT NULL,
    `c_schema`           text COLLATE utf8mb3_bin,
    `encrypted_data_key` text COLLATE utf8mb3_bin         NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfo_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 584
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='config_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info`
--

LOCK TABLES `config_info` WRITE;
/*!40000 ALTER TABLE `config_info`
    DISABLE KEYS */;
INSERT INTO `config_info`
VALUES (1, 'gateway-route', 'DEFAULT_GROUP', '[]', 'd751713988987e9331980363e24189ce', '2022-07-04 06:24:38',
        '2022-09-29 10:23:49', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '', ''),
       (17, 'sentinel', 'DEFAULT_GROUP', '[]', 'd751713988987e9331980363e24189ce', '2022-07-05 06:52:24',
        '2022-08-08 02:50:16', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '', ''),
       (22, 'gateway-flow', 'DEFAULT_GROUP', '[]', 'd751713988987e9331980363e24189ce', '2022-07-05 08:21:29',
        '2022-08-08 02:49:59', 'nacos', '0:0:0:0:0:0:0:1', '', '', '', '', '', 'json', '', ''),
       (23, 'gateway-api-group', 'DEFAULT_GROUP', '[]', 'd751713988987e9331980363e24189ce', '2022-07-05 08:21:40',
        '2022-07-05 08:21:40', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'json', NULL, ''),
       (27, 'matrix-security', 'DEFAULT_GROUP',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-07-29 07:28:54', '2022-10-08 10:29:14', NULL, '192.168.2.134', '', '',
        NULL, NULL, NULL, 'yaml', NULL, ''),
       (363, 'seataServer.properties', 'SEATA_GROUP',
        'transport.type=TCP\ntransport.server=NIO\ntransport.heartbeat=true\ntransport.enableTmClientBatchSendRequest=false\ntransport.enableRmClientBatchSendRequest=true\ntransport.enableTcServerBatchSendResponse=false\ntransport.rpcRmRequestTimeout=30000\ntransport.rpcTmRequestTimeout=30000\ntransport.rpcTcRequestTimeout=30000\ntransport.threadFactory.bossThreadPrefix=NettyBoss\ntransport.threadFactory.workerThreadPrefix=NettyServerNIOWorker\ntransport.threadFactory.serverExecutorThreadPrefix=NettyServerBizHandler\ntransport.threadFactory.shareBossWorker=false\ntransport.threadFactory.clientSelectorThreadPrefix=NettyClientSelector\ntransport.threadFactory.clientSelectorThreadSize=1\ntransport.threadFactory.clientWorkerThreadPrefix=NettyClientWorkerThread\ntransport.threadFactory.bossThreadSize=1\ntransport.threadFactory.workerThreadSize=default\ntransport.shutdown.wait=3\ntransport.serialization=seata\ntransport.compressor=none\n\nservice.vgroupMapping.default_tx_group=default\n\n\nclient.rm.asyncCommitBufferLimit=10000\nclient.rm.lock.retryInterval=10\nclient.rm.lock.retryTimes=30\nclient.rm.lock.retryPolicyBranchRollbackOnConflict=true\nclient.rm.reportRetryCount=5\nclient.rm.tableMetaCheckEnable=true\nclient.rm.tableMetaCheckerInterval=60000\nclient.rm.sqlParserType=druid\nclient.rm.reportSuccessEnable=false\nclient.rm.sagaBranchRegisterEnable=false\nclient.rm.sagaJsonParser=fastjson\nclient.rm.tccActionInterceptorOrder=-2147482648\nclient.tm.commitRetryCount=5\nclient.tm.rollbackRetryCount=5\nclient.tm.defaultGlobalTransactionTimeout=60000\nclient.tm.degradeCheck=false\nclient.tm.degradeCheckAllowTimes=10\nclient.tm.degradeCheckPeriod=2000\nclient.tm.interceptorOrder=-2147482648\nclient.undo.dataValidation=true\nclient.undo.logSerialization=jackson\nclient.undo.onlyCareUpdateColumns=true\nserver.undo.logSaveDays=7\nserver.undo.logDeletePeriod=86400000\nclient.undo.logTable=undo_log\nclient.undo.compress.enable=true\nclient.undo.compress.type=zip\nclient.undo.compress.threshold=64k\n\ntcc.fence.logTableName=tcc_fence_log\ntcc.fence.cleanPeriod=1h\n\n\nlog.exceptionRate=100\n\nstore.mode=db\nstore.lock.mode=db\nstore.session.mode=db\n\nstore.db.datasource=druid\nstore.db.dbType=mysql\nstore.db.driverClassName=com.mysql.cj.jdbc.Driver\nstore.db.url=jdbc:mysql://localhost:3306/seata?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai\nstore.db.user=root\nstore.db.password=123456\nstore.db.minConn=5\nstore.db.maxConn=30\nstore.db.globalTable=global_table\nstore.db.branchTable=branch_table\nstore.db.distributedLockTable=distributed_lock\nstore.db.queryLimit=100\nstore.db.lockTable=lock_table\nstore.db.maxWait=5000\n\n\n\nserver.recovery.committingRetryPeriod=1000\nserver.recovery.asynCommittingRetryPeriod=1000\nserver.recovery.rollbackingRetryPeriod=1000\nserver.recovery.timeoutRetryPeriod=1000\nserver.maxCommitRetryTimeout=-1\nserver.maxRollbackRetryTimeout=-1\nserver.rollbackRetryTimeoutUnlockEnable=false\nserver.distributedLockExpireTime=10000\nserver.xaerNotaRetryTimeout=60000\nserver.session.branchAsyncQueueSize=5000\nserver.session.enableBranchAsyncRemove=false\nserver.enableParallelRequestHandle=false\n\nmetrics.enabled=false\nmetrics.registryType=compact\nmetrics.exporterList=prometheus\nmetrics.exporterPrometheusPort=9898\n',
        '6e9829aa77a51ff924b5440882ee16e3', '2022-08-16 08:28:13', '2022-08-16 08:44:42', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'properties', '', ''),
       (371, 'service.vgroupMapping.default', 'SEATA_GROUP', 'default', 'c21f969b5f03d33d43e04f8f136e7682',
        '2022-08-16 09:43:23', '2022-08-16 09:43:23', NULL, '0:0:0:0:0:0:0:1', '', '', NULL, NULL, NULL, 'text', NULL,
        ''),
       (374, 'service.vgroupMapping.system-server-seata-service-group', 'SEATA_GROUP', 'default',
        'c21f969b5f03d33d43e04f8f136e7682', '2022-08-16 09:47:52', '2022-08-16 09:47:52', NULL, '0:0:0:0:0:0:0:1', '',
        '', NULL, NULL, NULL, 'text', NULL, ''),
       (380, 'service.vgroupMapping.api-gateway-seata-service-group', 'SEATA_GROUP', 'default',
        'c21f969b5f03d33d43e04f8f136e7682', '2022-08-24 06:44:15', '2022-08-24 06:44:15', NULL, '0:0:0:0:0:0:0:1', '',
        '', NULL, NULL, NULL, 'text', NULL, ''),
       (484, 'application-common', 'DEFAULT_GROUP',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:192.168.2.134}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '137bab4cfdc7523b8bc8891b7ed830a9', '2022-09-30 08:30:56', '2022-10-09 01:47:44', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '', ''),
       (494, 'api-gateway', 'DEFAULT_GROUP',
        'spring:\n  cloud:\n    sentinel:\n      filter:\n        enabled: false\n      scg:\n        fallback:\n          content-type: application/json\n          mode: response\n          response-status: 429\n          response-body: \'{\"message\":\"Too Many Requests\"}\'\n      datasource:\n        gw-flow:\n          nacos:\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n            username: ${NACOS_USERNAME:nacos}\n            password: ${NACOS_PASSWD:nacos}\n            data-id: gateway-flow\n            group-id: DEFAULT_GROUP\n            data-type: json\n            rule-type: gw_flow\n        gw-api-group:\n          nacos:\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n            username: ${NACOS_USERNAME:nacos}\n            password: ${NACOS_PASSWD:nacos}\n            data-id: gateway-api-group\n            group-id: DEFAULT_GROUP\n            data-type: json\n            rule-type: gw_api_group\n    gateway:\n      globalcors:\n        cors-configurations:\n          \'[/**]\':\n            allowed-origins: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n      discovery:\n        locator:\n          enabled: true\n          lower-case-service-id: true\n          url-expression: \"\'grayLb://\'+serviceId\"\n      loadbalancer:\n        use404: true',
        '457060057987b7e12bb8e840f95d79aa', '2022-09-30 09:18:19', '2022-10-08 10:24:24', 'nacos', '0:0:0:0:0:0:0:1',
        '', '', '', '', '', 'yaml', '', ''),
       (564, 'system-server-dev.yaml', 'DEFAULT_GROUP',
        'spring:\r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/matrix?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: 123456\r\n\r\nmatrix:\r\n  swagger:\r\n    enable: true\r\n    name: 系统服务\r\n    version: 1.0.0\r\n    description: 系统服务接口文档\r\n  tenant:\r\n    enable: true\r\n    ignoreTables:\r\n      - tenant\r\n    ignore-urls:\r\n      - /auth/**\r\n  security:\r\n    white-urls:\r\n      - /auth/**\r\n      - /nacos/**',
        '6f5721b6e428559b0e578d6a3d48f96c', '2022-10-08 08:11:34', '2022-10-08 08:11:34', NULL, '0:0:0:0:0:0:0:1', '',
        '', NULL, NULL, NULL, 'yaml', NULL, '');
/*!40000 ALTER TABLE `config_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_aggr`
--

DROP TABLE IF EXISTS `config_info_aggr`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_aggr`
(
    `id`           bigint                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
    `datum_id`     varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'datum_id',
    `content`      longtext COLLATE utf8mb3_bin     NOT NULL COMMENT '内容',
    `gmt_modified` datetime                         NOT NULL COMMENT '修改时间',
    `app_name`     varchar(128) COLLATE utf8mb3_bin DEFAULT NULL,
    `tenant_id`    varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT '租户字段',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfoaggr_datagrouptenantdatum` (`data_id`, `group_id`, `tenant_id`, `datum_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='增加租户字段';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_aggr`
--

LOCK TABLES `config_info_aggr` WRITE;
/*!40000 ALTER TABLE `config_info_aggr`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_aggr`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_beta`
--

DROP TABLE IF EXISTS `config_info_beta`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_beta`
(
    `id`                 bigint                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`            varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
    `group_id`           varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
    `app_name`           varchar(128) COLLATE utf8mb3_bin          DEFAULT NULL COMMENT 'app_name',
    `content`            longtext COLLATE utf8mb3_bin     NOT NULL COMMENT 'content',
    `beta_ips`           varchar(1024) COLLATE utf8mb3_bin         DEFAULT NULL COMMENT 'betaIps',
    `md5`                varchar(32) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`         datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`       datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`           text COLLATE utf8mb3_bin COMMENT 'source user',
    `src_ip`             varchar(50) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'source ip',
    `tenant_id`          varchar(128) COLLATE utf8mb3_bin          DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text COLLATE utf8mb3_bin         NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfobeta_datagrouptenant` (`data_id`, `group_id`, `tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='config_info_beta';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_beta`
--

LOCK TABLES `config_info_beta` WRITE;
/*!40000 ALTER TABLE `config_info_beta`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_beta`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_info_tag`
--

DROP TABLE IF EXISTS `config_info_tag`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_info_tag`
(
    `id`           bigint                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `data_id`      varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
    `group_id`     varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
    `tenant_id`    varchar(128) COLLATE utf8mb3_bin          DEFAULT '' COMMENT 'tenant_id',
    `tag_id`       varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_id',
    `app_name`     varchar(128) COLLATE utf8mb3_bin          DEFAULT NULL COMMENT 'app_name',
    `content`      longtext COLLATE utf8mb3_bin     NOT NULL COMMENT 'content',
    `md5`          varchar(32) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'md5',
    `gmt_create`   datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified` datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    `src_user`     text COLLATE utf8mb3_bin COMMENT 'source user',
    `src_ip`       varchar(50) COLLATE utf8mb3_bin           DEFAULT NULL COMMENT 'source ip',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_configinfotag_datagrouptenanttag` (`data_id`, `group_id`, `tenant_id`, `tag_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='config_info_tag';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_info_tag`
--

LOCK TABLES `config_info_tag` WRITE;
/*!40000 ALTER TABLE `config_info_tag`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `config_info_tag`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `config_tags_relation`
--

DROP TABLE IF EXISTS `config_tags_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `config_tags_relation`
(
    `id`        bigint                           NOT NULL COMMENT 'id',
    `tag_name`  varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'tag_name',
    `tag_type`  varchar(64) COLLATE utf8mb3_bin  DEFAULT NULL COMMENT 'tag_type',
    `data_id`   varchar(255) COLLATE utf8mb3_bin NOT NULL COMMENT 'data_id',
    `group_id`  varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'group_id',
    `tenant_id` varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
    `nid`       bigint                           NOT NULL AUTO_INCREMENT,
    PRIMARY KEY (`nid`),
    UNIQUE KEY `uk_configtagrelation_configidtag` (`id`, `tag_name`, `tag_type`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='config_tag_relation';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `config_tags_relation`
--

LOCK TABLES `config_tags_relation` WRITE;
/*!40000 ALTER TABLE `config_tags_relation`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `config_tags_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `group_capacity`
--

DROP TABLE IF EXISTS `group_capacity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `group_capacity`
(
    `id`                bigint unsigned                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `group_id`          varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Group ID，空字符表示整个集群',
    `quota`             int unsigned                     NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int unsigned                     NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int unsigned                     NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int unsigned                     NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数，，0表示使用默认值',
    `max_aggr_size`     int unsigned                     NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned                     NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_group_id` (`group_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='集群、各Group容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `group_capacity`
--

LOCK TABLES `group_capacity` WRITE;
/*!40000 ALTER TABLE `group_capacity`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `group_capacity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `his_config_info`
--

DROP TABLE IF EXISTS `his_config_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `his_config_info`
(
    `id`                 bigint unsigned                  NOT NULL,
    `nid`                bigint unsigned                  NOT NULL AUTO_INCREMENT,
    `data_id`            varchar(255) COLLATE utf8mb3_bin NOT NULL,
    `group_id`           varchar(128) COLLATE utf8mb3_bin NOT NULL,
    `app_name`           varchar(128) COLLATE utf8mb3_bin          DEFAULT NULL COMMENT 'app_name',
    `content`            longtext COLLATE utf8mb3_bin     NOT NULL,
    `md5`                varchar(32) COLLATE utf8mb3_bin           DEFAULT NULL,
    `gmt_create`         datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `gmt_modified`       datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP,
    `src_user`           text COLLATE utf8mb3_bin,
    `src_ip`             varchar(50) COLLATE utf8mb3_bin           DEFAULT NULL,
    `op_type`            char(10) COLLATE utf8mb3_bin              DEFAULT NULL,
    `tenant_id`          varchar(128) COLLATE utf8mb3_bin          DEFAULT '' COMMENT '租户字段',
    `encrypted_data_key` text COLLATE utf8mb3_bin         NOT NULL COMMENT '秘钥',
    PRIMARY KEY (`nid`),
    KEY `idx_gmt_create` (`gmt_create`),
    KEY `idx_gmt_modified` (`gmt_modified`),
    KEY `idx_did` (`data_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 780
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='多租户改造';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `his_config_info`
--

LOCK TABLES `his_config_info` WRITE;
/*!40000 ALTER TABLE `his_config_info`
    DISABLE KEYS */;
INSERT INTO `his_config_info`
VALUES (27, 596, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 14:31:55', '2022-09-27 06:31:55', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 597, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 14:32:53', '2022-09-27 06:32:53', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 598, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 14:44:09', '2022-09-27 06:44:10', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 599, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 15:11:25', '2022-09-27 07:11:26', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 600, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 15:18:41', '2022-09-27 07:18:42', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 601, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 15:28:31', '2022-09-27 07:28:32', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 602, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 15:33:30', '2022-09-27 07:33:31', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 603, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 16:19:13', '2022-09-27 08:19:13', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 604, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 16:39:35', '2022-09-27 08:39:35', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 605, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 16:51:53', '2022-09-27 08:51:53', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 606, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 16:56:31', '2022-09-27 08:56:32', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 607, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 16:57:43', '2022-09-27 08:57:44', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 608, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 17:01:34', '2022-09-27 09:01:35', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 609, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 17:07:17', '2022-09-27 09:07:17', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 610, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 17:08:14', '2022-09-27 09:08:15', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 611, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-27 17:09:59', '2022-09-27 09:09:59', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 612, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-27 17:11:23', '2022-09-27 09:11:23', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 613, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-28 11:16:44', '2022-09-28 03:16:45', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 614, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-28 16:26:22', '2022-09-28 08:26:22', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 615, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-28 16:28:10', '2022-09-28 08:28:11', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 616, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 16:37:57', '2022-09-28 08:37:58', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 617, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 16:39:47', '2022-09-28 08:39:47', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 618, 'gateway-route', 'DEFAULT_GROUP', '', '[]\n', '58e0494c51d30eb3494f7c9198986bb9', '2022-09-28 16:40:59',
        '2022-09-28 08:40:59', NULL, '127.0.0.1', 'U', '', ''),
       (1, 619, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\"}]',
        '9e7ee3c0db7e65375637f277c9759b38', '2022-09-28 16:53:08', '2022-09-28 08:53:09', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 620, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 16:53:59', '2022-09-28 08:53:59', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 621, 'gateway-route', 'DEFAULT_GROUP', '', '[]', 'd751713988987e9331980363e24189ce', '2022-09-28 16:54:29',
        '2022-09-28 08:54:30', NULL, '127.0.0.1', 'U', '', ''),
       (1, 622, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\"}]',
        '9e7ee3c0db7e65375637f277c9759b38', '2022-09-28 16:54:59', '2022-09-28 08:55:00', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 623, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 16:55:10', '2022-09-28 08:55:10', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 624, 'gateway-route', 'DEFAULT_GROUP', '', '[]', 'd751713988987e9331980363e24189ce', '2022-09-28 16:55:32',
        '2022-09-28 08:55:32', NULL, '127.0.0.1', 'U', '', ''),
       (1, 625, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\",\"order\":-1}]',
        '96ca691bfe658fb8ab7de4fd39dd5ba5', '2022-09-28 17:06:40', '2022-09-28 09:06:40', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 626, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 17:07:04', '2022-09-28 09:07:04', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 627, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-28 17:08:21', '2022-09-28 09:08:21', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 628, 'gateway-route', 'DEFAULT_GROUP', '', '[]', 'd751713988987e9331980363e24189ce', '2022-09-28 17:08:29',
        '2022-09-28 09:08:30', NULL, '127.0.0.1', 'U', '', ''),
       (1, 629, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\",\"order\":-1}]',
        '96ca691bfe658fb8ab7de4fd39dd5ba5', '2022-09-28 17:09:34', '2022-09-28 09:09:34', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (1, 630, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\",\"order\":-1}]',
        '96ca691bfe658fb8ab7de4fd39dd5ba5', '2022-09-28 17:09:47', '2022-09-28 09:09:48', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 631, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 17:11:53', '2022-09-28 09:11:53', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 632, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 17:12:45', '2022-09-28 09:12:45', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 633, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 17:14:22', '2022-09-28 09:14:23', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 634, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-28 17:15:36', '2022-09-28 09:15:37', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 635, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-28 17:48:26', '2022-09-28 09:48:26', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 636, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 09:43:41', '2022-09-29 01:43:42', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 637, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 10:58:06', '2022-09-29 02:58:07', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 638, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 10:58:29', '2022-09-29 02:58:29', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 639, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:00:29', '2022-09-29 03:00:30', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 640, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:01:02', '2022-09-29 03:01:03', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 641, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\",\"order\":-2}]',
        '7d9686f6641fa78affb83e331eb9f707', '2022-09-29 11:01:38', '2022-09-29 03:01:38', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 642, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 11:02:39', '2022-09-29 03:02:40', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 643, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:04:40', '2022-09-29 03:04:40', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 644, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:13:19', '2022-09-29 03:13:20', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 645, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:14:40', '2022-09-29 03:14:40', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 646, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:16:33', '2022-09-29 03:16:33', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 647, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:24:21', '2022-09-29 03:24:21', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 648, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:28:59', '2022-09-29 03:28:59', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 649, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:35:24', '2022-09-29 03:35:24', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 650, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:38:03', '2022-09-29 03:38:03', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 651, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 11:38:46', '2022-09-29 03:38:46', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 652, 'gateway-route', 'DEFAULT_GROUP', '', '[]', 'd751713988987e9331980363e24189ce', '2022-09-29 11:38:46',
        '2022-09-29 03:38:46', NULL, '127.0.0.1', 'U', '', ''),
       (27, 653, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 13:42:05', '2022-09-29 05:42:06', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 654, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 13:42:55', '2022-09-29 05:42:56', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 655, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 13:56:28', '2022-09-29 05:56:28', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 656, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 14:02:25', '2022-09-29 06:02:26', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 657, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 14:04:32', '2022-09-29 06:04:32', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 658, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 14:17:00', '2022-09-29 06:17:00', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 659, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 14:26:28', '2022-09-29 06:26:28', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 660, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 16:43:45', '2022-09-29 08:43:46', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 661, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 16:47:25', '2022-09-29 08:47:25', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 662, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 16:50:36', '2022-09-29 08:50:37', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 663, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:02:02', '2022-09-29 09:02:02', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 664, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 17:03:22', '2022-09-29 09:03:23', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 665, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:14:03', '2022-09-29 09:14:04', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 666, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:33:13', '2022-09-29 09:33:13', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 667, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:34:57', '2022-09-29 09:34:57', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 668, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 17:37:32', '2022-09-29 09:37:33', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 669, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:44:07', '2022-09-29 09:44:08', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 670, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:46:20', '2022-09-29 09:46:21', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 671, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 17:50:34', '2022-09-29 09:50:34', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 672, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-29 18:22:50', '2022-09-29 10:22:51', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 673, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-29 18:23:21', '2022-09-29 10:23:22', NULL, '127.0.0.1', 'U', '',
        ''),
       (1, 674, 'gateway-route', 'DEFAULT_GROUP', '',
        '[{\"predicates\":[{\"args\":{\"pattern\":\"/system-server/**\"},\"name\":\"Path\"}],\"id\":\"system-server\",\"uri\":\"grayLb://system-server\",\"order\":-1}]',
        'c69e749f01bcceb8efae8c9c09ca924b', '2022-09-29 18:23:49', '2022-09-29 10:23:49', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 675, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 15:39:03', '2022-09-30 07:39:03', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 676, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 15:39:53', '2022-09-30 07:39:53', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 677, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-30 15:56:20', '2022-09-30 07:56:21', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 678, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 16:03:03', '2022-09-30 08:03:03', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 679, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 16:11:05', '2022-09-30 08:11:05', NULL, '127.0.0.1', 'U', '',
        ''),
       (0, 680, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\r\n  servlet:\r\n    multipart:\r\n      max-request-size: 5MB\r\n      max-file-size: 100MB\r\n  jackson:\r\n    time-zone: GMT+8\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    serialization:\r\n      # 格式化输出\r\n      INDENT_OUTPUT: false\r\n      # 忽略无法转换的对象\r\n      fail_on_empty_beans: false\r\n    deserialization:\r\n      # 允许对象忽略json中不存在的属性\r\n      fail_on_unknown_properties: false\r\n  messages:\r\n    basename: i18n/messages\r\n    encoding: utf-8\r\n    fallback-to-system-locale: true\r\n  # redis配置\r\n  redis:\r\n    host: ${REDIS_HOST:127.0.0.1}\r\n    port: ${REDIS_PORT:6379}\r\n    database: ${REDIS_DATABASE:0}\r\n    timeout: 10s\r\n    lettuce:\r\n      pool:\r\n        # 连接池最大连接数\r\n        max-active: 200\r\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\r\n        max-wait: -1ms\r\n        # 连接池中的最大空闲连接\r\n        max-idle: 10\r\n        # 连接池中的最小空闲连接\r\n        min-idle: 0\r\n  cache:\r\n    type: redis\r\n  datasource:\r\n    druid:\r\n      initial-size: 5 # 初始连接数\r\n      min-idle: 10 # 最小连接池数量\r\n      max-active: 20 # 最大连接池数量\r\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\r\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\r\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\r\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\r\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\r\n      test-while-idle: true\r\n      test-on-borrow: false\r\n      test-on-return: false\r\n      web-stat-filter:\r\n        enabled: true\r\n      stat-view-servlet:\r\n        enabled: true\r\n        allow: # 设置白名单，不填则允许所有访问\r\n        url-pattern: /druid/*\r\n        login-username: druid\r\n        login-password: druid\r\n      filter:\r\n        stat:\r\n          enabled: true\r\n          log-slow-sql: true # 慢 SQL 记录\r\n          slow-sql-millis: 100\r\n          merge-sql: true\r\n        wall:\r\n          config:\r\n            multi-statement-allow: true\r\n  # spring cloud配置\r\n  cloud:\r\n    nacos:\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n      config:\r\n        file-extension: yaml\r\n    sentinel:\r\n      enabled: true\r\n      eager: true\r\n      transport:\r\n        port: ${SENTINEL_PORT:8719}\r\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\r\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.99}\r\n  main:\r\n    allow-bean-definition-overriding: true\r\n\r\nfeign:\r\n  ## 启用对 feign 的 Sentinel 支持\r\n  sentinel:\r\n    enabled: true\r\n  client:\r\n    config:\r\n      default:\r\n        connect-timeout: 5000\r\n        read-timeout: 5000\r\n        logger-level: full\r\n  autoconfiguration:\r\n    jackson:\r\n      enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true\r\n    response:\r\n      enabled: true\r\n  metrics:\r\n    enabled: true\r\n\r\n# 端点配置\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \"*\"\r\n    jmx:\r\n      exposure:\r\n        include: \"*\"\r\n  endpoint:\r\n    health:\r\n      show-details: always\r\n\r\n# 文档配置\r\nknife4j:\r\n  enable: true\r\n  production: false\r\n  cors: true\r\n\r\n# mybatis plus配置\r\nmybatis-plus:\r\n  global-config:\r\n    db-config:\r\n      id-type: auto\r\n  configuration:\r\n    map-underscore-to-camel-case: true\r\n    call-setters-on-nulls: true\r\n  check-config-location: false\r\n\r\n\r\nsa-token:\r\n  # jwt秘钥\r\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\r\n  # token名称 (同时也是cookie名称)\r\n  token-name: Authorization\r\n  # token有效期，单位s 默认30天, -1代表永不过期\r\n  timeout: 2592000\r\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\r\n  activity-timeout: 1800\r\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\r\n  is-concurrent: true\r\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\r\n  is-share: true\r\n  # token风格\r\n  token-style: uuid\r\n  # 是否输出操作日志\r\n  is-log: true\r\n\r\n\r\nlogging:\r\n  file:\r\n    path: /var/logs\r\n  level:\r\n    com.matrix.mapper: debug\r\n    com.matrix.api.**: debug\r\n\r\nseata:\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n      group: SEATA_GROUP\r\n      data-id: seataServer.properties\r\n      namespace:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      group: SEATA_GROUP\r\n      cluster: default\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n',
        'c70a596b99c5dcf3d844c5759c50b1cb', '2022-09-30 16:30:56', '2022-09-30 08:30:56', NULL, '0:0:0:0:0:0:0:1', 'I',
        '', ''),
       (27, 681, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-09-30 16:56:01', '2022-09-30 08:56:01', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 682, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 16:57:56', '2022-09-30 08:57:57', NULL, '127.0.0.1', 'U', '',
        ''),
       (484, 683, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\r\n  servlet:\r\n    multipart:\r\n      max-request-size: 5MB\r\n      max-file-size: 100MB\r\n  jackson:\r\n    time-zone: GMT+8\r\n    date-format: yyyy-MM-dd HH:mm:ss\r\n    serialization:\r\n      # 格式化输出\r\n      INDENT_OUTPUT: false\r\n      # 忽略无法转换的对象\r\n      fail_on_empty_beans: false\r\n    deserialization:\r\n      # 允许对象忽略json中不存在的属性\r\n      fail_on_unknown_properties: false\r\n  messages:\r\n    basename: i18n/messages\r\n    encoding: utf-8\r\n    fallback-to-system-locale: true\r\n  # redis配置\r\n  redis:\r\n    host: ${REDIS_HOST:127.0.0.1}\r\n    port: ${REDIS_PORT:6379}\r\n    database: ${REDIS_DATABASE:0}\r\n    timeout: 10s\r\n    lettuce:\r\n      pool:\r\n        # 连接池最大连接数\r\n        max-active: 200\r\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\r\n        max-wait: -1ms\r\n        # 连接池中的最大空闲连接\r\n        max-idle: 10\r\n        # 连接池中的最小空闲连接\r\n        min-idle: 0\r\n  cache:\r\n    type: redis\r\n  datasource:\r\n    druid:\r\n      initial-size: 5 # 初始连接数\r\n      min-idle: 10 # 最小连接池数量\r\n      max-active: 20 # 最大连接池数量\r\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\r\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\r\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\r\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\r\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\r\n      test-while-idle: true\r\n      test-on-borrow: false\r\n      test-on-return: false\r\n      web-stat-filter:\r\n        enabled: true\r\n      stat-view-servlet:\r\n        enabled: true\r\n        allow: # 设置白名单，不填则允许所有访问\r\n        url-pattern: /druid/*\r\n        login-username: druid\r\n        login-password: druid\r\n      filter:\r\n        stat:\r\n          enabled: true\r\n          log-slow-sql: true # 慢 SQL 记录\r\n          slow-sql-millis: 100\r\n          merge-sql: true\r\n        wall:\r\n          config:\r\n            multi-statement-allow: true\r\n  # spring cloud配置\r\n  cloud:\r\n    nacos:\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n      config:\r\n        file-extension: yaml\r\n    sentinel:\r\n      enabled: true\r\n      eager: true\r\n      transport:\r\n        port: ${SENTINEL_PORT:8719}\r\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\r\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.99}\r\n  main:\r\n    allow-bean-definition-overriding: true\r\n\r\nfeign:\r\n  ## 启用对 feign 的 Sentinel 支持\r\n  sentinel:\r\n    enabled: true\r\n  client:\r\n    config:\r\n      default:\r\n        connect-timeout: 5000\r\n        read-timeout: 5000\r\n        logger-level: full\r\n  autoconfiguration:\r\n    jackson:\r\n      enabled: true\r\n  compression:\r\n    request:\r\n      enabled: true\r\n    response:\r\n      enabled: true\r\n  metrics:\r\n    enabled: true\r\n\r\n# 端点配置\r\nmanagement:\r\n  endpoints:\r\n    web:\r\n      exposure:\r\n        include: \"*\"\r\n    jmx:\r\n      exposure:\r\n        include: \"*\"\r\n  endpoint:\r\n    health:\r\n      show-details: always\r\n\r\n# 文档配置\r\nknife4j:\r\n  enable: true\r\n  production: false\r\n  cors: true\r\n\r\n# mybatis plus配置\r\nmybatis-plus:\r\n  global-config:\r\n    db-config:\r\n      id-type: auto\r\n  configuration:\r\n    map-underscore-to-camel-case: true\r\n    call-setters-on-nulls: true\r\n  check-config-location: false\r\n\r\n\r\nsa-token:\r\n  # jwt秘钥\r\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\r\n  # token名称 (同时也是cookie名称)\r\n  token-name: Authorization\r\n  # token有效期，单位s 默认30天, -1代表永不过期\r\n  timeout: 2592000\r\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\r\n  activity-timeout: 1800\r\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\r\n  is-concurrent: true\r\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\r\n  is-share: true\r\n  # token风格\r\n  token-style: uuid\r\n  # 是否输出操作日志\r\n  is-log: true\r\n\r\n\r\nlogging:\r\n  file:\r\n    path: /var/logs\r\n  level:\r\n    com.matrix.mapper: debug\r\n    com.matrix.api.**: debug\r\n\r\nseata:\r\n  config:\r\n    type: nacos\r\n    nacos:\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n      group: SEATA_GROUP\r\n      data-id: seataServer.properties\r\n      namespace:\r\n  registry:\r\n    type: nacos\r\n    nacos:\r\n      application: seata-server\r\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n      group: SEATA_GROUP\r\n      cluster: default\r\n      username: ${NACOS_USERNAME:nacos}\r\n      password: ${NACOS_PASSWD:nacos}\r\n',
        'c70a596b99c5dcf3d844c5759c50b1cb', '2022-09-30 17:00:18', '2022-09-30 09:00:19', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 684, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:02:02', '2022-09-30 09:02:02', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 685, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:03:04', '2022-09-30 09:03:04', NULL, '127.0.0.1', 'U', '',
        ''),
       (484, 686, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.99}\n  main:\n    allow-bean-definition-overriding: true\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug\n\nseata:\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}\n      group: SEATA_GROUP\n      data-id: seataServer.properties\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      group: SEATA_GROUP\n      cluster: default\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}\n',
        'c0077316769a43b987a7b56084568d93', '2022-09-30 17:03:06', '2022-09-30 09:03:06', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 687, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:09:37', '2022-09-30 09:09:37', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 688, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:13:19', '2022-09-30 09:13:19', NULL, '127.0.0.1', 'U', '',
        ''),
       (484, 689, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.101}\n  main:\n    allow-bean-definition-overriding: true\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug\n\nseata:\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}\n      group: SEATA_GROUP\n      data-id: seataServer.properties\n      namespace:\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      group: SEATA_GROUP\n      cluster: default\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}\n',
        '652b3cdfb6c67c7f2eea2ffd3463421c', '2022-09-30 17:13:58', '2022-09-30 09:13:59', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (0, 690, 'api-gateway', 'DEFAULT_GROUP', '',
        'spring:\r\n  cloud:\r\n    ## sentinel配置\r\n    sentinel:\r\n      scg:\r\n        fallback:\r\n          content-type: application/json\r\n          mode: response\r\n          response-status: 429\r\n          response-body: \'{\"message\":\"Too Many Requests\"}\'\r\n      datasource:\r\n        gw-flow:\r\n          nacos:\r\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n            username: ${NACOS_USERNAME:nacos}\r\n            password: ${NACOS_PASSWD:nacos}\r\n            data-id: gateway-flow\r\n            group-id: DEFAULT_GROUP\r\n            data-type: json\r\n            rule-type: gw_flow\r\n        gw-api-group:\r\n          nacos:\r\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n            username: ${NACOS_USERNAME:nacos}\r\n            password: ${NACOS_PASSWD:nacos}\r\n            data-id: gateway-api-group\r\n            group-id: DEFAULT_GROUP\r\n            data-type: json\r\n            rule-type: gw_api_group\r\n    ## gateway配置\r\n    gateway:\r\n      globalcors:\r\n        ## 配置跨域\r\n        cors-configurations:\r\n          \'[/**]\':\r\n            allowed-origins: \"*\"\r\n            allowed-methods: \"*\"\r\n            allowed-headers: \"*\"\r\n      discovery:\r\n        locator:\r\n          enabled: true\r\n          lower-case-service-id: true\r\n          url-expression: \"\'grayLb://\'+serviceId\"\r\n      loadbalancer:\r\n        use404: true\r\n\r\n',
        '0d48b8557233ed3b73b0fc8daf503718', '2022-09-30 17:18:18', '2022-09-30 09:18:19', NULL, '0:0:0:0:0:0:0:1', 'I',
        '', ''),
       (27, 691, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:18:58', '2022-09-30 09:18:59', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 692, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:22:49', '2022-09-30 09:22:50', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 693, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:25:13', '2022-09-30 09:25:14', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 694, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:34:07', '2022-09-30 09:34:08', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 695, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 17:54:43', '2022-09-30 09:54:44', NULL, '127.0.0.1', 'U', '',
        ''),
       (27, 696, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 18:23:19', '2022-09-30 10:23:20', NULL, '192.168.2.101', 'U',
        '', ''),
       (27, 697, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 18:24:19', '2022-09-30 10:24:19', NULL, '192.168.2.101', 'U',
        '', ''),
       (27, 698, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-09-30 18:26:34', '2022-09-30 10:26:34', NULL, '192.168.2.101', 'U',
        '', ''),
       (27, 699, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:42:57', '2022-10-08 01:42:58', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 700, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:44:13', '2022-10-08 01:44:14', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 701, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:47:22', '2022-10-08 01:47:22', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 702, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.101}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug\n',
        '2603ecb3819aa71fd8909a1a16e4a081', '2022-10-08 09:50:15', '2022-10-08 01:50:16', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 703, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 09:53:08', '2022-10-08 01:53:09', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 704, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.101}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug\n\nseata:\n  config:\n    type: nacos\n    nacos:\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}\n      group: SEATA_GROUP\n      data-id: seataServer.properties\n  registry:\n    type: nacos\n    nacos:\n      application: seata-server\n      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n      group: SEATA_GROUP\n      cluster: default\n      username: ${NACOS_USERNAME:nacos}\n      password: ${NACOS_PASSWD:nacos}',
        '76f37143b51f3cd1649f0b8024b3f1a0', '2022-10-08 09:54:09', '2022-10-08 01:54:09', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 705, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:54:14', '2022-10-08 01:54:15', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 706, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:55:45', '2022-10-08 01:55:46', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 707, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 09:59:01', '2022-10-08 01:59:02', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 708, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 09:59:53', '2022-10-08 01:59:54', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 709, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.101}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug',
        '65d845e734d6fcf336413f1f4c115fe7', '2022-10-08 10:00:44', '2022-10-08 02:00:44', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 710, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:01:45', '2022-10-08 02:01:45', NULL, '192.168.2.134', 'U',
        '', ''),
       (494, 711, 'api-gateway', 'DEFAULT_GROUP', '',
        'spring:\r\n  cloud:\r\n    ## sentinel配置\r\n    sentinel:\r\n      scg:\r\n        fallback:\r\n          content-type: application/json\r\n          mode: response\r\n          response-status: 429\r\n          response-body: \'{\"message\":\"Too Many Requests\"}\'\r\n      datasource:\r\n        gw-flow:\r\n          nacos:\r\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n            username: ${NACOS_USERNAME:nacos}\r\n            password: ${NACOS_PASSWD:nacos}\r\n            data-id: gateway-flow\r\n            group-id: DEFAULT_GROUP\r\n            data-type: json\r\n            rule-type: gw_flow\r\n        gw-api-group:\r\n          nacos:\r\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\r\n            username: ${NACOS_USERNAME:nacos}\r\n            password: ${NACOS_PASSWD:nacos}\r\n            data-id: gateway-api-group\r\n            group-id: DEFAULT_GROUP\r\n            data-type: json\r\n            rule-type: gw_api_group\r\n    ## gateway配置\r\n    gateway:\r\n      globalcors:\r\n        ## 配置跨域\r\n        cors-configurations:\r\n          \'[/**]\':\r\n            allowed-origins: \"*\"\r\n            allowed-methods: \"*\"\r\n            allowed-headers: \"*\"\r\n      discovery:\r\n        locator:\r\n          enabled: true\r\n          lower-case-service-id: true\r\n          url-expression: \"\'grayLb://\'+serviceId\"\r\n      loadbalancer:\r\n        use404: true\r\n\r\n',
        '0d48b8557233ed3b73b0fc8daf503718', '2022-10-08 10:31:45', '2022-10-08 02:31:45', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 712, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:33:21', '2022-10-08 02:33:22', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 713, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 10:38:24', '2022-10-08 02:38:25', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 714, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n        client-ip: ${SENTINEL_CLIENT_IP:192.168.2.101}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '954a76b0aea17d45920272e1f925ffa1', '2022-10-08 10:38:42', '2022-10-08 02:38:43', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 715, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:39:18', '2022-10-08 02:39:18', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 716, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:46:57', '2022-10-08 02:46:58', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 717, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:48:13', '2022-10-08 02:48:14', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 718, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '8a5b1c5bca86608c72ae0fa8a007febe', '2022-10-08 10:49:50', '2022-10-08 02:49:51', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 719, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:51:17', '2022-10-08 02:51:18', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 720, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  # spring cloud配置\n  cloud:\n    sentinel:\n      enabled: true\n      eager: false\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        'fb2fed443ba60453d077079b098c8bd1', '2022-10-08 10:53:08', '2022-10-08 02:53:08', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 721, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:53:47', '2022-10-08 02:53:48', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 722, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 10:54:37', '2022-10-08 02:54:37', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 723, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 10:57:37', '2022-10-08 02:57:38', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 724, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 10:59:55', '2022-10-08 02:59:55', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 725, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:04:10', '2022-10-08 03:04:10', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 726, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:05:45', '2022-10-08 03:05:46', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 727, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:08:08', '2022-10-08 03:08:08', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 728, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:10:37', '2022-10-08 03:10:38', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 729, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '5bb7230185825048ea7fb4387bdaade7', '2022-10-08 11:13:08', '2022-10-08 03:13:09', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (484, 730, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        'a3a08c495cec164cec72466f6c67157d', '2022-10-08 11:13:57', '2022-10-08 03:13:58', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 731, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:14:23', '2022-10-08 03:14:24', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 732, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:16:10', '2022-10-08 03:16:10', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 733, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 11:19:02', '2022-10-08 03:19:03', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 734, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '4581bf5bc9971a770251c84d4d45df1f', '2022-10-08 11:38:04', '2022-10-08 03:38:05', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 735, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '4f8fd38172c5167ee1a15ead4154688d', '2022-10-08 11:39:09', '2022-10-08 03:39:10', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 736, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 11:41:01', '2022-10-08 03:41:02', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 737, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 15:21:56', '2022-10-08 07:21:56', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 738, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 15:25:24', '2022-10-08 07:25:25', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 739, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 15:28:53', '2022-10-08 07:28:53', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 740, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 15:32:15', '2022-10-08 07:32:15', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 741, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /api-gateway/docapp/**, /api-gateway/docApp/**,\n  /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c36b69edee032882d8b9891f9995341d', '2022-10-08 15:39:59', '2022-10-08 07:39:59', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 742, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: ${SENTINEL_PORT:8719}\n        dashboard: ${SENTINEL_DASHBOARD:localhost:8088}\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        'a2926bd31da0ab39df3e03bd898ad00d', '2022-10-08 15:40:09', '2022-10-08 07:40:10', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (484, 743, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8719\n        dashboard: localhost:8088\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '3deb8105bd849e319143bf8d17e65ea5', '2022-10-08 15:40:45', '2022-10-08 07:40:45', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (484, 744, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8720\n        dashboard: localhost:8088\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '68809293ef149ea6d3c25474c6b68ec2', '2022-10-08 15:41:09', '2022-10-08 07:41:10', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 745, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /api-gateway/docApp/**, /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'e96c7aec4b47140865e984ea1bf9ea38', '2022-10-08 15:42:39', '2022-10-08 07:42:40', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 746, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /api-gateway/docApp/**, /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '745f329f9841bfc92586be4f5e765080', '2022-10-08 15:46:07', '2022-10-08 07:46:07', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 747, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/shared/**,\n  /api-gateway/docApp/**, /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        '7dc3eb78e04ada35b9a661105223b1d6', '2022-10-08 15:47:16', '2022-10-08 07:47:17', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 748, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/shared/**,\n  /api-gateway/docApp/**, /system-server/resource/admin/**, /system-server/resource/role/**]\n',
        'c7ce8bfd256676c8e6591b5ba4609b77', '2022-10-08 15:48:42', '2022-10-08 07:48:42', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 749, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 15:49:56', '2022-10-08 07:49:56', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 750, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 15:50:55', '2022-10-08 07:50:56', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 751, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 15:52:07', '2022-10-08 07:52:08', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 752, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 15:53:55', '2022-10-08 07:53:56', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 753, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8719\n        dashboard: localhost:8088\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        '3deb8105bd849e319143bf8d17e65ea5', '2022-10-08 15:58:03', '2022-10-08 07:58:03', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 754, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 15:58:41', '2022-10-08 07:58:41', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 755, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8719\n        dashboard: localhost:8088\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        'bbbe0b568c067dd79240e2a7e44474eb', '2022-10-08 16:03:13', '2022-10-08 08:03:14', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 756, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:03:46', '2022-10-08 08:03:47', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 757, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:05:30', '2022-10-08 08:05:31', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 758, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8719\n        dashboard: localhost:8088\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true\n\n\nlogging:\n  file:\n    path: /var/logs\n  level:\n    com.matrix.mapper: debug\n    com.matrix.api.**: debug',
        'ca1b32dd3b93ae22ba86ba56af07542a', '2022-10-08 16:06:50', '2022-10-08 08:06:50', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 759, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:09:41', '2022-10-08 08:09:41', NULL, '192.168.2.134', 'U',
        '', ''),
       (0, 760, 'system-server-dev.yaml', 'DEFAULT_GROUP', '',
        'spring:\r\n  datasource:\r\n    driver-class-name: com.mysql.cj.jdbc.Driver\r\n    url: jdbc:mysql://localhost:3306/matrix?allowPublicKeyRetrieval=true&useUnicode=true&characterEncoding=UTF-8&autoReconnect=true&useSSL=false&zeroDateTimeBehavior=convertToNull&serverTimezone=Asia/Shanghai\r\n    username: root\r\n    password: 123456\r\n\r\nmatrix:\r\n  swagger:\r\n    enable: true\r\n    name: 系统服务\r\n    version: 1.0.0\r\n    description: 系统服务接口文档\r\n  tenant:\r\n    enable: true\r\n    ignoreTables:\r\n      - tenant\r\n    ignore-urls:\r\n      - /auth/**\r\n  security:\r\n    white-urls:\r\n      - /auth/**\r\n      - /nacos/**',
        '6f5721b6e428559b0e578d6a3d48f96c', '2022-10-08 16:11:34', '2022-10-08 08:11:34', NULL, '0:0:0:0:0:0:0:1', 'I',
        '', ''),
       (27, 761, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:12:05', '2022-10-08 08:12:05', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 762, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:13:46', '2022-10-08 08:13:47', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 763, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 16:20:28', '2022-10-08 08:20:29', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 764, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:23:12', '2022-10-08 08:23:13', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 765, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:23:49', '2022-10-08 08:23:49', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 766, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 16:24:46', '2022-10-08 08:24:47', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 767, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:26:06', '2022-10-08 08:26:07', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 768, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 16:28:17', '2022-10-08 08:28:18', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 769, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:29:37', '2022-10-08 08:29:38', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 770, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:31:27', '2022-10-08 08:31:28', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 771, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 16:33:33', '2022-10-08 08:33:34', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 772, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 16:42:11', '2022-10-08 08:42:12', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 773, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 16:46:49', '2022-10-08 08:46:49', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 774, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/system-server/auth/**, /system-server/nacos/**, /api-gateway/docapp/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '39dd7e34946efbe24b78dd7c98c69022', '2022-10-08 18:21:25', '2022-10-08 10:21:25', NULL, '192.168.2.134', 'U',
        '', ''),
       (494, 775, 'api-gateway', 'DEFAULT_GROUP', '',
        'spring:\n  cloud:\n    ## sentinel配置\n    sentinel:\n      filter:\n        enabled: false\n      scg:\n        fallback:\n          content-type: application/json\n          mode: response\n          response-status: 429\n          response-body: \'{\"message\":\"Too Many Requests\"}\'\n      datasource:\n        gw-flow:\n          nacos:\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n            username: ${NACOS_USERNAME:nacos}\n            password: ${NACOS_PASSWD:nacos}\n            data-id: gateway-flow\n            group-id: DEFAULT_GROUP\n            data-type: json\n            rule-type: gw_flow\n        gw-api-group:\n          nacos:\n            server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}\n            username: ${NACOS_USERNAME:nacos}\n            password: ${NACOS_PASSWD:nacos}\n            data-id: gateway-api-group\n            group-id: DEFAULT_GROUP\n            data-type: json\n            rule-type: gw_api_group\n    ## gateway配置\n    gateway:\n      globalcors:\n        ## 配置跨域\n        cors-configurations:\n          \'[/**]\':\n            allowed-origins: \"*\"\n            allowed-methods: \"*\"\n            allowed-headers: \"*\"\n      discovery:\n        locator:\n          enabled: true\n          lower-case-service-id: true\n          url-expression: \"\'grayLb://\'+serviceId\"\n      loadbalancer:\n        use404: true\n\n',
        'b092770da6d2226407a797a0cdda7dd0', '2022-10-08 18:24:24', '2022-10-08 10:24:24', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', ''),
       (27, 776, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 18:26:16', '2022-10-08 10:26:16', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 777, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 18:27:17', '2022-10-08 10:27:18', NULL, '192.168.2.134', 'U',
        '', ''),
       (27, 778, 'matrix-security', 'DEFAULT_GROUP', '',
        '!!com.matrix.properties.SecurityProperties\nwhiteUrls: [/api-gateway/docapp/**, /system-server/auth/**, /system-server/nacos/**,\n  /system-server/nacos/shared/**, /api-gateway/docApp/**, /system-server/resource/admin/**,\n  /system-server/resource/role/**]\n',
        '1131f9a8acc4fb16646785b1965a27a4', '2022-10-08 18:29:14', '2022-10-08 10:29:14', NULL, '192.168.2.134', 'U',
        '', ''),
       (484, 779, 'application-common', 'DEFAULT_GROUP', '',
        'spring:\n  cloud:\n    sentinel:\n      enabled: true\n      eager: true\n      transport:\n        port: 8719\n        dashboard: localhost:8088\n  main:\n    allow-bean-definition-overriding: true\n  servlet:\n    multipart:\n      max-request-size: 5MB\n      max-file-size: 100MB\n  jackson:\n    time-zone: GMT+8\n    date-format: yyyy-MM-dd HH:mm:ss\n    serialization:\n      # 格式化输出\n      INDENT_OUTPUT: false\n      # 忽略无法转换的对象\n      fail_on_empty_beans: false\n    deserialization:\n      # 允许对象忽略json中不存在的属性\n      fail_on_unknown_properties: false\n  messages:\n    basename: i18n/messages\n    encoding: utf-8\n    fallback-to-system-locale: true\n  # redis配置\n  redis:\n    host: ${REDIS_HOST:127.0.0.1}\n    port: ${REDIS_PORT:6379}\n    database: ${REDIS_DATABASE:0}\n    timeout: 10s\n    lettuce:\n      pool:\n        # 连接池最大连接数\n        max-active: 200\n        # 连接池最大阻塞等待时间（使用负值表示没有限制）\n        max-wait: -1ms\n        # 连接池中的最大空闲连接\n        max-idle: 10\n        # 连接池中的最小空闲连接\n        min-idle: 0\n  cache:\n    type: redis\n  datasource:\n    druid:\n      initial-size: 5 # 初始连接数\n      min-idle: 10 # 最小连接池数量\n      max-active: 20 # 最大连接池数量\n      max-wait: 600000 # 配置获取连接等待超时的时间，单位：毫秒\n      time-between-eviction-runs-millis: 60000 # 配置间隔多久才进行一次检测，检测需要关闭的空闲连接，单位：毫秒\n      min-evictable-idle-time-millis: 300000 # 配置一个连接在池中最小生存的时间，单位：毫秒\n      max-evictable-idle-time-millis: 900000 # 配置一个连接在池中最大生存的时间，单位：毫秒\n      validation-query: SELECT 1 FROM DUAL # 配置检测连接是否有效\n      test-while-idle: true\n      test-on-borrow: false\n      test-on-return: false\n      web-stat-filter:\n        enabled: true\n      stat-view-servlet:\n        enabled: true\n        allow: # 设置白名单，不填则允许所有访问\n        url-pattern: /druid/*\n        login-username: druid\n        login-password: druid\n      filter:\n        stat:\n          enabled: true\n          log-slow-sql: true # 慢 SQL 记录\n          slow-sql-millis: 100\n          merge-sql: true\n        wall:\n          config:\n            multi-statement-allow: true\n# 端点配置\nmanagement:\n  endpoints:\n    web:\n      exposure:\n        include: \"*\"\n    jmx:\n      exposure:\n        include: \"*\"\n  endpoint:\n    health:\n      show-details: always\n\nfeign:\n  ## 启用对 feign 的 Sentinel 支持\n  sentinel:\n    enabled: true\n  client:\n    config:\n      default:\n        connect-timeout: 5000\n        read-timeout: 5000\n        logger-level: full\n  autoconfiguration:\n    jackson:\n      enabled: true\n  compression:\n    request:\n      enabled: true\n    response:\n      enabled: true\n  metrics:\n    enabled: true\n\n\n# 文档配置\nknife4j:\n  enable: true\n  production: false\n  cors: true\n\n# mybatis plus配置\nmybatis-plus:\n  global-config:\n    db-config:\n      id-type: auto\n  configuration:\n    map-underscore-to-camel-case: true\n    call-setters-on-nulls: true\n  check-config-location: false\n\n\nsa-token:\n  # jwt秘钥\n  jwt-secret-key: ${SA_TOKEN_JWT_SECRET_KEY:abcdefghijklmnopqrstuvwxyz}\n  # token名称 (同时也是cookie名称)\n  token-name: Authorization\n  # token有效期，单位s 默认30天, -1代表永不过期\n  timeout: 2592000\n  # token临时有效期 (指定时间内无操作就视为token过期) 单位: 秒\n  activity-timeout: 1800\n  # 是否允许同一账号并发登录 (为true时允许一起登录, 为false时新登录挤掉旧登录)\n  is-concurrent: true\n  # 在多人登录同一账号时，是否共用一个token (为true时所有登录共用一个token, 为false时每次登录新建一个token)\n  is-share: true\n  # token风格\n  token-style: uuid\n  # 是否输出操作日志\n  is-log: true',
        'bbbe0b568c067dd79240e2a7e44474eb', '2022-10-09 09:47:44', '2022-10-09 01:47:44', 'nacos', '0:0:0:0:0:0:0:1',
        'U', '', '');
/*!40000 ALTER TABLE `his_config_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `permissions`
--

DROP TABLE IF EXISTS `permissions`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `permissions`
(
    `role`     varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `resource` varchar(255) COLLATE utf8mb4_general_ci NOT NULL,
    `action`   varchar(8) COLLATE utf8mb4_general_ci   NOT NULL,
    UNIQUE KEY `uk_role_permission` (`role`, `resource`, `action`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `permissions`
--

LOCK TABLES `permissions` WRITE;
/*!40000 ALTER TABLE `permissions`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `permissions`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `roles`
--

DROP TABLE IF EXISTS `roles`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `roles`
(
    `username` varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    `role`     varchar(50) COLLATE utf8mb4_general_ci NOT NULL,
    UNIQUE KEY `idx_user_role` (`username`, `role`) USING BTREE
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `roles`
--

LOCK TABLES `roles` WRITE;
/*!40000 ALTER TABLE `roles`
    DISABLE KEYS */;
INSERT INTO `roles`
VALUES ('nacos', 'ROLE_ADMIN');
/*!40000 ALTER TABLE `roles`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_capacity`
--

DROP TABLE IF EXISTS `tenant_capacity`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_capacity`
(
    `id`                bigint unsigned                  NOT NULL AUTO_INCREMENT COMMENT '主键ID',
    `tenant_id`         varchar(128) COLLATE utf8mb3_bin NOT NULL DEFAULT '' COMMENT 'Tenant ID',
    `quota`             int unsigned                     NOT NULL DEFAULT '0' COMMENT '配额，0表示使用默认值',
    `usage`             int unsigned                     NOT NULL DEFAULT '0' COMMENT '使用量',
    `max_size`          int unsigned                     NOT NULL DEFAULT '0' COMMENT '单个配置大小上限，单位为字节，0表示使用默认值',
    `max_aggr_count`    int unsigned                     NOT NULL DEFAULT '0' COMMENT '聚合子配置最大个数',
    `max_aggr_size`     int unsigned                     NOT NULL DEFAULT '0' COMMENT '单个聚合数据的子配置大小上限，单位为字节，0表示使用默认值',
    `max_history_count` int unsigned                     NOT NULL DEFAULT '0' COMMENT '最大变更历史数量',
    `gmt_create`        datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `gmt_modified`      datetime                         NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='租户容量信息表';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_capacity`
--

LOCK TABLES `tenant_capacity` WRITE;
/*!40000 ALTER TABLE `tenant_capacity`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `tenant_capacity`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `tenant_info`
--

DROP TABLE IF EXISTS `tenant_info`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `tenant_info`
(
    `id`            bigint                           NOT NULL AUTO_INCREMENT COMMENT 'id',
    `kp`            varchar(128) COLLATE utf8mb3_bin NOT NULL COMMENT 'kp',
    `tenant_id`     varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_id',
    `tenant_name`   varchar(128) COLLATE utf8mb3_bin DEFAULT '' COMMENT 'tenant_name',
    `tenant_desc`   varchar(256) COLLATE utf8mb3_bin DEFAULT NULL COMMENT 'tenant_desc',
    `create_source` varchar(32) COLLATE utf8mb3_bin  DEFAULT NULL COMMENT 'create_source',
    `gmt_create`    bigint                           NOT NULL COMMENT '创建时间',
    `gmt_modified`  bigint                           NOT NULL COMMENT '修改时间',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_tenant_info_kptenantid` (`kp`, `tenant_id`),
    KEY `idx_tenant_id` (`tenant_id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb3
  COLLATE = utf8mb3_bin COMMENT ='tenant_info';
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `tenant_info`
--

LOCK TABLES `tenant_info` WRITE;
/*!40000 ALTER TABLE `tenant_info`
    DISABLE KEYS */;
INSERT INTO `tenant_info`
VALUES (3, '1', '9f444a31-6446-4b83-8e85-ce5c2db019c7', 'dev', 'dev', 'nacos', 1656913932699, 1656913932699),
       (4, '1', '85fb8788-47e7-44e9-8d68-42b1bc752ad7', 'prod', 'prod', 'nacos', 1656913940692, 1656913940692);
/*!40000 ALTER TABLE `tenant_info`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `users`
--

DROP TABLE IF EXISTS `users`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `users`
(
    `username` varchar(50) COLLATE utf8mb4_general_ci  NOT NULL,
    `password` varchar(500) COLLATE utf8mb4_general_ci NOT NULL,
    `enabled`  tinyint(1)                              NOT NULL,
    PRIMARY KEY (`username`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_general_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `users`
--

LOCK TABLES `users` WRITE;
/*!40000 ALTER TABLE `users`
    DISABLE KEYS */;
INSERT INTO `users`
VALUES ('nacos', '$2a$10$EuWPZHzz32dJN7jexM34MOeYirDdFAZm2kuWj7VEOJhhZkDrxfvUu', 1);
/*!40000 ALTER TABLE `users`
    ENABLE KEYS */;
UNLOCK TABLES;
/*!40103 SET TIME_ZONE = @OLD_TIME_ZONE */;

/*!40101 SET SQL_MODE = @OLD_SQL_MODE */;
/*!40014 SET FOREIGN_KEY_CHECKS = @OLD_FOREIGN_KEY_CHECKS */;
/*!40014 SET UNIQUE_CHECKS = @OLD_UNIQUE_CHECKS */;
/*!40101 SET CHARACTER_SET_CLIENT = @OLD_CHARACTER_SET_CLIENT */;
/*!40101 SET CHARACTER_SET_RESULTS = @OLD_CHARACTER_SET_RESULTS */;
/*!40101 SET COLLATION_CONNECTION = @OLD_COLLATION_CONNECTION */;
/*!40111 SET SQL_NOTES = @OLD_SQL_NOTES */;

-- Dump completed on 2022-10-10 14:08:27
