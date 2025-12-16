# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

**Matrix-Cloud** is a comprehensive microservices scaffolding framework built with Java 17, Spring Boot 3.1.10, Spring Cloud 2022.0.4, and Spring Cloud Alibaba 2022.0.0.0. It provides a complete ecosystem for building cloud-native microservices applications.

## Build System

### Gradle Commands
- **Build all modules**: `./gradlew.bat build` (Windows) or `./gradlew build` (Unix)
- **Build specific module**: `./gradlew.bat :module-name:build`
- **Clean build**: `./gradlew.bat clean build`
- **Run tests**: `./gradlew.bat test` (Note: tests are disabled by default in build.gradle:92)
- **Build Docker images**: `./gradlew.bat jib` - Builds and pushes Docker images using Jib plugin
- **SonarQube analysis**: `./gradlew.bat sonar` - Requires SonarQube server configuration in build.gradle

### Key Build Files
- `build.gradle` - Root build configuration
- `settings.gradle` - Module definitions (35+ modules)
- `version.gradle` - Centralized dependency version management
- `deploy.gradle` - Jib Docker build configuration for microservices
- `gradle.properties` - Gradle properties including `GRADLE_VERSION=8.2`

## Architecture

### Core Structure
```
matrix-cloud/
├── matrix-core/          # Core components (35+ modules)
│   ├── matrix-common/    # Common utilities and tools
│   ├── matrix-web/       # Web/Servlet support
│   ├── matrix-auth/      # Authentication (Sa-Token 1.37.0)
│   ├── matrix-feign/     # OpenFeign with version-based load balancing
│   ├── matrix-config/    # Nacos configuration management
│   ├── matrix-mybatis/   # MyBatis Plus 3.5.5 integration
│   ├── matrix-redis/     # Redis operations with Redisson 3.24.3
│   ├── matrix-seata/     # Seata 1.7.1 distributed transactions
│   ├── matrix-tenant/    # Multi-tenancy support
│   ├── matrix-swagger/   # Swagger/OpenAPI documentation
│   ├── matrix-job/       # XXL-Job distributed scheduling
│   ├── matrix-mq/        # RocketMQ 4.9.4 integration
│   └── ... 28+ more modules
├── matrix-bom/           # Dependency version management (Bill of Materials)
├── matrix-admin/         # Spring Boot Admin monitoring (port 9001)
├── matrix-gateway/       # API Gateway (port 9000)
├── matrix-resource/      # Resource service (OSS, SMS, Email - port 9003)
└── config/               # Configuration files
```

### Service Architecture
- **Service Discovery**: Nacos 2.2.1 (port 8848)
- **API Gateway**: Spring Cloud Gateway (port 9000)
- **Authentication**: Sa-Token with gateway unified authentication
- **Distributed Tracing**: SkyWalking 9.2.0 agent integrated via Jib
- **Monitoring**: Prometheus + Grafana + Spring Boot Admin
- **Logging**: ELK Stack (Elasticsearch 7.17.6, Logstash, Kibana)
- **Message Queue**: RocketMQ 4.9.4
- **Distributed Scheduling**: XXL-Job (port 8090)
- **Circuit Breaking**: Sentinel 1.8.5 (port 8088)

## Development Workflow

### Adding a New Microservice
1. Create module in `settings.gradle`
2. Add dependencies in module's `build.gradle`:
   ```groovy
   dependencies {
       implementation(project(":matrix-core:matrix-common"))
       implementation(project(":matrix-core:matrix-web"))
   }
   ```
3. Add `@EnableMatrix` annotation to main class
4. Create `bootstrap.yml` with service name and Nacos configuration
5. Add module to `deploy.gradle` `microservices` list for Docker builds

### Configuration Management
- **Primary config**: `config/dev/` directory
- **Nacos configs**: `config/nacos/` for Nacos configuration files
- **Environment variables**: Set `PROFILE` for environment (dev/prod)
- **Database initialization**: SQL scripts in `deploy/sql/`

### Docker Deployment
- **Middleware**: Use `deploy/docker-compose.yml` for one-click deployment
- **Application services**: Defined in `deploy/docker-matrix.yml`
- **Build images**: `./gradlew.bat jib` builds and pushes to configured registry
- **SkyWalking agent**: Automatically included in Docker images (except admin)

## Key Configuration Patterns

### Bootstrap Configuration
```yaml
spring:
  application:
    name: service-name
  profiles:
    active: ${PROFILE:}  # Environment (maps to Nacos namespace)
  config:
    import:
      - optional:nacos:env.properties
      - optional:nacos:application-common.yml
      - optional:nacos:datasource.yml
```

### Matrix Framework Configuration
```yaml
matrix:
  security:
    captcha:
      validateUrl:  # URLs requiring captcha validation
  access-log: false  # Enable access logging
  load-balance:
    gray:
      enabled: true  # Gray release load balancing
      defaultVersion: 1.0
  swagger:
    enable: false  # Enable Swagger documentation
  tenant:
    enable: false  # Enable multi-tenancy
```

## Service Ports and Access

| Service | Port | URL | Credentials |
|---------|------|-----|-------------|
| Nacos | 8848 | http://localhost:8848/nacos | nacos/nacos |
| Sentinel | 8088 | http://localhost:8088/dashboard | sentinel/sentinel |
| Seata | 7091 | http://localhost:7091/TransactionInfo | seata/seata |
| SkyWalking | 8080 | http://localhost:8080/general | - |
| Elasticsearch | 9200 | http://localhost:9200 | elastic/changeme |
| Kibana | 5601 | http://localhost:5601 | elastic/changeme |
| Prometheus | 9090 | http://localhost:9090 | - |
| Grafana | 3000 | http://localhost:3000 | admin/admin |
| XXL-Job | 8090 | http://localhost:8090/xxl-job-admin | admin/123456 |
| RocketMQ Console | 19876 | http://localhost:19876/ | - |
| Gateway | 9000 | http://localhost:9000 | - |
| Spring Boot Admin | 9001 | http://localhost:9001 | admin/admin |
| System Service | 9002 | http://localhost:9002 | - |
| Resource Service | 9003 | http://localhost:9003 | - |

## Testing Notes
- Tests are disabled by default (`test.enabled = false` in build.gradle)
- To enable tests, modify `build.gradle:92` or use `./gradlew.bat test --tests "*TestClass"`
- Test utilities available in `matrix-core/matrix-test` module

## Code Generation
- **EasyCode templates**: Located in `EasyCode/` directory
- **MyBatis Plus code generation**: Configured in `matrix-core/matrix-mybatis`

## Important Dependencies
- **Centralized versions**: Managed in `matrix-bom` module
- **Spring Cloud Alibaba**: 2022.0.0.0 (compatible with Spring Boot 3.1.10)
- **Database**: MySQL + MyBatis Plus 3.5.5
- **Cache**: Redis + Redisson 3.24.3
- **Authentication**: Sa-Token 1.37.0
- **Object Mapping**: MapStruct Plus 1.3.5
