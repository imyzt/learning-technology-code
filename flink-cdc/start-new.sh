#!/bin/bash
# Flink CDC 项目启动脚本
# 完整启动和验证所有服务

set -e

echo "========================================"
echo "Flink CDC 数据同步平台启动脚本"
echo "========================================"
echo ""

# 颜色输出
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
NC='\033[0m' # No Color

# 检查docker和docker-compose
echo "检查环境..."
if ! command -v docker &> /dev/null; then
    echo -e "${RED}Error: Docker未安装${NC}"
    exit 1
fi

if ! command -v docker-compose &> /dev/null; then
    echo -e "${RED}Error: Docker Compose未安装${NC}"
    exit 1
fi

echo -e "${GREEN}✓${NC} Docker环境正常"
echo ""

# 停止已有容器
echo "清理旧容器..."
docker-compose -f docker-compose-new.yml down 2>/dev/null || true
echo -e "${GREEN}✓${NC} 旧容器已清理"
echo ""

# 创建必要的目录
echo "创建必要目录..."
mkdir -p logs/flink logs/manager cdc-pipelines init-scripts web-ui
echo -e "${GREEN}✓${NC} 目录创建完成"
echo ""

# 启动服务
echo "启动服务..."
echo "这可能需要几分钟时间，请耐心等待..."
echo ""

docker-compose -f docker-compose-new.yml up -d

echo ""
echo "等待服务启动..."
echo ""

# 等待PostgreSQL
echo -n "等待 PostgreSQL... "
max_attempts=30
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if docker exec flink-cdc-postgres pg_isready -U postgres >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC}"
        break
    fi
    sleep 2
    attempt=$((attempt + 1))
    echo -n "."
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "${RED}✗${NC} 超时"
    exit 1
fi

# 等待StarRocks
echo -n "等待 StarRocks... "
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8030/api/bootstrap >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC}"
        break
    fi
    sleep 3
    attempt=$((attempt + 1))
    echo -n "."
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "${RED}✗${NC} 超时"
    exit 1
fi

# 等待Flink
echo -n "等待 Flink CDC Runtime... "
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8081/ >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC}"
        break
    fi
    sleep 2
    attempt=$((attempt + 1))
    echo -n "."
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "${RED}✗${NC} 超时"
    exit 1
fi

# 等待CDC Manager
echo -n "等待 CDC Manager... "
attempt=0
while [ $attempt -lt $max_attempts ]; do
    if curl -s http://localhost:8888/health >/dev/null 2>&1; then
        echo -e "${GREEN}✓${NC}"
        break
    fi
    sleep 2
    attempt=$((attempt + 1))
    echo -n "."
done

if [ $attempt -eq $max_attempts ]; then
    echo -e "${RED}✗${NC} 超时"
    exit 1
fi

echo ""
echo "========================================"
echo -e "${GREEN}所有服务启动成功！${NC}"
echo "========================================"
echo ""
echo "服务访问地址："
echo "  ● Flink Web UI:    http://localhost:8081"
echo "  ● CDC Manager UI:  http://localhost:8888/ui"
echo "  ● CDC Manager API: http://localhost:8888/api"
echo "  ● StarRocks FE:    http://localhost:8030"
echo ""
echo "数据库连接："
echo "  ● PostgreSQL:  localhost:5432 (postgres/postgres)"
echo "  ● StarRocks:   localhost:9030 (root/)"
echo ""
echo "快速测试："
echo "  查看系统状态:"
echo "    curl http://localhost:8888/api/system/status | jq"
echo ""
echo "  查看所有表:"
echo "    curl http://localhost:8888/api/tables | jq"
echo ""
echo "  启用表同步:"
echo "    curl -X POST http://localhost:8888/api/tables/users/enable"
echo ""
echo "查看日志："
echo "  docker-compose -f docker-compose-new.yml logs -f [service-name]"
echo ""
echo "停止服务："
echo "  docker-compose -f docker-compose-new.yml down"
echo ""
echo "========================================"
