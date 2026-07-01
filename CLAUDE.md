# CLAUDE.md

## Project Overview

**Matrix-Cloud** — Java 21 微服务脚手架。Spring Boot 4.1.0 + Spring Cloud 2025.1.2 + Spring Cloud Alibaba 2025.1.0.0。

**权威版本来源**: `gradle/libs.versions.toml` 和 `matrix-bom/build.gradle`，始终查阅这两个文件而非本文件中的版本号。

## Build

```bash
./gradlew build                              # 构建全部
./gradlew :matrix-core:matrix-common:build   # 构建指定模块
./gradlew jib                                # Docker镜像(Jib)
./gradlew test                               # 测试(默认禁用)
```

关键文件：`gradle/libs.versions.toml`(版本目录) | `matrix-bom/build.gradle`(BOM约束) | `deploy.gradle`(Jib) | `settings.gradle`(模块定义)

Java 21 通过 toolchain (`JavaLanguageVersion.of(21)`)，测试默认禁用 (`test.enabled=false`)。

## Architecture

```
matrix-cloud/
├── matrix-bom/               # BOM 依赖版本约束
├── matrix-core/              # 38个可插拔核心模块
│   ├── matrix-common/        # R<T>响应、IResultCode/ErrorCode异常、BaseEntity(含clean防御)、脱敏(10种注解)、TTL上下文、JS精度序列化
│   ├── matrix-web/           # Web Starter：全局异常、Jackson、i18n、XSS过滤(Jsoup)
│   ├── matrix-auth/          # Sa-Token+JWT：登录、权限、@ApiSignature、验证码、租户鉴权
│   ├── matrix-feign/         # OpenFeign：租户/认证/灰度版本请求头传播、版本路由LB
│   ├── matrix-mybatis/       # MyBatis-Plus：BaseMapperX/Vo/乐观锁/雪花ID/多数据源/连表查询/跨数据库兼容
│   ├── matrix-redis/         # Redis+Redisson：Spring Cache、@RateLimiter、自定义TTL缓存
│   ├── matrix-tenant/        # 多租户：SQL自动拼接tenant_id、缓存隔离、@TenantIgnore
│   ├── matrix-crypto/        # @ApiEncrypt AES/RSA 请求解密+响应加密
│   ├── matrix-log/           # @Log操作日志 + @ExceptionNoticeLog异常通知
│   ├── matrix-mq/            # RocketMQ：消息模板、Listener自动注册、租户上下文传播
│   ├── matrix-job/           # XXL-Job：XxlJobSpringExecutor自动配置+自动端口IP
│   ├── matrix-seata/         # Seata AT分布式事务
│   ├── matrix-sentinel/      # Sentinel：自定义SlotChain、QPS监控
│   ├── matrix-strategy/      # @HandlerType → BusinessHandlerChooser 策略注入
│   ├── matrix-translation/   # @Translation 字段翻译(字典/用户名/地区/图片URL)
│   ├── matrix-data-permission/ # @DataPermission 行级数据权限 SQL拦截
│   ├── matrix-idempotent/    # @RepeatSubmit 防重复提交
│   ├── matrix-lock/          # Lock4j分布式锁(Redisson)
│   ├── matrix-sensitive/     # 网易易盾内容审核
│   ├── matrix-websocket/     # WebSocket多节点广播(Redis)、消息监听器SPI
│   ├── matrix-ip/            # IP定位(ip2region)+行政区划(四级树形area.csv)
│   ├── matrix-doc/           # SpringDoc OpenAPI文档(Swagger UI)、Sa-Token认证集成
│   ├── matrix-mail/          # Jakarta Mail邮件发送、MailBuilder链式构建
│   ├── matrix-social/        # JustAuth第三方登录(Gitee/GitHub/微信/钉钉/飞书)
│   ├── matrix-oss/           # S3兼容对象存储(AWS SDK)：上传/下载/预签名URL/批量删除
│   ├── matrix-sms/           # SMS4J多供应商短信
│   ├── matrix-excel/         # EasyExcel导入导出(@ExcelEnumFormat/@ExcelDynamicOptions/@ExcelNotation/@ExcelRequired + ExcelBuilder)
│   ├── matrix-es/            # Easy-Es Elasticsearch
│   ├── matrix-es/            # Easy-Es Elasticsearch ORM自动配置
│   ├── matrix-mongodb/       # MongoDB EasyMongoService
│   ├── matrix-prometheus/    # Prometheus指标 + trace-id
│   ├── matrix-jpush/         # 极光推送
│   ├── matrix-validator/     # @InEnum/@PhoneValue 校验
│   ├── matrix-api/           # 常用模块聚合 + IBaseFeignClient
│   ├── matrix-test/          # 测试基础设施
│   ├── matrix-auto/          # mica-auto注解处理器
│   └── matrix-config/        # Nacos配置中心聚合
├── matrix-admin/             # Spring Boot Admin (9002)
├── matrix-gateway/           # API网关(9000)：认证/灰度/XSS/黑名单/限流
├── matrix-resource/          # 资源服务(9003)：api→biz分层
└── matrix-system/            # 系统服务(未启用)
```

## Coding Patterns

### API响应与异常
所有接口返回 `R<T>`，工厂方法 `R.success()` / `R.fail(msg)` / `R.fail(IResultCode)`。
```java
UserDTO user = userClient.getUser(id).getCheckedData();          // Feign获取数据，失败自动抛异常
userClient.save(user).checkError(SystemErrorTypeEnum.OPERATE_FAIL); // 检查错误并自定义异常
```
- `IResultCode` → `SystemErrorTypeEnum`(1xxx) / `BusinessErrorTypeEnum`(2xxx-5xxx)
- `ErrorCode(1001, "用户{}不存在").exception(id)` — 占位符+异常构造链
- `ServiceException(IResultCode)` 或 `ServiceException(R<?>)`

### 实体继承
`BaseIdEntity`(雪花id) → `BaseEntity`(+审计字段+@TableLogic) → `TenantEntity`(+tenantId) / `TreeEntity<T>`(+parentId/children)
- `clean()` / `cleanCreateFields()` / `cleanUpdateFields()` — 防御性清理审计字段

### 上下文传播
全部使用 `TransmittableThreadLocal`(阿里TTL)，异步自动传播：
`LoginUserContextHolder` → `TenantContextHolder` → `LbIsolationContextHolder`

### 自动配置
使用 **mica-auto** 注解处理器，`@AutoConfiguration` 标注即自动注册，禁止手写 `spring.factories`。

### 常用注解速查
| 注解 | 模块 | 用途 |
|------|------|------|
| `@Sensitive` / `@MobileDesensitize` / `@IdCardDesensitize` / `@BankCardDesensitize` / `@EmailDesensitize` / `@NameDesensitize` / `@PasswordDesensitize` / `@AddressDesensitize` / `@FixedPhoneDesensitize` / `@IpDesensitize` / `@LicensePlateDesensitize` | common | 字段脱敏(10种独立注解) |
| `@Translation` | translation | 字段翻译 |
| `@DataPermission` / `@TenantIgnore` | data-permission/tenant | 数据权限+租户隔离 |
| `@RepeatSubmit` / `@RateLimiter` / `@ApiSignature` | idempotent/redis/auth | 防重/限流/API签名 |
| `@ApiEncrypt` / `@EncryptField` / `@Log` / `@ExceptionNoticeLog` | crypto/log | API加解密/MyBatis字段加解密/日志/异常通知 |
| `CryptoService`(AES/RSA/SM4/SM2) / `MailBuilder` / `SocialAuthService` | crypto/mail/social | 多算法加解密/链式邮件/第三方OAuth |
| `@HandlerType` / `@InEnum` / `@PhoneValue` | strategy/validator | 策略注入/校验 |
| `@JsonSerialize(using=NumberSerializer)` / `@JsonSerialize(using=LongToStringSerializer)` | common | Long JS精度保护 |
| `MPJLambdaWrapperX` / `LongListTypeHandler` / `IntegerListTypeHandler` / `LongSetTypeHandler` | mybatis | 连表查询+集合TypeHandler |
| `WebSocketMessageListener<T>` / `IPUtils` | websocket/ip | 消息监听SPI/IP定位 |

### 认证
Sa-Token JWT(simple) + Redis。`LoginHelper.loginByDevice(…)` 登录，`LoginHelper.getLoginUser()` 获取用户。网关内部校验：`SaSameUtil.checkCurrentRequestToken()`。

### Feign
请求头自动传播（tenantId/Sa-Token/userId/灰度版本），API模块放 `xxx-api`，客户端实现 `IBaseFeignClient`，熔断用 `DefaultFallbackFactory`。

## 新增微服务

1. `settings.gradle` 添加 include，创建模块 + `build.gradle`（依赖 `matrix-core:matrix-web`）
2. 主类标注 `@SpringBootApplication` + `@EnableDiscoveryClient` + `@EnableFeign`
3. 创建 `bootstrap.yml`（参考 `matrix-resource/resource-biz`），`deploy.gradle` 的 `microservices` 添加模块

bootstrap.yml 模板：
```yaml
spring.application.name: service-name
spring.profiles.active: ${PROFILE:}
spring.config.import:
  - optional:nacos:env.properties
  - optional:nacos:application-common.yml
  - optional:nacos:datasource.yml
```

## Configuration

环境：`PROFILE` 环境变量 (dev/prod) → Nacos namespace。本地配置：`config/dev/`，Nacos：`config/nacos/`，SQL：`deploy/sql/`。

`matrix.*` 前缀：`captcha.validateUrl` | `access-log.enable` | `load-balance.gray` | `tenant.enable` | `mq.enabled` | `crypto.enabled/type/secretKey` | `rate-limiter.enabled` | `xss.enabled/excludeUrls` | `websocket.enabled/path/sender-type`

## Ports

| 服务 | 端口 |
|------|------|
| Gateway | 9000 |
| Admin | 9002 |
| Resource | 9003 |

## Docker

中间件：`deploy/docker-compose.yml`，应用：`deploy/docker-matrix.yml`。`./gradlew jib` 构建，`PROFILE` 切换环境，SkyWalking agent 自动注入(admin除外)。
