# Flink CDC 3.5 Runtime
# 包含所有必需的CDC Connector和依赖

FROM flink:1.18.1-scala_2.12-java11

# 设置工作目录
WORKDIR /opt/flink

# 安装必要工具
USER root
RUN apt-get update && \
    apt-get install -y curl wget unzip mysql-client postgresql-client && \
    rm -rf /var/lib/apt/lists/*

# Flink CDC版本配置
ENV FLINK_CDC_VERSION=3.5.0
ENV FLINK_VERSION=1.18

# 下载并解压Flink CDC完整发行版
# 从GitHub Release下载（包含所有必需的依赖）
RUN cd /tmp && \
    echo "Downloading Flink CDC ${FLINK_CDC_VERSION}..." && \
    wget https://github.com/apache/flink-cdc/releases/download/release-${FLINK_CDC_VERSION}/flink-cdc-${FLINK_CDC_VERSION}-bin.tar.gz && \
    tar -xzf flink-cdc-${FLINK_CDC_VERSION}-bin.tar.gz && \
    mv flink-cdc-${FLINK_CDC_VERSION} /opt/flink-cdc && \
    rm flink-cdc-${FLINK_CDC_VERSION}-bin.tar.gz && \
    echo "Flink CDC installed successfully"

# 下载Pipeline Connectors
# 注意：如果flink-cdc-bin中已包含connector，这步会失败但不影响使用
# PostgreSQL Connector
RUN cd /opt/flink-cdc/lib && \
    wget -q https://repo1.maven.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-postgres/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-postgres-${FLINK_CDC_VERSION}.jar 2>/dev/null || \
    echo "PostgreSQL connector may already be included in flink-cdc-bin"

# StarRocks Connector  
RUN cd /opt/flink-cdc/lib && \
    wget -q https://repo1.maven.org/maven2/org/apache/flink/flink-cdc-pipeline-connector-starrocks/${FLINK_CDC_VERSION}/flink-cdc-pipeline-connector-starrocks-${FLINK_CDC_VERSION}.jar 2>/dev/null || \
    echo "StarRocks connector may already be included in flink-cdc-bin"

# 创建必要目录
RUN mkdir -p /opt/cdc-pipelines /tmp/flink-checkpoints /tmp/flink-savepoints /opt/scripts

# 复制Flink配置
COPY flink-conf.yaml /opt/flink/conf/flink-conf.yaml

# 设置环境变量
ENV FLINK_CDC_HOME=/opt/flink-cdc
ENV PATH=$PATH:$FLINK_CDC_HOME/bin

# 赋予flink用户权限
RUN chown -R flink:flink /opt/flink /opt/flink-cdc /opt/cdc-pipelines /tmp/flink-checkpoints /tmp/flink-savepoints /opt/scripts

# 切换到flink用户
USER flink

# 暴露端口
EXPOSE 8081 6123 6124

# 默认启动命令（在docker-compose中覆盖）
CMD ["/opt/flink/bin/start-cluster.sh", "&&", "tail", "-f", "/opt/flink/log/flink-*.log"]
