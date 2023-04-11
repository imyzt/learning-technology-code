# SpringBoot3.0

将SpringBoot应用打包成可执行文件,启动速度快,但包体积变大(包含jre).

## 环境要求
- Java17+
- SpringBoot3.0+
- graalvm


## 基本配置
1. 安装graalvm, 配置环境变量
2. Maven配置, 安装 `native-maven-plugin` 插件
3. 执行打包脚本 `mvn clean package -Dnative`
4. 启动后, 访问 `curl localhost:8080/index`
