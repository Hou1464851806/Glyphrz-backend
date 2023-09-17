# `glyphz-backend`

「[字塑 / Glyphz](https://github.com/s-hit/glyphz)」的后端部分，用于「探索」功能及字体、设置项的云同步。

## 环境

```sh
brew install java
brew install gradle
brew install mysql
```

## 构建与运行

在 `/src/main/resources/application.properties` 内配置数据库后，使用以下指令运行。

```sh
gradle build && java -jar ./build/libs/glyphz-0.0.1.jar
```
