# Matrix-Cloud 升级记录与待办计划

> 最后更新：2026-06-29

---

## 一、已完成事项

### 1.1 deploy-dev 中间件升级（commit `02d0577`）

| 中间件 | 升级前 | 升级后 | 说明 |
|--------|--------|--------|------|
| Nacos | v3.1.1 | **v3.2.2** | schema 迁移（新增 ai_resource/pipeline_execution 等表），dev 关闭三层鉴权 |
| MySQL | 8.0.30 | **8.0.46** | — |
| Redis | 6.2.7 | **7.4.9** | — |
| Elasticsearch | 7.17.10 | **8.19.17** | 重写 elasticsearch.yml（http.ssl off 保持 HTTP+basic auth） |
| SkyWalking OAP/UI | 9.6 | **10.4.0** | OAP 配置替换为 10.4.0 默认 + 自定义 |
| SkyWalking Agent | 9.0.0 | **9.6.0** | 186 文件树整体替换 |
| SkyWalking Toolkit | 9.0.0 | **9.6.0** | gradle/libs.versions.toml 版本对齐 |
| XXL-Job | 3.3.1 | **3.4.2** | schema 已应用 3.4.0 变更 |
| RocketMQ | 5.3.4 | **5.5.0** | compose 中已注释 |
| Sentinel | 1.8.5 | 1.8.5 | 镜像无新 tag，无法升级 |
| Seata Server | 2.5.0 | **2.6.0** | 注册中心改用 NamingServer（见下） |

**Seata NamingServer 部署：**
- 根因：Seata 2.6.0 捆绑 nacos-client 1.4.6，使用 v1 naming API（`/nacos/v1/ns/instance`），Nacos 3.x 已移除该 API（返回 501），结构性不兼容，无配置级修复
- 方案：部署 `apache/seata-naming-server:2.6.0.jdk25` 作为独立注册中心 + 控制台（端口 8081）
- seata-server 改为 `registry.type: seata`、`config.type: file`
- 移除 seata-config-init 容器，改用 nacos-cli 发布 seataServer.properties
- 应用端 matrix-seata registry 同步改为 seata（指向 namingserver）
- GitHub issue 参考：[apache/incubator-seata#8150](https://github.com/apache/incubator-seata/issues/8150)

**其他清理：**
- 删除 nacos-config.sh（用户决定改用 nacos-cli）
- 清理 seata.env 中 init 容器专用变量
- 补充 .gitignore 忽略运行时数据目录

### 1.2 仓库卫生清理（commit `d1c77a9` + `6be4dd4`）

- 移除误提交的运行时产物：redis/data、elasticsearch/{data,logs}、skywalking-agent/logs、img/*.png
- .gitignore 补充 deploy-dev 运行时数据 + skywalking-agent/logs 忽略规则

### 1.3 Spring Boot 4.0 升级（commit `8938c3e`）

**框架版本：**

| 组件 | 升级前 | 升级后 |
|------|--------|--------|
| Spring Boot | 3.5.7 | **4.0.7** |
| Spring Cloud | 2025.0.0 | **2025.1.2** |
| Spring Cloud Alibaba | 2025.0.0.0 | **2025.1.0.0** |
| mica | 3.5.7 | **4.1.0** |
| Spring Boot Admin | 3.5.6 | **4.1.1** |
| JDK (toolchain) | 17 | **21** |

**Starter 全量升级：**

| Starter | 升级前 | 升级后 |
|---------|--------|--------|
| sa-token | 1.44.0 | **1.45.0** |
| mybatis-plus | 3.5.15 | **3.5.16** |
| redisson | 3.52.0 | **4.6.1** |
| druid | 1.2.20 | **1.2.28** |
| dynamic-datasource | 4.3.0 | **4.5.0** |
| lock4j | 2.2.5 | **2.2.7** |
| xxl-job | 3.3.1 | **3.4.2** |
| easy-es | 3.0.1 | **3.0.2** |
| bean-searcher | 4.7.1 | **4.8.7** |
| mapstruct-plus | 1.3.5 | **1.5.1** |
| micrometer | 1.16.1 | **1.17.0** |
| hutool | 5.8.22 | **5.8.46** |
| guava | 31.1-jre | **33.6.0-jre** |
| fastjson2 | 2.0.48 | **2.0.62** |
| caffeine | 3.1.1 | **3.2.4** |
| transmittable | 2.13.2 | **2.14.5** |
| easyexcel | 3.3.2 | **4.0.3** |
| httpclient5 | 5.2.1 | **5.6.1** |
| lombok | 1.18.30 | **1.18.46** |

**boot3 → boot4 制品切换：**
- `mybatis-plus-spring-boot3-starter` → `-spring-boot4-starter`
- `druid-spring-boot-3-starter` → `-spring-boot-4-starter`
- `dynamic-datasource-spring-boot3-starter` → `-spring-boot4-starter`
- `sa-token-spring-boot3-starter` → `-spring-boot4-starter`
- `sa-token-reactor-spring-boot3-starter` → `-spring-boot4-starter`
- `spring-cloud-starter-gateway` → `spring-cloud-starter-gateway-server-webflux`（SC 2025.1 改名）

**版本源统一：**
- 插件版本从 `gradle.properties` 迁移到 `libs.versions.toml [plugins]`
- `settings.gradle` pluginManagement.plugins 移除，root build.gradle 改用 `alias()`
- buildscript spotbugs classpath 移除，改用 plugin alias
- 工具版本（checkstyle/pmd/spotless）走 toml accessor
- 删除死条目：jacoco、seata 1.7.1（io.seata 旧组，未使用）

**移除 knife4j/swagger：**
- 用户决定：移除 knife4j 所有依赖，改用 apifox 插件生成文档
- 删除 matrix-swagger 模块、toml alias、BOM constraint、gateway dep
- 清理 application-common.yml / gateway.yml 中 knife4j/springdoc 配置
- swagger-annotations 保留（matrix-common 直接依赖，@Schema 注解仍可用）

**SB4 代码迁移：**

| 变更 | 文件 | 说明 |
|------|------|------|
| Jackson 2 保留 | matrix-common/build.gradle | 加 `spring-boot-jackson2` 过渡模块 |
| JacksonConfig 包迁移 | matrix-web + matrix-gateway | `jackson.autoconfigure` → `jackson2.autoconfigure` |
| GrayVersionIsolationFilter | matrix-gateway/filter | 内联 LoadBalancerLifecycleValidator（SC 5.0 移除） |
| VersionLoadBalancer | matrix-feign/loadbalancer | `toSingleValueMap()` 替代 containsKey/getFirst |
| DeflectionIntanceFilter | matrix-gateway/filter | `get()` 替代 containsKey |
| GatewayExceptionHandler | matrix-gateway/handler | `ErrorWebExceptionHandler` → `WebExceptionHandler` |
| GatewayConfig | matrix-gateway/config | 移除 HttpMessageConverters bean（WebFlux 无需） |
| MetricsAutoConfiguration | matrix-prometheus | MeterRegistryCustomizer 包迁移（+spring-boot-micrometer-metrics） |
| CacheAutoConfiguration | matrix-redis | CacheProperties 包迁移（+spring-boot-cache） |
| KeyPrefixHandler | matrix-redis/handler | NameMapper 包迁移（redisson api → config） |
| ServletUtils | matrix-common | `APPLICATION_JSON_UTF8` → `APPLICATION_JSON` |
| WebFrameworkUtils | matrix-gateway | `APPLICATION_JSON_UTF8` → `APPLICATION_JSON` |
| IpRegionController | resource-biz | Long→String 适配 mica-ip2region 4.x |
| SecurityConfiguration | matrix-auth | +spring-webmvc compileOnly |
| mica-jobs 移除 | matrix-job | mica 4.x 已移除该模块，未使用 |
| xxl-job-core | matrix-tenant | +compileOnly（mica-jobs 移除后传递链断裂） |

---

## 二、待办计划

### 2.1 运行时冒烟测试 ✅ 已完成 (2026-06-30)

- [x] **gateway 启动验证**：SB4 启动 6s，Nacos 注册成功，Redisson 连接，sa-token 鉴权正常，Sentinel 网关过滤器加载
- [x] **admin 启动验证**：SB4 启动 3s，Nacos 注册成功，Spring Boot Admin UI 可访问(admin:admin)，实例检测正常
- [x] **resource-biz 启动验证**：Druid 数据源连接正常，MyBatis-Plus 3.5.16 初始化，Sentinel 注册，Sa-Token 鉴权
- [x] **定向回归**：Gateway 401 鉴权拦截正常；Gateway → Resource-Biz 路由转发正常；Admin UI 访问正常

**Phase 2 修复项：**
- Nacos OOM：mem_limit 1GB → 2GB（`deploy-dev/docker-compose.yml`）
- Resource-Biz 数据库连接：`application-dev.yml` 旧地址 → 127.0.0.1:3306/matrix
- SensitiveConfig 启动失败：添加 `@ConditionalOnProperty(prefix = "yidun", name = "secretId")`
- TransConfig 启动失败：添加 `@ConditionalOnBean(TransCacheManager.class)`

### 2.2 Gradle configuration-cache 修复 ✅ 已完成 (2026-06-30)

- [x] configuration-cache 在 Gradle 8.14.3 + Spring Boot 4.0.7 下默认可用，无需 `--no-configuration-cache`
- [x] `gradle/libs.versions.toml` 清理了 21 个无用别名

### 2.3 seata 客户端配置生效 ⚠️ 阻塞中

**已完成：**
- [x] seataServer.properties 已发布到 Nacos（dev 命名空间，SEATA_GROUP 分组）
- [x] vgroupMapping 已配置：`matrix-resource-group=default`, `matrix-system-group=default`
- [x] Seata 客户端成功从 Nacos 读取配置（NacosConfiguration 加载成功）

**阻塞问题：**
- **NamingServer JDK 兼容性**：`apache/seata-naming-server:2.6.0.jdk25` 运行在 JDK 25，与 `apache/seata-server:2.6.0`（JDK8）存在元数据格式不兼容，注册时报 `NamingServerNode.getTransaction() NPE`
- **Redis 注册 Jedis 版本冲突**：Seata 2.5.0 依赖 Jedis 5.x API（`redis.clients.jedis.ScanParams`），但 Spring Boot 4.0.7 传递依赖 Jedis 7.x（类已移到 `redis.clients.jedis.params.ScanParams`），`strictly` 约束被 Gradle 覆盖

**解决方案（待执行）：**
1. 升级 seata-server 到 JDK17 镜像（`apache/seata-server:2.6.0-java17`）以匹配 NamingServer
2. 或：排除 `spring-boot-data-redis` 的 Jedis 传递依赖，强制使用 Jedis 5.x
3. 或：等待 Seata 2.7.x 发布，预计修复 JDK 兼容性问题

**当前状态**：`seata.enabled: false`（resource-biz/bootstrap.yml）

### 2.4 SkyWalking 验证

- [ ] 启动 ES 8.19 + SkyWalking OAP 10.4.0 + UI 栈（docker-prometheus.yml）
- [ ] 验证 SkyWalking agent 9.6.0 注入到 Jib 构建的镜像
- [ ] 验证 OAP 连接 ES 8.19 正常，trace 数据可查

### 2.5 后续延后项目

| 项目 | 说明 | 优先级 |
|------|------|--------|
| Jackson 3 完整迁移 | 本轮保留 Jackson 2（经 `spring-boot-jackson2`），后续迁移到 `tools.jackson` 组，涉及 ~15 文件 import 变更 + JavaTimeModule 合并 | 低 |
| matrix-system 模块启用 | settings.gradle 中已注释，启用时需 `javax.validation`/`javax.annotation` → `jakarta.*` | 低 |
| easyexcel 4.x API 适配 | 编译通过但未验证运行时，4.x 有 API 变更（EasyExcel API 改名等） | 中 |
| seata 客户端版本精确对齐 | SCA BOM 管理 seata 2.5.0，已部署 server 2.6.0。2.x 内 client/server 兼容，可选 override 到 2.6.0 | 低 |
| dead [libraries] alias 清理 | toml 中约 32 个 library alias 未被模块引用（模块用版本化 GAV 字符串），风格不一致但无版本风险 | 低 |

---

## 三、版本速查

### 框架版本（gradle/libs.versions.toml 权威）

```
springboot          = 4.0.7
springCloud         = 2025.1.2
springCloudAlibaba  = 2025.1.0.0
mica                = 4.1.0
dependency-management = 1.1.7
```

### 中间件版本（deploy-dev/.env 权威）

```
NACOS_VERSION       = v3.2.2
MYSQL_VERSION       = 8.0.46
REDIS_VERSION       = 7.4.9
SEATA_VERSION       = 2.6.0
SEATA_NAMING_VERSION = 2.6.0.jdk25
ELASTIC_VERSION     = 8.19.17
SKYWALING_VERSION   = 10.4.0-java17
SKYWALING_UI_VERSION = 10.4.0-java17
XXL_JOB_VERSION     = 3.4.2
ROCKETMQ_VERSION    = 5.5.0
SENTINEL_VERSION    = 1.8.5
```

### 服务端口

| 服务 | 端口 |
|------|------|
| Gateway | 9000 |
| Admin | 9002 |
| Resource | 9003 |
| Nacos | 8848 |
| Seata NamingServer | 8081 |
| Seata Server RPC | 8091 |
| XXL-Job Admin | 9100 |
| SkyWalking UI | 8080 |

---

## 四、关键决策记录

| 决策 | 原因 | 日期 |
|------|------|------|
| Nacos 保持 3.2.2，Seata 改用 NamingServer | Nacos 3.x 移除 v1 naming API，Seata 2.6.0 捆绑旧 nacos-client 无法注册，NamingServer 是 Seata 官方推荐方式 | 2026-06-26 |
| 移除 knife4j，改用 apifox | knife4j 4.5.0 不支持 SB4（依赖 springdoc 2.x），无 SB4 版本 | 2026-06-29 |
| 保留 Jackson 2（spring-boot-jackson2） | SB4 Jackson 迁移影响面大（~15 文件 + easy-es/bean-searcher 兼容），过渡模块可保兼容 | 2026-06-29 |
| JDK 17 → 21 | 用户选择，SB4 支持 21，可享用虚拟线程等新特性 | 2026-06-29 |
| 全量 starter 升级到最新 | 用户选择，确保兼容性 | 2026-06-29 |
