# CLAUDE.md

## Project Overview

**Matrix-Cloud** — Java 17 微服务脚手架框架。Spring Boot 3.5.7 + Spring Cloud 2025.0.0 + Spring Cloud Alibaba 2025.0.0.0。

**权威版本来源**: `version.gradle` 和 `matrix-bom/build.gradle`，不要依赖本文件中的版本号，始终查阅这两个文件。

## Build

```bash
./gradlew build                          # 构建全部模块
./gradlew :matrix-core:matrix-common:build  # 构建指定模块
./gradlew clean build                    # 清理构建
./gradlew jib                            # Docker镜像构建推送(Jib)
./gradlew test                           # 测试(默认禁用 test.enabled=false)
```

关键构建文件：
- `build.gradle` — 根构建配置，所有子模块共享 `commons` 配置块
- `version.gradle` — **统一版本管理**，所有第三方依赖版本在此声明
- `matrix-bom/build.gradle` — BOM平台，约束所有依赖版本
- `deploy.gradle` — Jib Docker构建，`microservices` 列表控制哪些服务打包镜像
- `settings.gradle` — 模块定义，matrix-system 已注释掉（未启用）

Java 17 通过 toolchain 配置（`JavaLanguageVersion.of(17)`），非 sourceCompatibility。

测试默认关闭（`build.gradle` `test { enabled = false }`）。测试基础设施在 `matrix-core/matrix-test`。

## Architecture

```
matrix-cloud/
├── matrix-bom/               # BOM 统一依赖版本约束
├── matrix-core/              # 31个可插拔核心模块
│   ├── matrix-common/        # 基础：R<T>响应、异常体系(IResultCode)、实体基类、上下文Holder、脱敏、工具集
│   ├── matrix-auto/          # 全局 @ConfigurationProperties + mica-auto注解处理器
│   ├── matrix-web/           # Web Starter：全局异常、Jackson、i18n、访问日志、上下文传播
│   ├── matrix-auth/          # Sa-Token+JWT认证：登录、权限、网关内部令牌、验证码、租户鉴权
│   ├── matrix-feign/         # OpenFeign：请求头传播(租户/认证/灰度版本)、版本路由负载均衡
│   ├── matrix-mybatis/       # MyBatis-Plus 3.5.15：BaseMapperX/Vo查询/乐观锁/雪花ID/自动填充
│   ├── matrix-redis/         # Redis+Redisson：单机/主从/集群、Spring Cache、RedisUtils
│   ├── matrix-tenant/        # 多租户：SQL自动拼接tenant_id、缓存隔离、租户Job遍历、@TenantIgnore
│   ├── matrix-swagger/       # Knife4j 4.5.0 OpenAPI 3 文档
│   ├── matrix-log/           # 操作日志(@Log) + 异常通知(@ExceptionNoticeLog)
│   ├── matrix-mq/            # RocketMQ：同步/异步/顺序/事务消息模板，自动注册Listener
│   ├── matrix-job/           # XXL-Job：自动计算executor端口
│   ├── matrix-seata/         # Seata AT模式分布式事务，自动建undo_log表
│   ├── matrix-sentinel/      # Sentinel增强：自定义SlotChain(流量/降级预警)、QPS监控
│   ├── matrix-strategy/      # 策略模式自动注入(@HandlerType → BusinessHandlerChooser)
│   ├── matrix-translation/   # 字段翻译(@Translation)：字典/用户名/地区/图片URL
│   ├── matrix-data-permission/ # 行级数据权限：@DataPermission、SQL拦截改写
│   ├── matrix-idempotent/    # 防重复提交(@RepeatSubmit)、Redis存储
│   ├── matrix-lock/          # Lock4j分布式锁(Redisson)
│   ├── matrix-sensitive/     # 网易易盾内容审核(文本/图片)
│   ├── matrix-oss/           # 阿里云OSS依赖聚合
│   ├── matrix-sms/           # SMS4J多供应商短信
│   ├── matrix-excel/         # EasyExcel导入导出(字典转换/单元格合并)
│   ├── matrix-es/            # Easy-Es Elasticsearch ORM聚合
│   ├── matrix-mongodb/       # MongoDB MyBatis-Plus风格封装(EasyMongoService)
│   ├── matrix-prometheus/    # Prometheus指标 + trace-id响应头
│   ├── matrix-jpush/         # 极光推送多App客户端
│   ├── matrix-validator/     # 自定义校验(@InEnum/@PhoneValue/@DateValue)
│   ├── matrix-api/           # 常用模块聚合 + IBaseFeignClient标准CRUD契约
│   ├── matrix-test/          # 测试基础设施(Mockito基类/随机POJO)
│   └── matrix-config/        # Nacos配置中心依赖聚合
├── matrix-admin/             # Spring Boot Admin 监控 (端口9002)
├── matrix-gateway/           # API网关 (端口9000) — 认证/灰度/XSS/黑名单/限流/i18n
├── matrix-resource/          # 资源服务 (端口9003)
│   ├── resource-api/         # Feign API契约(DTO/VO/枚举)
│   └── resource-biz/         # 业务实现(OSS/SMS/OCR/极光/IP地区)
└── matrix-system/            # 系统服务(已注释，未启用)
```

## Coding Patterns (关键约定)

### 统一API响应
所有接口返回 `R<T>`（`com.matrix.common.result.R`）。工厂方法：`R.success()`, `R.fail(msg)`, `R.fail(IResultCode)`。

### 异常体系
- 错误码接口：`IResultCode` → 实现：`SystemErrorTypeEnum`(1xxx), `BusinessErrorTypeEnum`(2xxx-5xxx)
- 业务异常：`ServiceException(IResultCode)` 或 `ServiceException(R<?>)`
- 工具构造：`ServiceExceptionUtil.exception(...)` 支持i18n模板

### 实体继承
- `BaseIdEntity` — snowflake id + TransPojo
- `BaseEntity` — + createdBy/createdAt/updatedBy/updatedAt/deleted(@TableLogic)
- `TenantEntity` — + tenantId
- `TreeEntity<T>` — + parentId/children

### Mapper/Service继承
- Mapper：`BaseMapperX<M,T,V>` — Vo查询、批量操作、FOR UPDATE
- Service：`IRootService<T,V>` / `RootServiceImpl` — idempotent save、Vo分页

### 上下文传播
所有 ThreadLocal 使用 `TransmittableThreadLocal`（阿里TTL），异步线程通过 `ContextCopyingDecorator` 或 `CustomThreadPoolTaskExecutor`(TtlRunnable/TtlCallable) 自动传播：
- `LoginUserContextHolder` — 当前登录用户
- `TenantContextHolder` — 当前租户ID + 忽略租户标志
- `LbIsolationContextHolder` — 灰度版本字符串

### 模块自动配置注册
使用 **mica-auto** 注解处理器（compile-time），不要手动写 `spring.factories` 或 `.imports` 文件。配置类标注 `@AutoConfiguration` 即自动注册。

### 常用注解速查
| 注解 | 模块 | 用途 |
|------|------|------|
| `@Sensitive` | common | 字段脱敏(Jackson序列化时) |
| `@SensitiveCheck` | common | 内容审核触发(文本/图片) |
| `@Translation` | translation | 字段翻译(字典/用户名/地区) |
| `@DataPermission` | data-permission | 行级数据权限 |
| `@TenantIgnore` | tenant | 跳过租户SQL过滤 |
| `@RepeatSubmit` | idempotent | 防重复提交(5秒间隔) |
| `@HandlerType(type, source)` | strategy | 策略模式标记 → BusinessHandlerChooser |
| `@Log` | log | 操作日志记录 |
| `@ExceptionNoticeLog` | log | 异常通知 |
| `@EnableFeign` | feign | 启用Feign客户端+版本负载均衡 |
| `@InEnum` / `@PhoneValue` | validator | 自定义校验 |

### 认证模式
Sa-Token JWT(simple模式) + Redis持久化。登录：`LoginHelper.loginByDevice(LoginUser, DeviceTypeEnum)`。获取当前用户：`LoginHelper.getLoginUser()`。权限接口：`SaPermissionImpl`(StpInterface)。

网关内部调用校验：`SaSameUtil.checkCurrentRequestToken()` — 确保请求经过网关。

### Feign调用规范
- 请求头自动传播：tenantId、Sa-Token、userId、灰度版本 — 由 `FeignAutoConfig` 的 `RequestInterceptor` 处理
- Feign API模块放在 `xxx-api` 子模块（如 `resource-api`），biz模块依赖api模块
- Feign客户端实现 `IBaseFeignClient<T,V,Q>` 提供标准CRUD契约
- 熔断降级：`DefaultFallbackFactory`

## 新增微服务步骤

1. `settings.gradle` 添加 include
2. 创建模块 `build.gradle`，依赖 `matrix-core:matrix-web`（已包含 common/auto/validator/feign等）
3. 主类标注 `@SpringBootApplication` + `@EnableDiscoveryClient` + `@EnableFeign`
4. 创建 `bootstrap.yml`（参考 matrix-resource/resource-biz 的配置）
5. `deploy.gradle` 的 `microservices` 列表添加模块（用于Docker构建）

## Configuration

- 本地配置：`config/dev/` 目录
- Nacos配置：`config/nacos/`
- 环境切换：环境变量 `PROFILE`（dev/prod，映射到Nacos namespace）
- SQL脚本：`deploy/sql/`

bootstrap.yml 标准模式：
```yaml
spring:
  application:
    name: service-name
  profiles:
    active: ${PROFILE:}
  config:
    import:
      - optional:nacos:env.properties
      - optional:nacos:application-common.yml
      - optional:nacos:datasource.yml
```

matrix 框架配置前缀 `matrix.*`：
- `matrix.security.captcha.validateUrl` — 验证码校验URL
- `matrix.access-log.enable` — API访问日志
- `matrix.load-balance.gray.enabled` / `defaultVersion` — 灰度负载均衡
- `matrix.swagger.enable` — Knife4j文档开关
- `matrix.tenant.enable` — 多租户开关

## Service Ports

| 服务 | 端口 |
|------|------|
| Gateway | 9000 |
| Admin | 9002 |
| Resource | 9003 |
| System | 9002 (未启用) |

中间件端口见 `deploy/docker-compose.yml` 和 `deploy/README.md`。

## Docker

- 中间件：`deploy/docker-compose.yml`
- 应用服务：`deploy/docker-matrix.yml`
- 镜像构建：`./gradlew jib`，环境通过 `PROFILE` 切换(dev/prod)
- SkyWalking agent 自动注入（admin除外）
