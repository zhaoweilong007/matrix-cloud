# Spring Cloud Matrix

## 介绍

Spring cloud matrix是微服务的脚手架，整合目前主流的微服务框架

### 项目环境

开发环境和相应中间件版本

- jdk11
- gradle7.4.2
- mysql8.0
- redis5.0
- elasticSearch8.0

### 技术选型

| 技术框架                 | 描述                   | 版本           |
|----------------------|----------------------|--------------|
| spring cloud alibaba | spring cloud alibaba | 2021.0.1.0   |
| spring cloud         | spring cloud         | 2021.0.1     |
| spring boot          | spring boot          | 2.6.3        |
| nacos                | 服务注册发现               | 2.1.0        |
| spring cloud gateway | 网关                   | 3.1.1        |
| sentinel             | 熔断限流                 | 1.8.4        |
| sa-Token             | 权限认证                 | 1.30.0       |
| seata                | 分布式事务                | 1.5.1        |
| rocketmq             | 消息队列                 | 4.9.4        |
| skywalking           | 分布式链路追踪              | 9.1.0        |
| ELK                  | 日志处理分析               | 8.0          |
| prometheus+grafana    | 应用监控                 | 2.36.2+9.0.1 |

后续会逐步整合相关框架，搭建一个分布式系统架构.....

## 模块

| 模块 | 描述   | 服务地址                            |
|----|------|---------------------------------|
|gateway| 网关   | http://localhost:9000           |
|system-server| 系统服务 | http://localhost:9001           |
|doc| 文档服务 | http://localhost:10000/doc.html |

## 功能开发进度

- [x] RBAC权限管理
- [x] 聚合swagger文档
- [x] 多租户管理
- [x] 动态路由
- [x] 集成seata分布式事务
- [ ] 集成skyWalking分布式链路追踪
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

## 部署

相关中间件的部署可以查看[deploy文档](/deploy/README.md)