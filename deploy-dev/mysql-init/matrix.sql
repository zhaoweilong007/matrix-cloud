-- ============================================================
-- Matrix-Cloud 数据库初始化脚本
-- Database: matrix
-- Engine:   InnoDB
-- Charset:  utf8mb4 / utf8mb4_0900_ai_ci
-- ============================================================

CREATE DATABASE IF NOT EXISTS `matrix`
    DEFAULT CHARACTER SET utf8mb4
    COLLATE utf8mb4_unicode_ci;

USE `matrix`;

-- ============================================================
-- 1. 系统核心表
-- ============================================================

-- ----------------------------
-- 系统用户
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin`;
CREATE TABLE `sys_admin`
(
    `id`          BIGINT       NOT NULL COMMENT '用户ID',
    `username`    VARCHAR(64)  DEFAULT NULL COMMENT '用户名',
    `password`    VARCHAR(64)  DEFAULT NULL COMMENT '密码',
    `icon`        VARCHAR(500) DEFAULT NULL COMMENT '头像',
    `email`       VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `nick_name`   VARCHAR(200) DEFAULT NULL COMMENT '昵称',
    `note`        VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `dept_id`     BIGINT       DEFAULT NULL COMMENT '关联部门ID',
    `mobile`      VARCHAR(20)  DEFAULT NULL COMMENT '手机号',
    `sex`         INT          DEFAULT 0 COMMENT '性别: 0-未知 1-男 2-女',
    `avatar`      VARCHAR(500) DEFAULT NULL COMMENT '头像URL',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `login_ip`    VARCHAR(50)  DEFAULT NULL COMMENT '最后登录IP',
    `login_time`  DATETIME     DEFAULT NULL COMMENT '最后登录时间',
    `status`      INT          DEFAULT NULL COMMENT '状态',
    `user_type`   VARCHAR(10)  DEFAULT NULL COMMENT '用户类型',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '系统用户';

INSERT INTO `sys_admin`
VALUES (1, 'admin', '21232f297a57a5a743894a0e4a801fc3', NULL, 'admin@qq.com', NULL, NULL, NULL, NULL, 0, NULL, NULL,
        NULL, '2022-08-25 15:35:34', NULL, 'pc', '2022-07-14 17:58:09', '2022-08-25 15:35:34', '15', 'admin', 0, NULL);

-- ----------------------------
-- 角色
-- ----------------------------
DROP TABLE IF EXISTS `sys_role`;
CREATE TABLE `sys_role`
(
    `id`          BIGINT       NOT NULL COMMENT '角色ID',
    `name`        VARCHAR(100) DEFAULT NULL COMMENT '角色名称',
    `code`        VARCHAR(100) DEFAULT NULL COMMENT '角色编码（唯一）',
    `type`        INT          DEFAULT 2 COMMENT '角色类型: 1-内置 2-自定义',
    `data_scope`  INT          DEFAULT 1 COMMENT '数据范围: 1-全部 2-自定义 3-本部门 4-本部门及以下 5-仅本人',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `admin_count` INT          DEFAULT NULL COMMENT '关联用户数',
    `status`      INT          DEFAULT NULL COMMENT '状态',
    `sort`        INT          DEFAULT NULL COMMENT '排序',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '角色';

INSERT INTO `sys_role`
VALUES (1, '系统管理员', 'super_admin', 1, 1, '系统管理员', 1, 0, 1, NULL, NULL, NULL, NULL, NULL, 0, NULL),
       (2, '开发', 'dev', 2, 1, '开发角色', NULL, 0, NULL, NULL, '2022-07-15 09:30:40', '2022-07-15 09:30:40', '1', '1', 0, NULL);

-- ----------------------------
-- 用户-角色关联
-- ----------------------------
DROP TABLE IF EXISTS `sys_admin_role_relation`;
CREATE TABLE `sys_admin_role_relation`
(
    `id`        BIGINT NOT NULL COMMENT '主键',
    `admin_id`  BIGINT DEFAULT NULL COMMENT '用户ID',
    `role_id`   BIGINT DEFAULT NULL COMMENT '角色ID',
    `tenant_id` INT    DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '用户-角色关联';

INSERT INTO `sys_admin_role_relation`
VALUES (1, 1, 1, NULL),
       (2, 1, 2, NULL);

-- ----------------------------
-- 菜单（支持目录/菜单/按钮三级）
-- ----------------------------
DROP TABLE IF EXISTS `sys_menu`;
CREATE TABLE `sys_menu`
(
    `id`             BIGINT       NOT NULL COMMENT '菜单ID',
    `parent_id`      BIGINT       DEFAULT NULL COMMENT '父菜单ID，0=根',
    `title`          VARCHAR(100) DEFAULT NULL COMMENT '菜单标题',
    `level`          INT          DEFAULT NULL COMMENT '菜单层级',
    `sort`           INT          DEFAULT NULL COMMENT '排序',
    `name`           VARCHAR(100) DEFAULT NULL COMMENT '前端路由名称',
    `icon`           VARCHAR(200) DEFAULT NULL COMMENT '图标',
    `hidden`         INT          DEFAULT NULL COMMENT '是否隐藏: 1-是 0-否',
    `permission`     VARCHAR(100) DEFAULT NULL COMMENT '权限标识（如 system:user:list）',
    `type`           INT          DEFAULT 2 COMMENT '菜单类型: 1-目录 2-菜单 3-按钮',
    `path`           VARCHAR(200) DEFAULT NULL COMMENT '前端路由路径',
    `component`      VARCHAR(200) DEFAULT NULL COMMENT '前端组件路径',
    `component_name` VARCHAR(100) DEFAULT NULL COMMENT 'keep-alive 组件名',
    `visible`        INT          DEFAULT 1 COMMENT '是否可见: 1-显示 0-隐藏',
    `keep_alive`     INT          DEFAULT 0 COMMENT '是否缓存: 1-是 0-否',
    `always_show`    INT          DEFAULT 0 COMMENT '是否始终显示: 1-是 0-否',
    `status`         INT          DEFAULT 1 COMMENT '状态: 1-启用 0-禁用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`        INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`      INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '菜单';

INSERT INTO `sys_menu`
VALUES (1, 0, '菜单一', 0, 0, '菜单一', 'qwer', 0, NULL, 1, '/menu1', 'menu1/index', NULL, 1, 0, 0, 1,
        '2022-07-15 09:20:37', '2022-07-15 09:20:37', '1', '1', 0, NULL),
       (2, 0, '菜单二', 0, 0, '菜单二', 'ewq', 0, NULL, 1, '/menu2', 'menu2/index', NULL, 1, 0, 0, 1,
        '2022-07-15 09:20:55', '2022-07-15 09:20:55', '1', '1', 0, NULL),
       (3, 1, '菜单1-1', 1, 0, '菜单1-1', 'ewqeqweqw', 0, NULL, 2, '/menu1/sub1', 'menu1/sub1', NULL, 1, 0, 0, 1,
        '2022-07-15 09:21:32', '2022-07-15 09:21:32', '1', '1', 0, NULL),
       (4, 2, '菜单2-1', 1, 0, '菜单2-1', 'ewqeqweqw', 0, NULL, 2, '/menu2/sub1', 'menu2/sub1', NULL, 1, 0, 0, 1,
        '2022-07-15 09:21:41', '2022-07-15 09:21:41', '1', '1', 0, NULL);

-- ----------------------------
-- 角色-菜单关联
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_menu_relation`;
CREATE TABLE `sys_role_menu_relation`
(
    `id`        BIGINT NOT NULL COMMENT '主键',
    `role_id`   BIGINT DEFAULT NULL COMMENT '角色ID',
    `menu_id`   BIGINT DEFAULT NULL COMMENT '菜单ID',
    `tenant_id` INT    DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '角色-菜单关联';

INSERT INTO `sys_role_menu_relation`
VALUES (41, 2, 2, NULL),
       (42, 2, 3, NULL);

-- ----------------------------
-- 资源（API 权限）
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource`;
CREATE TABLE `sys_resource`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `name`        VARCHAR(200) DEFAULT NULL COMMENT '资源名称',
    `url`         VARCHAR(200) DEFAULT NULL COMMENT 'URL 匹配模式',
    `description` VARCHAR(500) DEFAULT NULL COMMENT '描述',
    `category_id` BIGINT       DEFAULT NULL COMMENT '分类ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = 'API 资源';

INSERT INTO `sys_resource`
VALUES (1, '用户管理', '/system-server/admin/**', '用户管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (2, '菜单管理', '/system-server/menu/**', '菜单管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (3, '角色管理', '/system-server/role/**', '角色管理', 1, NULL, NULL, NULL, NULL, 0, NULL),
       (4, '资源管理', '/system-server/resource/**', '资源管理', 1, NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- 资源分类
-- ----------------------------
DROP TABLE IF EXISTS `sys_resource_category`;
CREATE TABLE `sys_resource_category`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `name`        VARCHAR(200) DEFAULT NULL COMMENT '分类名称',
    `sort`        INT          DEFAULT NULL COMMENT '排序',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '资源分类';

INSERT INTO `sys_resource_category`
VALUES (1, '权限管理', 1, NULL, NULL, NULL, NULL, 0, NULL);

-- ----------------------------
-- 角色-资源关联
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_resource_relation`;
CREATE TABLE `sys_role_resource_relation`
(
    `id`          BIGINT NOT NULL COMMENT '主键',
    `role_id`     BIGINT DEFAULT NULL COMMENT '角色ID',
    `resource_id` BIGINT DEFAULT NULL COMMENT '资源ID',
    `tenant_id`   INT    DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '角色-资源关联';

INSERT INTO `sys_role_resource_relation`
VALUES (1, 1, 1, NULL),
       (2, 1, 2, NULL),
       (3, 1, 3, NULL),
       (4, 1, 4, NULL);

-- ============================================================
-- 2. 多租户
-- ============================================================

-- ----------------------------
-- 租户套餐
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant_package`;
CREATE TABLE `sys_tenant_package`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `name`        VARCHAR(100) DEFAULT NULL COMMENT '套餐名称',
    `status`      INT          DEFAULT 1 COMMENT '状态: 1-启用 0-禁用',
    `menu_ids`    TEXT         DEFAULT NULL COMMENT '关联菜单ID集合（JSON数组）',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '租户套餐';

-- ----------------------------
-- 租户
-- ----------------------------
DROP TABLE IF EXISTS `sys_tenant`;
CREATE TABLE `sys_tenant`
(
    `id`             BIGINT       NOT NULL COMMENT '租户ID',
    `status`         INT          DEFAULT 0 COMMENT '状态: 0-禁用 1-启用',
    `tenant_name`    VARCHAR(200) DEFAULT NULL COMMENT '租户名称',
    `package_id`     BIGINT       DEFAULT NULL COMMENT '关联套餐ID',
    `contact_name`   VARCHAR(50)  DEFAULT NULL COMMENT '联系人姓名',
    `contact_mobile` VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `expire_time`    DATETIME     DEFAULT NULL COMMENT '到期时间',
    `account_count`  INT          DEFAULT NULL COMMENT '最大账号数',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`        INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '租户';

-- ============================================================
-- 3. 组织架构
-- ============================================================

-- ----------------------------
-- 部门（树形结构）
-- ----------------------------
DROP TABLE IF EXISTS `sys_dept`;
CREATE TABLE `sys_dept`
(
    `id`             BIGINT       NOT NULL COMMENT '部门ID',
    `parent_id`      BIGINT       DEFAULT 0 COMMENT '父部门ID，0=根',
    `name`           VARCHAR(100) DEFAULT NULL COMMENT '部门名称',
    `leader`         VARCHAR(50)  DEFAULT NULL COMMENT '负责人',
    `leader_user_id` BIGINT       DEFAULT NULL COMMENT '负责人用户ID',
    `ancestors`      VARCHAR(500) DEFAULT '' COMMENT '祖级列表（如 0,100,101）',
    `dept_category`  VARCHAR(50)  DEFAULT NULL COMMENT '部门类别编码',
    `phone`          VARCHAR(20)  DEFAULT NULL COMMENT '联系电话',
    `email`          VARCHAR(100) DEFAULT NULL COMMENT '邮箱',
    `sort`           INT          DEFAULT 0 COMMENT '排序',
    `status`         INT          DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`        INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`      INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '部门';

-- ----------------------------
-- 岗位
-- ----------------------------
DROP TABLE IF EXISTS `sys_post`;
CREATE TABLE `sys_post`
(
    `id`            BIGINT       NOT NULL COMMENT '岗位ID',
    `code`          VARCHAR(50)  DEFAULT NULL COMMENT '岗位编码',
    `name`          VARCHAR(100) DEFAULT NULL COMMENT '岗位名称',
    `dept_id`       BIGINT       DEFAULT NULL COMMENT '归属部门ID',
    `post_category` VARCHAR(50)  DEFAULT NULL COMMENT '岗位类别编码',
    `sort`          INT          DEFAULT 0 COMMENT '排序',
    `status`        INT          DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `remark`        VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`     VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`     VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`       INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`     INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_code` (`code`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '岗位';

-- ----------------------------
-- 角色-部门关联（数据权限）
-- ----------------------------
DROP TABLE IF EXISTS `sys_role_dept`;
CREATE TABLE `sys_role_dept`
(
    `id`          BIGINT      NOT NULL COMMENT '主键',
    `role_id`     BIGINT      DEFAULT NULL COMMENT '角色ID',
    `dept_id`     BIGINT      DEFAULT NULL COMMENT '部门ID',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50) DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50) DEFAULT NULL COMMENT '更新人',
    `deleted`     INT         DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '角色-部门关联（数据权限）';

-- ============================================================
-- 4. 字典与配置
-- ============================================================

-- ----------------------------
-- 字典类型
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_type`;
CREATE TABLE `sys_dict_type`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `name`        VARCHAR(100) DEFAULT NULL COMMENT '字典名称',
    `type`        VARCHAR(100) DEFAULT NULL COMMENT '字典类型编码',
    `status`      INT          DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_type` (`type`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '字典类型';

-- ----------------------------
-- 字典数据
-- ----------------------------
DROP TABLE IF EXISTS `sys_dict_data`;
CREATE TABLE `sys_dict_data`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `dict_type`   VARCHAR(100) DEFAULT NULL COMMENT '字典类型编码',
    `label`       VARCHAR(100) DEFAULT NULL COMMENT '字典标签',
    `value`       VARCHAR(100) DEFAULT NULL COMMENT '字典值',
    `sort`        INT          DEFAULT 0 COMMENT '排序',
    `status`      INT          DEFAULT 1 COMMENT '状态: 0-禁用 1-启用',
    `color_type`  VARCHAR(20)  DEFAULT NULL COMMENT '颜色类型（primary/success/danger）',
    `css_class`   VARCHAR(50)  DEFAULT NULL COMMENT 'CSS 类名',
    `is_default`  INT          DEFAULT 0 COMMENT '是否默认: 1-是 0-否',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '字典数据';

-- ----------------------------
-- 系统配置
-- ----------------------------
DROP TABLE IF EXISTS `sys_config`;
CREATE TABLE `sys_config`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `name`        VARCHAR(100) DEFAULT NULL COMMENT '参数名称',
    `config_key`  VARCHAR(100) DEFAULT NULL COMMENT '参数键名',
    `value`       VARCHAR(500) DEFAULT NULL COMMENT '参数值',
    `type`        INT          DEFAULT 0 COMMENT '是否系统内置: 1-是 0-否',
    `visible`     INT          DEFAULT 1 COMMENT '是否可见: 1-是 0-否',
    `category`    VARCHAR(50)  DEFAULT NULL COMMENT '配置分类',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`),
    UNIQUE KEY `uk_config_key` (`config_key`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '系统配置';

-- ============================================================
-- 5. 通知与消息
-- ============================================================

-- ----------------------------
-- 通知公告
-- ----------------------------
DROP TABLE IF EXISTS `sys_notice`;
CREATE TABLE `sys_notice`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `title`       VARCHAR(200) DEFAULT NULL COMMENT '通知标题',
    `content`     TEXT         DEFAULT NULL COMMENT '通知内容',
    `type`        INT          DEFAULT 1 COMMENT '通知类型: 1-系统通知 2-业务通知',
    `status`      INT          DEFAULT 0 COMMENT '状态: 0-草稿 1-已发布',
    `remark`      VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    `tenant_id`   INT          DEFAULT NULL COMMENT '租户ID',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '通知公告';

-- ----------------------------
-- 站内消息
-- ----------------------------
DROP TABLE IF EXISTS `sys_message`;
CREATE TABLE `sys_message`
(
    `id`           BIGINT       NOT NULL COMMENT '主键',
    `user_id`      BIGINT       DEFAULT NULL COMMENT '接收用户ID',
    `title`        VARCHAR(200) DEFAULT NULL COMMENT '消息标题',
    `content`      TEXT         DEFAULT NULL COMMENT '消息内容',
    `message_type` INT          DEFAULT 1 COMMENT '消息类型: 1-系统 2-通知',
    `status`       INT          DEFAULT 0 COMMENT '状态: 0-未读 1-已读',
    `read_time`    DATETIME     DEFAULT NULL COMMENT '阅读时间',
    `jump_url`     VARCHAR(500) DEFAULT NULL COMMENT '跳转URL',
    `remark`       VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`    VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`    VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`      INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '站内消息';

-- ============================================================
-- 6. 审计日志
-- ============================================================

-- ----------------------------
-- 登录日志
-- ----------------------------
DROP TABLE IF EXISTS `sys_login_log`;
CREATE TABLE `sys_login_log`
(
    `id`          BIGINT       NOT NULL COMMENT '主键',
    `username`    VARCHAR(50)  DEFAULT NULL COMMENT '用户名',
    `ip`          VARCHAR(50)  DEFAULT NULL COMMENT '登录IP',
    `user_agent`  VARCHAR(500) DEFAULT NULL COMMENT '浏览器 UserAgent',
    `status`      INT          DEFAULT 1 COMMENT '状态: 0-失败 1-成功',
    `result`      VARCHAR(200) DEFAULT NULL COMMENT '结果描述',
    `login_time`  DATETIME     DEFAULT NULL COMMENT '登录时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`   VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`   VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`     INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '登录日志';

-- ----------------------------
-- 操作日志
-- ----------------------------
DROP TABLE IF EXISTS `sys_operate_log`;
CREATE TABLE `sys_operate_log`
(
    `id`             BIGINT       NOT NULL COMMENT '主键',
    `username`       VARCHAR(50)  DEFAULT NULL COMMENT '操作人用户名',
    `user_id`        BIGINT       DEFAULT NULL COMMENT '操作人用户ID',
    `dept_id`        BIGINT       DEFAULT NULL COMMENT '部门ID',
    `module`         VARCHAR(50)  DEFAULT NULL COMMENT '模块名',
    `name`           VARCHAR(100) DEFAULT NULL COMMENT '操作名称',
    `type`           INT          DEFAULT NULL COMMENT '操作类型',
    `request_method` VARCHAR(20)  DEFAULT NULL COMMENT '请求方法',
    `request_url`    VARCHAR(500) DEFAULT NULL COMMENT '请求URL',
    `request_params` TEXT         DEFAULT NULL COMMENT '请求参数',
    `request_result` TEXT         DEFAULT NULL COMMENT '请求结果',
    `cost_time`      BIGINT       DEFAULT NULL COMMENT '耗时（毫秒）',
    `ip`             VARCHAR(50)  DEFAULT NULL COMMENT '操作IP',
    `user_agent`     VARCHAR(500) DEFAULT NULL COMMENT '浏览器 UserAgent',
    `browser`        VARCHAR(50)  DEFAULT NULL COMMENT '浏览器名称',
    `os`             VARCHAR(50)  DEFAULT NULL COMMENT '操作系统',
    `client_key`     VARCHAR(20)  DEFAULT NULL COMMENT '客户端标识',
    `status`         INT          DEFAULT 1 COMMENT '状态: 0-失败 1-成功',
    `error_msg`      TEXT         DEFAULT NULL COMMENT '错误信息',
    `operate_time`   DATETIME     DEFAULT NULL COMMENT '操作时间',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`        INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '操作日志';

-- ============================================================
-- 7. OAuth2 与社交登录
-- ============================================================

-- ----------------------------
-- OAuth2 客户端管理
-- ----------------------------
DROP TABLE IF EXISTS `sys_client`;
CREATE TABLE `sys_client`
(
    `id`             BIGINT       NOT NULL COMMENT '主键',
    `client_id`      VARCHAR(100) DEFAULT NULL COMMENT '客户端ID',
    `client_key`     VARCHAR(100) DEFAULT NULL COMMENT '客户端Key',
    `client_secret`  VARCHAR(200) DEFAULT NULL COMMENT '客户端密钥',
    `grant_types`    VARCHAR(200) DEFAULT NULL COMMENT '授权类型（逗号分隔）',
    `device_type`    VARCHAR(20)  DEFAULT NULL COMMENT '设备类型',
    `active_timeout` BIGINT       DEFAULT NULL COMMENT '活跃超时（秒）',
    `timeout`        BIGINT       DEFAULT NULL COMMENT 'Token超时（秒）',
    `status`         INT          DEFAULT 1 COMMENT '状态: 1-启用 0-禁用',
    `remark`         VARCHAR(500) DEFAULT NULL COMMENT '备注',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`      VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`      VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`        INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = 'OAuth2 客户端';

-- ----------------------------
-- 社交登录绑定
-- ----------------------------
DROP TABLE IF EXISTS `sys_social`;
CREATE TABLE `sys_social`
(
    `id`            BIGINT       NOT NULL COMMENT '主键',
    `user_id`       BIGINT       DEFAULT NULL COMMENT '用户ID',
    `auth_id`       VARCHAR(100) DEFAULT NULL COMMENT '平台用户唯一标识',
    `source`        VARCHAR(50)  DEFAULT NULL COMMENT '平台来源（gitee/github/wechat 等）',
    `open_id`       VARCHAR(100) DEFAULT NULL COMMENT '平台 OpenId',
    `access_token`  VARCHAR(500) DEFAULT NULL COMMENT '访问令牌',
    `expire_in`     INT          DEFAULT NULL COMMENT '令牌过期时间（秒）',
    `refresh_token` VARCHAR(500) DEFAULT NULL COMMENT '刷新令牌',
    `nick_name`     VARCHAR(100) DEFAULT NULL COMMENT '平台昵称',
    `email`         VARCHAR(100) DEFAULT NULL COMMENT '平台邮箱',
    `avatar`        VARCHAR(500) DEFAULT NULL COMMENT '平台头像URL',
    `union_id`      VARCHAR(100) DEFAULT NULL COMMENT '平台 UnionId',
    `scope`         VARCHAR(200) DEFAULT NULL COMMENT '授权范围',
    `create_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP COMMENT '创建时间',
    `update_time` DATETIME NOT NULL DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP COMMENT '更新时间',
    `create_by`     VARCHAR(50)  DEFAULT NULL COMMENT '创建人',
    `update_by`     VARCHAR(50)  DEFAULT NULL COMMENT '更新人',
    `deleted`       INT          DEFAULT 0 COMMENT '逻辑删除',
    PRIMARY KEY (`id`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COLLATE = utf8mb4_0900_ai_ci
  COMMENT = '社交登录绑定';

-- ============================================================
-- 8. Seata AT 模式 undo_log
-- ============================================================
DROP TABLE IF EXISTS `undo_log`;
CREATE TABLE IF NOT EXISTS `undo_log`
(
    `branch_id`     BIGINT       NOT NULL COMMENT 'branch transaction id',
    `xid`           VARCHAR(128) NOT NULL COMMENT 'global transaction id',
    `context`       VARCHAR(128) NOT NULL COMMENT 'undo_log context',
    `rollback_info` LONGBLOB     NOT NULL COMMENT 'rollback info',
    `log_status`    INT          NOT NULL COMMENT '0:normal 1:defense',
    `log_created`   DATETIME(6)  NOT NULL COMMENT 'create datetime',
    `log_modified`  DATETIME(6)  NOT NULL COMMENT 'modify datetime',
    UNIQUE KEY `ux_undo_log` (`xid`, `branch_id`),
    INDEX `ix_log_created` (`log_created`)
) ENGINE = InnoDB
  DEFAULT CHARSET = utf8mb4
  COMMENT = 'Seata AT 模式 undo 表';
