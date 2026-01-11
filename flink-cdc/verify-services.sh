#!/bin/bash

# 验证所有服务是否正常运行

echo "====== 验证 Flink CDC 服务状态 ======"
echo ""

# PostgreSQL
echo "1. PostgreSQL (端口 5432):"
if docker compose exec -T postgres pg_isready >/dev/null 2>&1; then
  echo "   ✓ 正常运行"
else
  echo "   ✗ 无法连接"
fi

# StarRocks
echo "2. StarRocks (HTTP 端口 8030):"
if curl -s http://localhost:8030/api/bootstrap | grep -q "replayedJournalId"; then
  echo "   ✓ 正常运行"
else
  echo "   ✗ 无法连接"
fi

# Flink JobManager
echo "3. Flink JobManager (HTTP 端口 8081):"
if curl -s http://localhost:8081/ | grep -q "Apache Flink"; then
  echo "   ✓ 正常运行"
else
  echo "   ✗ 无法连接"
fi

# API Server
echo "4. API Server (HTTP 端口 8888):"
if curl -s http://localhost:8888/health >/dev/null 2>&1; then
  echo "   ✓ 正常运行"
else
  echo "   ✗ 无法连接"
fi

echo ""
echo "====== 服务访问地址 ======"
echo "Flink Dashboard: http://localhost:8081"
echo "StarRocks UI:    http://localhost:8030"
echo "API Server:      http://localhost:8888"
echo "PostgreSQL:      localhost:5432"
echo ""
echo "====== 容器运行状态 ======"
docker compose ps
