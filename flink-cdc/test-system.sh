#!/bin/bash
# Flink CDC 功能测试脚本
# 验证系统各项功能是否正常工作

set -e

GREEN='\033[0;32m'
RED='\033[0;31m'
YELLOW='\033[1;33m'
NC='\033[0m'

echo "========================================"
echo "Flink CDC 功能测试"
echo "========================================"
echo ""

# 测试计数器
PASSED=0
FAILED=0

# 测试函数
test_endpoint() {
    local name=$1
    local url=$2
    local expected=$3
    
    echo -n "测试: $name ... "
    
    response=$(curl -s -o /dev/null -w "%{http_code}" "$url")
    
    if [ "$response" = "$expected" ]; then
        echo -e "${GREEN}✓ PASSED${NC}"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected, Got: $response)"
        FAILED=$((FAILED + 1))
    fi
}

test_json_response() {
    local name=$1
    local url=$2
    local field=$3
    local expected=$4
    
    echo -n "测试: $name ... "
    
    response=$(curl -s -L "$url" | jq -r "$field")
    
    if [ "$response" = "$expected" ]; then
        echo -e "${GREEN}✓ PASSED${NC}"
        PASSED=$((PASSED + 1))
    else
        echo -e "${RED}✗ FAILED${NC} (Expected: $expected, Got: $response)"
        FAILED=$((FAILED + 1))
    fi
}

echo ">>> 基础服务测试"
echo ""

# 测试Flink Web UI
test_endpoint "Flink Web UI" "http://localhost:8081/" "200"

# 测试CDC Manager
test_endpoint "CDC Manager Health" "http://localhost:8888/health" "200"

# 测试CDC Manager API
test_json_response "CDC Manager Status" "http://localhost:8888/api/system/status" ".code" "0"

echo ""
echo ">>> 数据库连接测试"
echo ""

# 测试PostgreSQL连接
test_json_response "PostgreSQL连接" "http://localhost:8888/api/system/status" ".data.postgres.connected" "true"

# 测试StarRocks连接
test_json_response "StarRocks连接" "http://localhost:8888/api/system/status" ".data.starrocks.connected" "true"

echo ""
echo ">>> API功能测试"
echo ""

# 测试表列表API
test_json_response "获取表列表" "http://localhost:8888/api/tables" ".code" "0"

# 测试Flink状态API
test_json_response "Flink集群状态" "http://localhost:8888/api/system/flink" ".code" "0"

echo ""
echo ">>> 数据验证测试"
echo ""

# 检查PostgreSQL测试数据
echo -n "测试: PostgreSQL测试数据 ... "
pg_count=$(docker exec flink-cdc-postgres psql -U postgres -d cdc_db -t -c "SELECT COUNT(*) FROM users;" 2>/dev/null | tr -d ' ')
if [ "$pg_count" -gt 0 ]; then
    echo -e "${GREEN}✓ PASSED${NC} (Found $pg_count rows)"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗ FAILED${NC} (No data found)"
    FAILED=$((FAILED + 1))
fi

echo ""
echo ">>> 配置文件测试"
echo ""

# 检查Pipeline配置文件
echo -n "测试: Pipeline配置文件 ... "
if [ -f "cdc-pipelines/pg-to-sr-pipeline.yaml" ]; then
    echo -e "${GREEN}✓ PASSED${NC}"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗ FAILED${NC}"
    FAILED=$((FAILED + 1))
fi

# 检查管理配置文件
echo -n "测试: 管理配置文件 ... "
if [ -f "cdc-pipelines/pipeline-config.yaml" ]; then
    echo -e "${GREEN}✓ PASSED${NC}"
    PASSED=$((PASSED + 1))
else
    echo -e "${RED}✗ FAILED${NC}"
    FAILED=$((FAILED + 1))
fi

echo ""
echo "========================================"
echo "测试结果汇总"
echo "========================================"
echo -e "通过: ${GREEN}$PASSED${NC}"
echo -e "失败: ${RED}$FAILED${NC}"
echo "总计: $((PASSED + FAILED))"
echo ""

if [ $FAILED -eq 0 ]; then
    echo -e "${GREEN}所有测试通过！系统运行正常。${NC}"
    echo ""
    echo "下一步操作："
    echo "  1. 访问 Web UI: http://localhost:8888/ui"
    echo "  2. 启用表同步: curl -X POST http://localhost:8888/api/tables/users/enable"
    echo "  3. 生成Pipeline: curl -X POST http://localhost:8888/api/pipeline/generate"
    echo "  4. 提交作业: curl -X POST http://localhost:8888/api/pipeline/submit -d '{\"pipeline_file\":\"generated-pipeline.yaml\"}'"
    exit 0
else
    echo -e "${RED}部分测试失败，请检查日志。${NC}"
    echo ""
    echo "查看日志命令："
    echo "  docker-compose -f docker-compose-new.yml logs -f"
    exit 1
fi
