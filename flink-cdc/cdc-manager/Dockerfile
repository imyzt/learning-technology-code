FROM python:3.11-slim

WORKDIR /app

# 安装系统依赖
RUN apt-get update && apt-get install -y \
    curl \
    postgresql-client \
    default-mysql-client \
    && rm -rf /var/lib/apt/lists/*

# 复制requirements
COPY requirements.txt .

# 安装Python依赖
RUN pip install --no-cache-dir -r requirements.txt

# 复制应用代码
COPY app /app

# 创建必要目录
RUN mkdir -p /var/log/cdc-manager /opt/cdc-pipelines

# 暴露端口
EXPOSE 8888

# 健康检查
HEALTHCHECK --interval=30s --timeout=3s --start-period=40s --retries=3 \
    CMD curl -f http://localhost:8888/health || exit 1

# 启动应用
CMD ["python", "main.py"]
