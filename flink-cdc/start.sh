#!/bin/bash

# 启动所有容器
echo "启动 docker-compose 环境..."
docker-compose up -d

# 等待服务启动
echo "等待服务启动中..."
sleep 10

# 检查服务状态
echo ""
echo "======== 服务状态 ========"
curl -s http://localhost:8888/health | jq .
echo ""
echo "PostgreSQL: localhost:5432"
echo "StarRocks FE: localhost:8030"
echo "Flink UI: http://localhost:8081"
echo "API 服务: http://localhost:8888"
echo ""
echo "======== 快速开始 ========"
echo "1. 查看可同步的表:"
echo "   curl http://localhost:8888/api/tables | jq ."
echo ""
echo "2. 启用表同步 (示例: users 表):"
echo "   curl -X POST http://localhost:8888/api/tables/users/enable"
echo ""
echo "3. 查看同步配置:"
echo "   curl http://localhost:8888/api/sync-config | jq ."
echo ""
echo "4. 查看系统状态:"
echo "   curl http://localhost:8888/api/status | jq ."
