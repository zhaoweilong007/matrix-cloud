# Matrix-Cloud

## ✨项目介绍

Matrix-Cloud是一个基于Spring Cloud Alibaba的企业级微服务脚手架，整合了目前主流的微服务框架和中间件，提供了完整的微服务架构解决方案。

### 🎯设计理念

- **模块化设计**：核心组件与业务模块分离，便于扩展和维护
- **开箱即用**：提供完整的微服务解决方案，可直接基于此框架开发业务应用
- **主流技术栈**：整合目前主流的微服务框架和中间件
- **高可用设计**：支持服务注册发现、熔断限流、分布式事务等高可用特性
- **易扩展性**：支持自定义组件接入，通过简单的配置即可集成到现有架构中
- **完善的监控体系**：集成了SkyWalking、Prometheus、ELK等监控和日志系统

### 📋项目目标

- 为企业提供一套完整的微服务架构解决方案
- 降低微服务架构的学习和使用成本
- 提高微服务应用的开发效率和质量
- 支持快速构建和部署高可用、可扩展的微服务应用

### 🔨项目环境

#### 开发环境

- **JDK**: 17
- **Gradle**: 8.14.3
- **IDE**: IntelliJ IDEA 或 Eclipse

#### 中间件版本

| 中间件类型       | 版本    | 用途说明                |
|--------------|-------|---------------------|
| Nacos        | 2.2.1 | 服务注册发现与配置中心          |
| Sentinel     | 1.8.6 | 熔断限流                |
| Seata        | 1.7.1 | 分布式事务               |
| RocketMQ     | 4.9.4 | 消息队列                |
| SkyWalking   | 9.0.0 | 分布式链路追踪             |
| Elasticsearch| 7.17.6| 搜索引擎与日志存储           |
| Kibana       | 7.17.6| 日志分析与可视化           |
| Logstash     | 7.17.6| 日志收集与处理            |
| Prometheus   | 1.16.1| 监控数据收集              |
| Grafana      | latest| 监控数据可视化             |
| XXL-Job      | 3.3.1 | 分布式任务调度             |

## 🚀快速开始

### 1. 环境准备

- 安装JDK 17并配置环境变量
- 安装Gradle 8.14.3并配置环境变量
- 克隆项目代码

### 2. 启动中间件

#### 使用Docker Compose一键部署

```bash
# 进入部署目录
cd deploy

# 启动所有中间件
docker-compose up -d
```

#### 手动部署中间件

请参考各中间件官方文档进行部署，部署完成后确保各中间件服务正常运行。

### 3. 初始化数据库

- 执行`deploy/sql`目录下的SQL脚本
- 先执行`nacos.sql`初始化Nacos配置中心
- 再执行`seata.sql`初始化Seata分布式事务
- 最后执行`matrix.sql`初始化业务数据库

### 4. 编译项目

```bash
# 编译整个项目
./gradlew.bat build

# 编译指定模块
./gradlew.bat :matrix-system:system-biz:build
```

### 5. 启动服务

按照以下顺序启动服务：

1. 启动`matrix-gateway`（网关服务）
2. 启动`matrix-system`（系统服务）
3. 启动`matrix-resource`（资源服务）
4. 启动`matrix-admin`（管理服务）

### 6. 访问服务

启动完成后，可通过以下地址访问各服务：

| 服务名称          | 访问地址                                  | 备注                |
|---------------|---------------------------------------|-------------------|
| Nacos控制台     | http://localhost:8848/nacos           | 用户名/密码：nacos/nacos |
| Sentinel控制台  | http://localhost:8088/dashboard       | 用户名/密码：sentinel/sentinel |
| 网关服务          | http://localhost:9000                 | 主要访问入口            |
| API文档         | http://localhost:9000/doc.html        | Swagger文档聚合       |
| Spring Boot Admin | http://localhost:9001                 | 应用监控             |

## �️技术选型

### 核心框架

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| Spring Cloud Alibaba | 2025.0.0.0       | 微服务基础框架             |
| Spring Cloud         | 2025.0.0         | 微服务生态              |
| Spring Boot          | 3.5.7            | 应用开发框架              |

### 服务治理

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| Nacos                | 2.2.1            | 服务注册发现与配置中心          |
| Spring Cloud Gateway | 依赖Spring Cloud版本 | 网关服务，处理请求路由、鉴权等     |
| Sentinel             | 1.8.6            | 熔断限流                |
| Seata                | 1.7.1            | 分布式事务               |

### 安全认证

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| Sa-Token             | 1.44.0           | 权限认证                |

### 数据持久化

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| MyBatis-Plus         | 3.5.15           | ORM框架               |
| Redis                | 6.2.x            | 缓存数据库               |
| MongoDB              | 4.4.x            | 文档数据库               |
| Elasticsearch        | 7.17.6           | 搜索引擎与日志存储           |

### 消息队列

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| RocketMQ             | 4.9.4            | 消息队列                |

### 监控与日志

| 技术框架                 | 版本               | 用途说明                |
|----------------------|------------------|---------------------|
| SkyWalking           | 9.0.0            | 分布式链路追踪             |
| ELK Stack            | 7.17.6           | 日志收集、存储与分析         |
| Prometheus           | 1.16.1           | 监控数据收集              |
| Grafana              | latest           | 监控数据可视化             |
| Spring Boot Admin    | 3.5.6            | Spring Boot应用监控     |

### 开发工具

| 工具名称               | 版本               | 用途说明                |
|------------------|------------------|---------------------|
| Knife4j           | 4.5.0            | Swagger增强工具，API文档生成 |
| XXL-Job           | 3.3.1            | 分布式任务调度             |
| Jib               | 3.5.2            | Docker镜像构建工具        |

### 工具库

| 工具库名称              | 版本               | 用途说明                |
|-------------------|------------------|---------------------|
| Hutool            | 5.8.22           | Java工具类库           |
| Lombok            | 1.18.30          | 简化Java代码           |
| MapStruct Plus    | 1.3.5            | 对象映射工具             |
| EasyExcel         | 3.3.2            | Excel导入导出           |

## 📌核心功能

| 功能模块         | 状态 | 描述说明                |
|--------------|----|---------------------|
| RBAC权限管理     | ✅  | 基于Sa-Token的角色权限管理     |
| 聚合Swagger文档  | ✅  | 统一管理所有微服务的API文档     |
| 多租户管理       | ✅  | 支持多租户模式，实现数据隔离     |
| 动态路由         | ✅  | 基于Nacos的配置，修改后可实时生效  |
| 灰度发布         | ✅  | 支持基于版本和IP的灰度发布      |
| 分布式事务        | ✅  | 集成Seata实现分布式事务管理     |
| 分布式链路追踪     | ✅  | 使用SkyWalking进行分布式链路追踪 |
| 日志收集分析      | ✅  | 集成ELK进行日志收集和分析      |
| 应用监控         | ✅  | 使用Prometheus和Grafana进行应用监控 |
| 分布式任务调度     | ✅  | 集成XXL-Job实现分布式任务调度   |
| 熔断限流         | ✅  | 集成Sentinel实现熔断限流       |
| 幂等性校验        | ✅  | 防止重复请求             |
| 敏感数据脱敏       | ✅  | 敏感数据自动脱敏           |
| 数据权限控制       | ✅  | 基于注解的数据权限隔离        |

## 🔧模块架构

### 系统架构图

```
┌────────────────────────────────────────────────────────────────────┐
│                          客户端请求                                │
└───────────────────┬────────────────────────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────────────────────────┐
│                        Matrix-Gateway（网关层）                    │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐    │
│  │  路由   │  │  鉴权   │  │  限流   │  │  熔断   │  │  灰度   │    │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘    │
└───────────────────┬────────────────────────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────────────────────────┐
│                        服务注册发现（Nacos）                         │
└───────────────────┬────────────────────────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────────────────────────┐
│                          业务服务层                                │
│  ┌─────────────────┐  ┌─────────────────┐  ┌─────────────────┐     │
│  │  Matrix-System  │  │ Matrix-Resource │  │   自定义服务    │     │
│  └─────────────────┘  └─────────────────┘  └─────────────────┘     │
└───────────────────┬────────────────────────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────────────────────────┐
│                          公共组件层                                │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐    │
│  │  Auth   │  │  Feign  │  │  Redis  │  │  MQ     │  │  Seata  │    │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘    │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐    │
│  │  Log    │  │  Excel  │  │  OSS    │  │  SMS    │  │  Job    │    │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘    │
└───────────────────┬────────────────────────────────────────────────┘
                    │
┌───────────────────▼────────────────────────────────────────────────┐
│                          数据存储层                                │
│  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐  ┌─────────┐    │
│  │  MySQL  │  │  Redis  │  │ MongoDB │  │   ES    │  │ RocketMQ│    │
│  └─────────┘  └─────────┘  └─────────┘  └─────────┘  └─────────┘    │
└────────────────────────────────────────────────────────────────────┘
```

### 业务模块说明

| 模块名称           | 服务地址       | 端口 | 主要功能                |
|----------------|------------|----|---------------------|
| matrix-gateway | 127.0.0.1  | 9000| 网关服务，处理请求路由、鉴权等     |
| matrix-system  | 127.0.0.1  | 9002| 系统服务，提供用户、角色、权限等基础功能 |
| matrix-resource| 127.0.0.1  | 9003| 资源服务，提供OSS、SMS、Email等功能 |
| matrix-admin   | 127.0.0.1  | 9001| Spring Boot Admin，用于监控和管理Spring Boot应用 |

## �公共组件说明

### 核心组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-bom**     | `matrix-bom`            | 依赖统一版本管理，集中管理所有依赖的版本号       |
| **matrix-common**  | `matrix-core:matrix-common` | 公共工具类库，提供基础的工具类和通用功能         |
| **matrix-web**     | `matrix-core:matrix-web` | Web组件，提供对Servlet服务的支持及相关配置     |
| **matrix-auto**    | `matrix-core:matrix-auto` | 自动配置组件，提供各种自动装配的配置类         |

### 服务治理组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-api**     | `matrix-core:matrix-api` | 子服务API接口依赖，提供服务间调用基础功能       |
| **matrix-feign**   | `matrix-core:matrix-feign` | 集成OpenFeign，支持版本号负载均衡        |
| **matrix-config**  | `matrix-core:matrix-config` | 公共的Nacos配置，默认自动读取bootstrap.properties |

### 安全认证组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-auth**    | `matrix-core:matrix-auth` | 权限认证相关功能，基于Sa-Token实现       |
| **matrix-sensitive** | `matrix-core:matrix-sensitive` | 敏感数据脱敏，支持多种脱敏策略         |
| **matrix-idempotent** | `matrix-core:matrix-idempotent` | 幂等性校验，防止重复请求             |

### 数据处理组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-mybatis** | `matrix-core:matrix-mybatis` | 集成MyBatis-Plus，提供ORM框架支持     |
| **matrix-redis**   | `matrix-core:matrix-redis` | Redis操作封装，提供Redis相关功能     |
| **matrix-es**      | `matrix-core:matrix-es` | 集成ElasticSearch，提供ES相关操作     |
| **matrix-mongodb** | `matrix-core:matrix-mongodb` | 集成MongoDB，提供MongoDB相关操作     |
| **matrix-excel**   | `matrix-core:matrix-excel` | 通用的Excel操作，提供导入导出功能       |
| **matrix-translation** | `matrix-core:matrix-translation` | 翻译相关功能，如字典翻译、字段id>name翻译 |
| **matrix-validator** | `matrix-core:matrix-validator` | 校验组件，提供数据校验功能           |

### 分布式组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-seata**   | `matrix-core:matrix-seata` | 集成Seata，提供分布式事务支持        |
| **matrix-sentinel** | `matrix-core:matrix-sentinel` | 集成Sentinel，提供限流、熔断、降级等能力 |
| **matrix-mq**      | `matrix-core:matrix-mq` | 集成RocketMQ，提供消息队列功能        |
| **matrix-lock**    | `matrix-core:matrix-lock` | 分布式锁相关功能，支持多种锁实现       |

### 业务功能组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-log**     | `matrix-core:matrix-log` | 公共日志配置，记录操作日志和系统日志      |
| **matrix-job**     | `matrix-core:matrix-job` | 集成XXL-Job，提供分布式任务调度功能     |
| **matrix-oss**     | `matrix-core:matrix-oss` | 对象存储相关功能，支持多种云存储服务     |
| **matrix-sms**     | `matrix-core:matrix-sms` | 短信功能，支持多种短信服务提供商       |
| **matrix-jpush**   | `matrix-core:matrix-jpush` | 集成极光推送，提供推送功能           |
| **matrix-tenant**  | `matrix-core:matrix-tenant` | 多租户组件，支持多租户数据隔离         |
| **matrix-data-permission** | `matrix-core:matrix-data-permission` | 数据权限相关功能，基于注解的数据权限隔离   |

### 开发支持组件

| 组件名称               | 模块路径                    | 用途说明                          |
|--------------------|-------------------------|-------------------------------|
| **matrix-swagger** | `matrix-core:matrix-swagger` | 集成Swagger，提供API文档聚合        |
| **matrix-prometheus** | `matrix-core:matrix-prometheus` | 服务监控相关，集成Prometheus      |
| **matrix-strategy** | `matrix-core:matrix-strategy` | 策略模式组件，提供策略模式的通用实现     |
| **matrix-test**    | `matrix-core:matrix-test` | 测试组件，提供测试相关功能           |


## 🎯业务模块说明

### matrix-system（系统服务）

#### 核心功能

- **路由管理**：基于Nacos的动态路由配置，修改后可实时生效
- **权限管理**：基于RBAC的权限管理，动态配置资源权限
- **用户管理**：提供用户的增删改查、密码重置等功能
- **角色管理**：提供角色的增删改查、权限分配等功能
- **菜单管理**：提供菜单的增删改查、权限配置等功能

### matrix-resource（资源服务）

#### 核心功能

- **OSS服务**：对象存储服务，支持多种云存储提供商
- **SMS服务**：短信发送服务，支持多种短信服务提供商
- **Email服务**：邮件发送服务，支持多种邮件服务器配置

### matrix-gateway（网关服务）

#### 核心功能

- **请求路由**：根据配置将请求路由到相应的服务
- **统一鉴权**：对所有请求进行统一的权限校验
- **熔断限流**：集成Sentinel，提供熔断限流功能
- **灰度发布**：支持基于版本和IP的灰度发布
- **日志收集**：收集所有请求的日志信息

### matrix-admin（管理服务）

#### 核心功能

- **应用监控**：使用Spring Boot Admin监控所有Spring Boot应用
- **健康检查**：实时查看各服务的健康状态
- **性能监控**：监控各服务的性能指标
- **日志查看**：查看各服务的日志信息

## ⏳功能开发进度

| 功能模块         | 状态 | 描述说明                |
|--------------|----|---------------------|
| RBAC权限管理     | ✅  | 基于Sa-Token的角色权限管理     |
| 聚合Swagger文档  | ✅  | 统一管理所有微服务的API文档     |
| 多租户管理       | ✅  | 支持多租户模式，实现数据隔离     |
| 动态路由         | ✅  | 基于Nacos的配置，修改后可实时生效  |
| 灰度发布         | ✅  | 支持基于版本和IP的灰度发布      |
| 分布式事务        | ✅  | 集成Seata实现分布式事务管理     |
| 分布式链路追踪     | ✅  | 使用SkyWalking进行分布式链路追踪 |
| 日志收集分析      | ✅  | 集成ELK进行日志收集和分析      |
| 应用监控         | ✅  | 使用Prometheus和Grafana进行应用监控 |
| 分布式任务调度     | ✅  | 集成XXL-Job实现分布式任务调度   |
| 熔断限流         | ✅  | 集成Sentinel实现熔断限流       |
| 幂等性校验        | ✅  | 防止重复请求             |
| 敏感数据脱敏       | ✅  | 敏感数据自动脱敏           |
| 数据权限控制       | ✅  | 基于注解的数据权限隔离        |
| 分库分表          | ⏳  | 集成Sharding-JDBC实现分库分表    |
| 工作流引擎         | ⏳  | 集成Flowable工作流引擎       |

## 🔔构建自定义组件说明

Matrix-Cloud支持自定义组件接入，您可以按照以下步骤快速构建和接入自定义组件。

### 1. 新建模块

在matrix-cloud项目下新建模块，建议使用以下命名规范：
- 模块名称：`matrix-{module-name}`
- 业务模块内部结构：
  - `{module-name}-api`：对外API接口
  - `{module-name}-biz`：业务逻辑实现

### 2. 添加依赖

在新建模块的`build.gradle`文件中添加核心依赖：

```groovy
dependencies {
    // 基础工具类库
    implementation(project(":matrix-core:matrix-common"))
    // Web组件支持
    implementation(project(":matrix-core:matrix-web"))
    // 可选：服务间调用支持
    implementation(project(":matrix-core:matrix-feign"))
    // 可选：MyBatis支持
    implementation(project(":matrix-core:matrix-mybatis"))
    // 可选：Redis支持
    implementation(project(":matrix-core:matrix-redis"))
    // 可选：权限认证支持
    implementation(project(":matrix-core:matrix-auth"))
}
```

### 3. 配置启动类

在模块中创建启动类，并添加必要的注解：

```java
package com.matrix.yourmodule;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;
import com.matrix.feign.annotation.EnableFeign;

@SpringBootApplication
@EnableDiscoveryClient  // 启用服务发现
@EnableFeign           // 启用Feign客户端（可选）
public class YourModuleApp {
    public static void main(String[] args) {
        SpringApplication.run(YourModuleApp.class, args);
    }
}
```

### 4. 添加配置文件

在`src/main/resources`目录下创建`bootstrap.yml`配置文件：

```yaml
spring:
  application:
    name: matrix-yourmodule  # 服务名称
  profiles:
    active: ${PROFILE:dev}  # 环境配置，对应Nacos的namespace
  config:
    import:
      # 从Nacos导入配置文件
      - optional:nacos:env.properties        # 环境变量
      - optional:nacos:application-common.yml  # 公共配置
      - optional:nacos:datasource.yml         # 数据库配置（如果需要）

# 日志配置
logging:
  # Logstash配置
  stash:
    address: ${LOGSTASH_ADDRESS:localhost:5000}
  # 日志文件路径
  file:
    path: /var/logs/matrix-yourmodule

# Matrix框架配置
matrix:
  # 访问日志配置
  access-log: false
  # Swagger文档配置
  swagger:
    enable: true
    name: 自定义模块API
    version: 1.0.0
    description: 自定义模块的API文档
  # 灰度发布配置
  load-balance:
    gray:
      enabled: true
      defaultVersion: 1.0
  # 多租户配置（可选）
  tenant:
    enable: false
    ignoreTables:
      - sys_config
      - sys_dict
    ignore-urls:
      - /api/public/**
  # 安全配置（可选）
  security:
    captcha:
      validateUrl:
        - /auth/sys/login
```

### 5. 构建与运行

```bash
# 编译模块
./gradlew.bat :matrix-yourmodule:yourmodule-biz:build

# 运行模块
java -jar matrix-yourmodule-yourmodule-biz-3.0.0.jar
```

### 6. 验证接入

启动后，可通过以下方式验证模块是否成功接入：

1. 查看Nacos控制台，确认服务已注册
2. 访问Swagger文档：http://localhost:9000/doc.html，查看是否包含自定义模块的API
3. 调用自定义模块的API，验证功能是否正常

## ⚙️Matrix 配置说明

Matrix框架提供了丰富的配置选项，以下是主要配置项说明：

### 1. 安全配置

```yaml
matrix:
  security:
    # 验证码配置
    captcha:
      validateUrl:          # 需要校验验证码的URL列表
        - /auth/sys/login
      expiration: 300       # 验证码过期时间（秒）
      width: 130            # 验证码图片宽度
      height: 48            # 验证码图片高度
```

### 2. 访问日志配置

```yaml
matrix:
  access-log: true  # 是否记录访问日志
```

### 3. 灰度发布配置

```yaml
matrix:
  load-balance:
    gray:
      enabled: true                  # 是否启用灰度发布
      defaultVersion: 1.0            # 默认兜底版本
      chooser: com.matrix.feign.chooser.ProfileRuleChooser  # 灰度规则选择器
      ips:                           # 支持灰度发布的IP列表
        - 192.168.1.100
        - 192.168.1.101
```

### 4. Swagger文档配置

```yaml
matrix:
  swagger:
    enable: true                     # 是否启用Swagger
    name: Matrix Cloud API           # 文档名称
    version: 3.0.0                   # 文档版本
    description: Matrix Cloud 微服务框架API文档  # 文档描述
    contact:                         # 联系人信息
      name: zwl
      email: zhaoweilong176@gmail.com
    base-package: com.matrix         # 扫描的基础包
```

### 5. 多租户配置

```yaml
matrix:
  tenant:
    enable: true                     # 是否启用多租户
    ignoreTables:                    # 多租户忽略的表名
      - sys_config
      - sys_dict
    ignore-urls:                     # 多租户忽略的接口地址
      - /api/public/**
    column: tenant_id                # 多租户字段名
```

### 6. 限流配置

```yaml
matrix:
  sentinel:
    enable: true                     # 是否启用Sentinel
    flow-rules:                      # 流控规则
      - resource: /api/user/**
        count: 100
        grade: 1
        limitApp: default
```

## 🚀部署指南

### 1. 应用构建

Matrix-Cloud使用Jib插件自动构建Docker镜像，支持将镜像推送到远程仓库。

#### 构建配置

在项目根目录的`build.gradle`文件中，已经配置了Jib插件和镜像仓库信息。您可以根据需要修改镜像仓库地址：

```groovy
// Jib配置示例
jib {
    to {
        image = "registry.cn-hangzhou.aliyuncs.com/matrix-cloud/${project.name}:${version}"
        auth {
            username = "your-username"
            password = "your-password"
        }
    }
}
```

#### 构建命令

```bash
# 构建单个模块的Docker镜像
./gradlew.bat :matrix-system:system-biz:jib

# 构建所有模块的Docker镜像
./gradlew.bat jib

# 构建本地镜像（不推送）
./gradlew.bat :matrix-system:system-biz:jibDockerBuild
```

#### 自定义组件构建

如新增自定义组件，需要在根目录的`build.gradle`文件中添加该模块到构建列表：

```groovy
// 定义需要构建Docker的模块
def javaMicroservices = [
        project(':matrix-gateway'),
        project(':matrix-system:system-biz'),
        project(':matrix-resource:resource-biz'),
        project(':matrix-admin'),
        // 添加自定义组件
        project(':matrix-yourmodule:yourmodule-biz')
]
```

### 2. 中间件部署

Matrix-Cloud提供了完整的中间件部署方案，支持Docker Compose一键部署和手动部署两种方式。

#### Docker Compose一键部署

**环境要求**：
- Docker 20.10.0+
- Docker Compose 2.0.0+

**部署步骤**：

```bash
# 进入部署目录
cd deploy

# 启动所有中间件（开发环境）
docker-compose -f docker-compose.yml up -d

# 启动所有中间件（生产环境）
docker-compose -f docker-matrix.yml up -d

# 启动监控组件
docker-compose -f docker-prometheus.yml up -d
```

**部署验证**：

| 中间件名称       | 访问地址                                  | 用户名/密码                |
|--------------|---------------------------------------|------------------------|
| Nacos        | http://localhost:8848/nacos           | nacos/nacos            |
| Sentinel     | http://localhost:8088/dashboard       | sentinel/sentinel      |
| Seata        | http://localhost:7091/TransactionInfo | seata/seata            |
| SkyWalking   | http://localhost:8080/general         | 无                      |
| Kibana       | http://localhost:5601                  | elastic/changeme       |
| Grafana      | http://localhost:3000                 | admin/admin            |
| XXL-Job Admin | http://localhost:8090/xxl-job-admin   | admin/123456           |

#### 手动部署

详细的手动部署说明请参考：
- [中间件部署文档](./deploy/README.md)
- [Docker部署文档](./deploy/README-docker.md)

### 3. 数据库初始化

**初始化步骤**：

1. 启动MySQL数据库服务
2. 创建数据库：`CREATE DATABASE matrix DEFAULT CHARACTER SET utf8mb4 COLLATE utf8mb4_unicode_ci;
3. 执行SQL脚本：
   ```bash
   # 初始化Nacos配置
   mysql -u root -p matrix < deploy/sql/nacos.sql
   
   # 初始化Seata分布式事务
   mysql -u root -p matrix < deploy/sql/seata.sql
   
   # 初始化业务数据库
   mysql -u root -p matrix < deploy/sql/matrix.sql
   
   # 初始化XXL-Job
   mysql -u root -p matrix < deploy/sql/tables_xxl_job.sql
   ```

### 4. 应用部署

#### 环境变量配置

在部署应用前，需要配置以下环境变量：

| 环境变量名           | 说明                | 默认值                |
|----------------|-------------------|--------------------|
| PROFILE        | 环境配置（dev/prod） | dev                |
| NACOS_ADDR     | Nacos服务地址         | localhost:8848     |
| SEATA_ADDR     | Seata服务地址         | localhost:8091     |
| REDIS_ADDR     | Redis服务地址         | localhost:6379     |
| LOGSTASH_ADDRESS | Logstash地址      | localhost:5000     |

#### Docker部署

```bash
# 启动网关服务
docker run -d --name matrix-gateway \
  -p 9000:9000 \
  -e PROFILE=prod \
  -e NACOS_ADDR=192.168.1.100:8848 \
  registry.cn-hangzhou.aliyuncs.com/matrix-cloud/matrix-gateway:3.0.0

# 启动系统服务
docker run -d --name matrix-system \
  -p 9002:9002 \
  -e PROFILE=prod \
  -e NACOS_ADDR=192.168.1.100:8848 \
  registry.cn-hangzhou.aliyuncs.com/matrix-cloud/matrix-system-system-biz:3.0.0
```

#### Kubernetes部署

Matrix-Cloud支持Kubernetes部署，提供了完整的Kubernetes配置文件，位于`deploy-prod`目录下：

```bash
# 部署到Kubernetes
kubectl apply -f deploy-prod/cluster-ip.yaml
kubectl apply -f deploy-prod/docker-compose.yml
```

### 5. 健康检查

应用启动后，可以通过以下方式进行健康检查：

```bash
# 检查网关服务健康状态
curl http://localhost:9000/actuator/health

# 检查系统服务健康状态
curl http://localhost:9002/actuator/health
```

### 6. 常见问题

#### 服务无法注册到Nacos

- 检查Nacos服务是否正常运行
- 检查应用配置中的Nacos地址是否正确
- 检查网络连接是否正常

#### 分布式事务不生效

- 检查Seata服务是否正常运行
- 检查应用配置中的Seata地址是否正确
- 检查@GlobalTransactional注解是否正确使用

#### 日志无法收集到ELK

- 检查Logstash服务是否正常运行
- 检查应用配置中的Logstash地址是否正确
- 检查Elasticsearch和Kibana服务是否正常运行

## 📊监控与维护

### 1. 应用监控

Matrix-Cloud集成了多种监控工具，提供了完整的监控体系：

| 监控工具       | 用途说明                | 访问地址                                  |
|------------|---------------------|---------------------------------------|
| SkyWalking | 分布式链路追踪             | http://localhost:8080/general         |
| Prometheus | 监控数据收集              | http://localhost:9090                 |
| Grafana    | 监控数据可视化             | http://localhost:3000                 |
| Spring Boot Admin | Spring Boot应用监控     | http://localhost:9001                 |

### 2. 日志管理

#### 日志收集

Matrix-Cloud使用ELK Stack进行日志收集和分析：

1. **Logstash**：收集应用日志，进行过滤和转换
2. **Elasticsearch**：存储日志数据
3. **Kibana**：日志可视化和分析

#### 日志查询

通过Kibana可以方便地查询和分析日志：

1. 访问Kibana：http://localhost:5601
2. 创建索引模式：`matrix-*`
3. 在Discover页面查询日志
4. 在Dashboard页面查看日志统计

### 3. 性能优化

#### 常见优化点

1. **数据库优化**：
   - 添加合适的索引
   - 使用连接池
   - 优化SQL查询

2. **缓存优化**：
   - 合理使用Redis缓存
   - 设置合适的缓存过期时间
   - 避免缓存穿透和缓存雪崩

3. **服务优化**：
   - 调整JVM参数
   - 优化线程池配置
   - 使用异步处理

4. **网关优化**：
   - 调整路由规则
   - 优化限流配置
   - 启用缓存

## 📄文档说明

### 1. 开发文档

- [快速开始指南](#快速开始)
- [构建自定义组件说明](#构建自定义组件说明)
- [Matrix配置说明](#matrix-配置说明)

### 2. 部署文档

- [Docker部署文档](./deploy/README-docker.md)
- [中间件部署文档](./deploy/README.md)

### 3. API文档

- **Swagger文档**：http://localhost:9000/doc.html
- **接口文档**：可通过Swagger UI在线查看和测试API

## 🤝贡献指南

欢迎大家参与Matrix-Cloud的开发和维护！

### 1. 贡献流程

1. Fork本仓库
2. 创建特性分支：`git checkout -b feature/your-feature`
3. 提交更改：`git commit -am 'Add some feature'`
4. 推送到分支：`git push origin feature/your-feature`
5. 提交Pull Request

### 2. 代码规范

- 遵循Java代码规范
- 使用Lombok简化代码
- 编写单元测试
- 添加必要的注释

### 3. 版本管理

Matrix-Cloud使用语义化版本控制：

- **主版本号**：功能有重大变更
- **次版本号**：添加新功能
- **修订号**：修复bug

## 📄许可证

Matrix-Cloud采用Apache License 2.0开源许可证。

## 📞联系方式

- **作者**：zhaoWeiLong
- **邮箱**：zhaoweilong176@gmail.com
- **GitHub**：https://github.com/zhaoweilong176/matrix-cloud

## 🙏致谢

感谢所有为Matrix-Cloud做出贡献的开发者和用户！

---

**Matrix-Cloud** - 让微服务开发更简单！
