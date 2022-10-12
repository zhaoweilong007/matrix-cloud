# Spring Cloud Matrix

## ✨介绍

Spring cloud matrix是微服务的脚手架，整合目前主流的微服务框架

### 🔨项目环境

开发环境和相应中间件版本

- jdk11
- gradle7.4.2
- mysql8.0
- redis5.0
- elasticSearch7.10.0

### 📝技术选型

| 技术框架                 | 描述                   | 版本           |
|----------------------|----------------------|--------------|
| spring cloud alibaba | spring cloud alibaba | 2021.0.4.0   |
| spring cloud         | spring cloud         | 2021.0.4     |
| spring boot          | spring boot          | 2.6.11       |
| nacos                | 服务注册发现               | 2.1.0        |
| spring cloud gateway | 网关                   | 3.1.4        |
| sentinel             | 熔断限流                 | 1.8.5        |
| sa-Token             | 权限认证                 | 1.30.0       |
| seata                | 分布式事务                | 1.5.2        |
| rocketmq             | 消息队列                 | 4.9.4        |
| skywalking           | 分布式链路追踪              | 9.1.0        |
| ELK                  | 日志处理分析               | 8.0          |
| prometheus+grafana   | 应用监控                 | 2.36.2+9.0.1 |

后续会逐步整合相关框架，搭建一个分布式系统架构.....

## 📌模块

| 模块 | 描述   | 服务地址                            |
|----|------|---------------------------------|
|gateway| 网关   | http://localhost:9000           |
|system-server| 系统服务 | http://localhost:9001           |
|doc| 文档服务 | http://localhost:10000/doc.html |

## ⏳功能开发进度

- [x] RBAC权限管理
- [x] 聚合swagger文档
- [x] 多租户管理
- [x] 动态路由
- [x] 集成seata分布式事务
- [x] 集成skyWalking分布式链路追踪
- [x] 集成jib构建docker
- [ ] 集成prometheus监控
- [ ] 集成ELK日收集
- [ ] 集成rocketmq消息队列
- [ ] 集成sharding-jdbc分库分表
- [ ] 集成工作流flowable

## Common组件

- common-core

> 公共组件，提供基础的工具类和通用的工具类

> 集成nacos服务注册发现，配置中心

> 在SpringBoot 2.4.x的版本之后，对于bootstrap.properties/bootstrap.yaml配置文件 的支持

> 集成openfeign

> Spring Cloud 2020版本以后，默认移除了对Netflix的依赖，其中就包括Ribbon，官方默认推荐使用Spring Cloud
> Loadbalancer正式替换Ribbon，并成为了Spring Cloud负载均衡器的唯一实现

> 集成sentinel的支持

> 集成knife4j的支持

- matrix-core

> 对common-core提供的的自动配置

- matrix-web

> 提供对servlet服务的支持及相关配置

## system-server

### 路由管理

gateway使用alibaba sentinel集成，支持nacos动态路由配置

> ps:在gateway网关集成sentinel时，需要添加JVM参数`-Dcsp.sentinel.app.type=1`,将应用识别为网关，否则看不到api管理页面

通过nacos监听配置，使用`RouteDefinitionWriter`更新网关路由配置，实现动态路由配置

### 权限管理

- sa-token框架集成，网关统一鉴权，内部服务外网隔离
- 基于RBAC的权限管理，动态配置资源权限

## 🔔构建自定义组件说明

如需自定义组件，可按以下步骤自动接入spring cloud matrix服务

- 在spring-cloud-matrix下新建模块
- 添加以下核心配置依赖

```groovy
dependencies {
    implementation(project(":common:matrix-core"))
    implementation(project(":common:matrix-web"))
}
```

matrix-core自动集成
matrix-web对servlet的配置（目前只支持servlet）

- 在启动类上加上`@EnableMatrix`注解
- 新建bootstrap.yml配置文件，配置如下

```yaml
spring:
  application:
    name: youAppName
  profiles:
    include: matrix #包含了基础配置，可在matrix-core下application-matrix.yml查看
    active: dev #指定当前环境
  cloud:
    # nacos配置，可通过环境变量指定
    nacos:
      server-addr: ${NACOS_SERVER_ADDRESS:localhost:8848}
      username: ${NACOS_USERNAME:nacos}
      password: ${NACOS_PASSWD:nacos}
      config:
        file-extension: yaml
        shared-configs:
          - data-id: application-common #包含的公共配置，在/config/application-common.yaml查看
            refresh: true
logging:
  level:
    com.matrix.mapper: debug
    com.matrix.api.**: debug
  file:
    path: /var/logs
```

> 以上步骤完成可正常启动应用，自动接入所有微服务功能

## 部署

### 应用构建

所有应用使用jib自动构建docker镜像

项目使用阿里云镜像仓库，可以修改为其他仓库，在[build.gradle](/build.gradle)下修改

构建本地镜像并推送到远程仓库

```shell
./gradlew.bat jib
```

如新增自定义组件，还需在[build.gradle](/build.gradle)下以下位置增加一行配置，让该模块包含jib的配置

```groovy
//定义需要构建docker的模块
def javaMicroservices = [
        project(':gateway'),
        project(':system:system-biz'),
        project(':doc'),
        //添加自定义组件
        project(':youModuleName')
]
```

### 中间件

相关中间件的部署可以查看[deploy文档](/deploy/README.md)

提供docker-compose配置文件一键部署所有中间件，[docker-compose.yml](/deploy/docker-compose.yml)

> ps: sql文件需要自行导入，deploy下[nacos.sql](/deploy/sql/nacos.sql)和[matrix.sql](/deploy/sql/matrix.sql)