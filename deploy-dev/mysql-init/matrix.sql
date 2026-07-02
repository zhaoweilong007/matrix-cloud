-- MySQL dump 10.13  Distrib 8.0.30, for Win64 (x86_64)
--
-- Host: 127.0.0.1    Database: matrix
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

create database if not exists `matrix` default character set utf8mb4 collate utf8mb4_unicode_ci;

use `matrix`;

--
-- Table structure for table `sys_admin`
--

DROP TABLE IF EXISTS `sys_admin`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_admin`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `username`    varchar(64)  DEFAULT NULL,
    `password`    varchar(64)  DEFAULT NULL,
    `icon`        varchar(500) DEFAULT NULL,
    `email`       varchar(100) DEFAULT NULL,
    `nick_name`   varchar(200) DEFAULT NULL,
    `note`        varchar(500) DEFAULT NULL,
    `login_time`  datetime     DEFAULT NULL,
    `status`      int          DEFAULT NULL,
    `user_type`   varchar(10)  DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_admin`
--

LOCK TABLES `sys_admin` WRITE;
/*!40000 ALTER TABLE `sys_admin`
    DISABLE KEYS */;
INSERT INTO `sys_admin`
VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 'admin@qq.com', NULL, NULL, '2022-08-25 15:35:34', NULL,
        'pc', '2022-07-14 17:58:09', '2022-08-25 15:35:34', '15', 'admin', 0, NULL);
/*!40000 ALTER TABLE `sys_admin`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_admin_role_relation`
--

DROP TABLE IF EXISTS `sys_admin_role_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_admin_role_relation`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `admin_id`  bigint DEFAULT NULL,
    `role_id`   bigint DEFAULT NULL,
    `tenant_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_admin_role_relation`
--

LOCK TABLES `sys_admin_role_relation` WRITE;
/*!40000 ALTER TABLE `sys_admin_role_relation`
    DISABLE KEYS */;
INSERT INTO `sys_admin_role_relation`
VALUES (1, 1, 1, NULL),
       (2, 1, 2, NULL);
/*!40000 ALTER TABLE `sys_admin_role_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_menu`
--

DROP TABLE IF EXISTS `sys_menu`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_menu`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint       DEFAULT NULL,
    `title`       varchar(100) DEFAULT NULL,
    `level`       int          DEFAULT NULL,
    `sort`        int          DEFAULT NULL,
    `name`        varchar(100) DEFAULT NULL,
    `icon`        varchar(200) DEFAULT NULL,
    `hidden`      int          DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_menu`
--

LOCK TABLES `sys_menu` WRITE;
/*!40000 ALTER TABLE `sys_menu`
    DISABLE KEYS */;
INSERT INTO `sys_menu`
VALUES (1, 0, '菜单一', 0, 0, '菜单一', 'qwer', 0, '2022-07-15 09:20:37', '2022-07-15 09:20:37', '1', '1', 0, NULL),
       (2, 0, '菜单二', 0, 0, '菜单二', 'ewq', 0, '2022-07-15 09:20:55', '2022-07-15 09:20:55', '1', '1', 0, NULL),
       (3, 1, '菜单1-1', 1, 0, '菜单1-1', 'ewqeqweqw', 0, '2022-07-15 09:21:32', '2022-07-15 09:21:32', '1', '1', 0,
        NULL),
       (4, 2, '菜单2-1', 1, 0, '菜单2-1', 'ewqeqweqw', 0, '2022-07-15 09:21:41', '2022-07-15 09:21:41', '1', '1', 0,
        NULL);
/*!40000 ALTER TABLE `sys_menu`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_resource`
--

DROP TABLE IF EXISTS `sys_resource`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_resource`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(200) DEFAULT NULL,
    `url`         varchar(200) DEFAULT NULL,
    `description` varchar(500) DEFAULT NULL,
    `category_id` bigint       DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_resource`
--

LOCK TABLES `sys_resource` WRITE;
/*!40000 ALTER TABLE `sys_resource`
    DISABLE KEYS */;
INSERT INTO `sys_resource`
VALUES (1, '用户管理', '/system-server/admin/**', '用户管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (2, '菜单管理', '/system-server/menu/**', '菜单管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (3, '角色管理', '/system-server/role/**', '角色管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (4, '资源管理', '/system-server/resource/**', '资源管理', 1, NULL, NULL, NULL, NULL, 0, NULL);
/*!40000 ALTER TABLE `sys_resource`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_resource_category`
--

DROP TABLE IF EXISTS `sys_resource_category`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_resource_category`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(200) DEFAULT NULL,
    `sort`        int          DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 2
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_resource_category`
--

LOCK TABLES `sys_resource_category` WRITE;
/*!40000 ALTER TABLE `sys_resource_category`
    DISABLE KEYS */;
INSERT INTO `sys_resource_category`
VALUES (1, '权限管理', 1, NULL, NULL, NULL, NULL, 0, NULL);
/*!40000 ALTER TABLE `sys_resource_category`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role`
--

DROP TABLE IF EXISTS `sys_role`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL,
    `description` varchar(500) DEFAULT NULL,
    `admin_count` int          DEFAULT NULL,
    `create_time` datetime     DEFAULT NULL,
    `status`      int          DEFAULT NULL,
    `sort`        int          DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 3
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role`
--

LOCK TABLES `sys_role` WRITE;
/*!40000 ALTER TABLE `sys_role`
    DISABLE KEYS */;
INSERT INTO `sys_role`
VALUES (1, '系统管理员', '系统管理员', 1, NULL, 0, 1, NULL, NULL, NULL, 0, NULL),
       (2, '开发', '开发角色', NULL, '2022-07-15 09:30:40', 0, NULL, '2022-07-15 09:30:40', '1', '1', 0, NULL);
/*!40000 ALTER TABLE `sys_role`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_menu_relation`
--

DROP TABLE IF EXISTS `sys_role_menu_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_menu_relation`
(
    `id`        bigint NOT NULL AUTO_INCREMENT,
    `role_id`   bigint DEFAULT NULL,
    `menu_id`   bigint DEFAULT NULL,
    `tenant_id` int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 43
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_menu_relation`
--

LOCK TABLES `sys_role_menu_relation` WRITE;
/*!40000 ALTER TABLE `sys_role_menu_relation`
    DISABLE KEYS */;
INSERT INTO `sys_role_menu_relation`
VALUES (41, 2, 2, NULL),
       (42, 2, 3, NULL);
/*!40000 ALTER TABLE `sys_role_menu_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_role_resource_relation`
--

DROP TABLE IF EXISTS `sys_role_resource_relation`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_role_resource_relation`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `role_id`     bigint DEFAULT NULL,
    `resource_id` bigint DEFAULT NULL,
    `tenant_id`   int    DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 5
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_role_resource_relation`
--

LOCK TABLES `sys_role_resource_relation` WRITE;
/*!40000 ALTER TABLE `sys_role_resource_relation`
    DISABLE KEYS */;
INSERT INTO `sys_role_resource_relation`
VALUES (1, 1, 1, NULL),
       (2, 1, 2, NULL),
       (3, 1, 3, NULL),
       (4, 1, 4, NULL);
/*!40000 ALTER TABLE `sys_role_resource_relation`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_tenant`
--

DROP TABLE IF EXISTS `sys_tenant`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `status`      int          DEFAULT '0' COMMENT '租户状态：启用、禁用',
    `tenant_name` varchar(200) DEFAULT NULL COMMENT '租户名称',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(20)  DEFAULT NULL,
    `update_by`   varchar(20)  DEFAULT NULL,
    `deleted`     int          DEFAULT NULL,
    `package_id`  bigint       DEFAULT NULL COMMENT '关联套餐ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `sys_tenant`
--

LOCK TABLES `sys_tenant` WRITE;
/*!40000 ALTER TABLE `sys_tenant`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `sys_tenant`
    ENABLE KEYS */;
UNLOCK TABLES;

--
-- Table structure for table `sys_dept`
--

DROP TABLE IF EXISTS `sys_dept`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dept`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `parent_id`   bigint       DEFAULT '0' COMMENT '父部门ID，0表示根部门',
    `name`        varchar(100) DEFAULT NULL COMMENT '部门名称',
    `leader`      varchar(50)  DEFAULT NULL COMMENT '负责人',
    `phone`       varchar(20)  DEFAULT NULL COMMENT '联系电话',
    `email`       varchar(100) DEFAULT NULL COMMENT '邮箱',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `status`      int          DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_post`
--

DROP TABLE IF EXISTS `sys_post`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_post`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `code`        varchar(50)  DEFAULT NULL COMMENT '岗位编码（唯一）',
    `name`        varchar(100) DEFAULT NULL COMMENT '岗位名称',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `status`      int          DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_dict_type`
--

DROP TABLE IF EXISTS `sys_dict_type`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_type`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL COMMENT '字典名称',
    `type`        varchar(100) DEFAULT NULL COMMENT '字典类型编码（唯一）',
    `status`      int          DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type` (`type`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_dict_data`
--

DROP TABLE IF EXISTS `sys_dict_data`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_dict_data`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `dict_type`   varchar(100) DEFAULT NULL COMMENT '字典类型编码',
    `label`       varchar(100) DEFAULT NULL COMMENT '字典标签',
    `value`       varchar(100) DEFAULT NULL COMMENT '字典值',
    `sort`        int          DEFAULT '0' COMMENT '排序',
    `status`      int          DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `color_type`  varchar(20)  DEFAULT NULL COMMENT '颜色类型',
    `css_class`   varchar(50)  DEFAULT NULL COMMENT 'CSS类名',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_config`
--

DROP TABLE IF EXISTS `sys_config`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_config`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL COMMENT '参数名称',
    `config_key`  varchar(100) DEFAULT NULL COMMENT '参数键名（唯一）',
    `value`       varchar(500) DEFAULT NULL COMMENT '参数值',
    `type`        int          DEFAULT '0' COMMENT '是否系统内置：1-是 0-否',
    `visible`     int          DEFAULT '1' COMMENT '是否可见：1-是 0-否',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_notice`
--

DROP TABLE IF EXISTS `sys_notice`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_notice`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `title`       varchar(200) DEFAULT NULL COMMENT '通知标题',
    `content`     text         DEFAULT NULL COMMENT '通知内容',
    `type`        int          DEFAULT '1' COMMENT '通知类型：1-系统通知 2-业务通知',
    `status`      int          DEFAULT '0' COMMENT '状态：0-草稿 1-已发布',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_login_log`
--

DROP TABLE IF EXISTS `sys_login_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_login_log`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `username`    varchar(50)  DEFAULT NULL COMMENT '用户名',
    `ip`          varchar(50)  DEFAULT NULL COMMENT '登录IP',
    `user_agent`  varchar(500) DEFAULT NULL COMMENT '浏览器UserAgent',
    `status`      int          DEFAULT '1' COMMENT '登录状态：0-失败 1-成功',
    `result`      varchar(200) DEFAULT NULL COMMENT '登录结果描述',
    `login_time`  datetime     DEFAULT NULL COMMENT '登录时间',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_operate_log`
--

DROP TABLE IF EXISTS `sys_operate_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_operate_log`
(
    `id`             bigint NOT NULL AUTO_INCREMENT,
    `username`       varchar(50)  DEFAULT NULL COMMENT '操作人用户名',
    `module`         varchar(50)  DEFAULT NULL COMMENT '模块名',
    `name`           varchar(100) DEFAULT NULL COMMENT '操作名称',
    `type`           int          DEFAULT NULL COMMENT '操作类型',
    `request_method` varchar(20)  DEFAULT NULL COMMENT '请求方法',
    `request_url`    varchar(500) DEFAULT NULL COMMENT '请求URL',
    `request_params` text         DEFAULT NULL COMMENT '请求参数',
    `request_result` text         DEFAULT NULL COMMENT '请求结果',
    `cost_time`      bigint       DEFAULT NULL COMMENT '耗时（毫秒）',
    `ip`             varchar(50)  DEFAULT NULL COMMENT '操作IP',
    `user_agent`     varchar(500) DEFAULT NULL COMMENT '浏览器UserAgent',
    `status`         int          DEFAULT '1' COMMENT '状态：0-失败 1-成功',
    `error_msg`      text         DEFAULT NULL COMMENT '错误信息',
    `operate_time`   datetime     DEFAULT NULL COMMENT '操作时间',
    `create_time`    datetime     DEFAULT NULL,
    `update_time`    datetime     DEFAULT NULL,
    `create_by`      varchar(50)  DEFAULT NULL,
    `update_by`      varchar(50)  DEFAULT NULL,
    `deleted`        int          DEFAULT '0',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `sys_tenant_package`
--

DROP TABLE IF EXISTS `sys_tenant_package`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
CREATE TABLE `sys_tenant_package`
(
    `id`          bigint NOT NULL AUTO_INCREMENT,
    `name`        varchar(100) DEFAULT NULL COMMENT '套餐名称',
    `status`      int          DEFAULT '1' COMMENT '状态：0-禁用 1-启用',
    `menu_ids`    text         DEFAULT NULL COMMENT '关联菜单ID集合（JSON数组）',
    `remark`      varchar(500) DEFAULT NULL COMMENT '备注',
    `create_time` datetime     DEFAULT NULL,
    `update_time` datetime     DEFAULT NULL,
    `create_by`   varchar(50)  DEFAULT NULL,
    `update_by`   varchar(50)  DEFAULT NULL,
    `deleted`     int          DEFAULT '0',
    `tenant_id`   int          DEFAULT NULL,
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  AUTO_INCREMENT = 1
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci;
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Table structure for table `undo_log`
--

DROP TABLE IF EXISTS `undo_log`;
/*!40101 SET @saved_cs_client = @@character_set_client */;
/*!50503 SET character_set_client = utf8mb4 */;
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
) ENGINE = InnoDB AUTO_INCREMENT = 1 DEFAULT CHARSET = utf8mb4 COMMENT ='AT transaction mode undo table';
ALTER TABLE `undo_log` ADD INDEX `ix_log_created` (`log_created`);
/*!40101 SET character_set_client = @saved_cs_client */;

--
-- Dumping data for table `undo_log`
--

LOCK TABLES `undo_log` WRITE;
/*!40000 ALTER TABLE `undo_log`
    DISABLE KEYS */;
/*!40000 ALTER TABLE `undo_log`
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

-- Dump completed on 2022-10-10 14:09:55
