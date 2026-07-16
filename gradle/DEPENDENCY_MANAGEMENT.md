# 依赖管理

直接依赖与插件版本只在 `gradle/libs.versions.toml` 声明；`matrix-bom` 将项目约束及外部 BOM 聚合为 Gradle Platform。业务模块不得新增带版本的第三方 GAV。

## 新增或升级依赖

1. 在版本目录新增或更新版本与库别名，并在 `matrix-bom` 增加项目自主管理依赖的约束。
2. 执行 `./gradlew dependencies --write-locks --write-verification-metadata sha256 --no-daemon`。
3. 执行 `./gradlew test --no-daemon`，提交版本目录、平台、所有受影响锁文件及 `gradle/verification-metadata.xml`。

## 本地仓库

默认构建不会使用本地 Maven 仓库。仅调试未发布制品时使用 `./gradlew -PuseMavenLocal=true <task>`；不得用该开关生成或更新锁文件与校验元数据。
