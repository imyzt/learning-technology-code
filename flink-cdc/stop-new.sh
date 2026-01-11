#!/bin/bash
# 停止并清理所有服务

set -e

echo "停止 Flink CDC 服务..."

# 停止容器
docker-compose -f docker-compose-new.yml down

# 询问是否删除数据卷
read -p "是否删除所有数据卷？这将清除所有数据 (y/N): " -n 1 -r
echo
if [[ $REPLY =~ ^[Yy]$ ]]; then
    echo "删除数据卷..."
    docker volume rm flink-cdc-postgres-data flink-cdc-starrocks-fe flink-cdc-starrocks-be flink-cdc-checkpoints flink-cdc-savepoints 2>/dev/null || true
    echo "数据卷已删除"
fi

echo "服务已停止"
