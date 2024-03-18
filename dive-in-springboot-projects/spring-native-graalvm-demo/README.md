# Started
1. jdk17
2. springboot 3.0+

# macos install graalvm-jdk

1. 下载: https://github.com/graalvm/graalvm-ce-builds/releases
2. 解压缩和安装

```shell
sudo tar -xzf graalvm-community-jdk-17.0.9_macos-x64_bin.tar.gz
sudo mv graalvm-community-jdk-17.0.9_macos-x64_bin /Library/Java/JavaVirtualMachines
```

3. 指定环境变量, `.zshrc`
```shell
export PATH=/Library/Java/JavaVirtualMachines/graalvm-community-openjdk-17.0.9+9.1/Contents/Home/bin:$PATH
export JAVA_HOME=/Library/Java/JavaVirtualMachines/graalvm-community-openjdk-17.0.9+9.1/Contents/Home
```

4. 查看效果
```shell
(base) ➜  ~ java -version
openjdk version "17.0.9" 2023-10-17
OpenJDK Runtime Environment GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22)
OpenJDK 64-Bit Server VM GraalVM CE 17.0.9+9.1 (build 17.0.9+9-jvmci-23.0-b22, mixed mode, sharing)

(base) ➜  ~ gu --version
GraalVM Updater 23.0.2
```

# 打包
1. 打包
```shell
mvn -Pnative native:compile
```

2. 查看效果  
```shell
(base) ➜  spring-native-graalvm-demo ll target
total 186352
-rwxr-xr-x@ 1 imyzt  staff    72M spring-native-graalvm-demo

# 运行
(base) ➜  spring-native-graalvm-demo ./target/spring-native-graalvm-demo
Started SpringNativeGraalvmDemoApplication in 0.187 seconds (process running for 0.201)
```

3. 访问 `curl lcoalhost:8081`

