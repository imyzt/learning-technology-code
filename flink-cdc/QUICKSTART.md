# 快速开始指南

## 🎯 5分钟快速上手

### 前置条件
- Docker & Docker Compose
- macOS/Linux/Windows (with WSL2)

### 步骤 1: 启动环境

```bash
# 进入项目目录
cd /Users/imyzt/dev/ideaWorkspace/learning-technology-code/flink-cdc

# 给启动脚本添加执行权限
chmod +x start.sh

# 启动所有容器（约 1-2 分钟）
./start.sh
```

### 步骤 2: 验证服务

等待脚本执行完成后，检查各个服务：

```bash
# 检查 PostgreSQL
curl localhost:5432
# 应该返回: ERROR or connection refused（这是正常的，表示 PostgreSQL 在监听）

# 检查 Flink
curl http://localhost:8081/api/overview | jq .
# 应该返回 Flink 集群信息

# 检查 API 服务
curl http://localhost:8888/api/status | jq .
# 应该返回系统状态
```

### 步骤 3: 打开管理界面

在浏览器中打开：
```
file:///Users/imyzt/dev/ideaWorkspace/learning-technology-code/flink-cdc/dashboard.html
```

或使用 http 方式（需要简单的 HTTP 服务器）：
```bash
# 在项目目录启动一个简单的 HTTP 服务器
cd /Users/imyzt/dev/ideaWorkspace/learning-technology-code/flink-cdc
python3 -m http.server 8000

# 然后访问
http://localhost:8000/dashboard.html
```

## 📚 核心概念

### 1. 表自动发现机制

当您访问 API 时，系统会：
1. **扫描** PostgreSQL 中的所有表
2. **对比** sync-tables.json 中的配置
3. **自动添加** 新表到配置中
4. **展示**在表管理界面

```bash
# 查看当前可同步的表
curl http://localhost:8888/api/tables | jq '.data.tables[].table_name'
```

### 2. 灵活的表选择

您可以通过多种方式选择要同步的表：

#### 方式 A: Web 界面（推荐）
1. 打开 dashboard.html
2. 在表列表中找到目标表
3. 点击"启用"按钮

#### 方式 B: API 调用
```bash
# 启用 users 表同步
curl -X POST http://localhost:8888/api/tables/users/enable

# 禁用 users 表同步
curl -X POST http://localhost:8888/api/tables/users/disable
```

#### 方式 C: 手动编辑配置
编辑 `sync-tables.json`，将表的 `enabled` 字段改为 `true` 或 `false`：

```json
{
  "table_name": "users",
  "enabled": true,  // ← 改这里
  "target_table": "users",
  "sync_mode": "full_and_incremental"
}
```

### 3. 全量 + 增量同步

系统会自动执行以下流程：

```
启用表同步
    ↓
[全量阶段] Flink CDC 读取 PostgreSQL 快照
    ↓
[全量导入] 所有存量数据导入到 StarRocks
    ↓
[增量阶段] 记录 CDC 位置，开始监听 WAL
    ↓
[实时同步] 新增/修改/删除操作实时同步
```

### 4. 自动处理新表

```
PG 新建表 orders_detail
    ↓
API 扫描发现新表
    ↓
自动添加到 sync-tables.json
    ↓
表出现在管理界面
    ↓
您点击启用按钮就可同步
```

## 🔧 常见操作

### 启用某个表同步

```bash
curl -X POST http://localhost:8888/api/tables/orders/enable | jq .
```

### 查看某个表的详细信息

```bash
curl http://localhost:8888/api/tables/users | jq '.data'
```

### 修改同步配置

```bash
curl -X PUT http://localhost:8888/api/sync-config \
  -H "Content-Type: application/json" \
  -d '{
    "sync_config": {
      "options": {
        "chunk.size": 16384,
        "incremental.parallelism": 8
      }
    }
  }' | jq .
```

### 查看系统状态

```bash
curl http://localhost:8888/api/status | jq .
```

## 📊 实时监控

### Flink UI
访问：http://localhost:8081

可以查看：
- TaskManager 状态
- 正在运行的 Jobs
- 数据吞吐量
- 处理延迟

### StarRocks UI
访问：http://localhost:8030

可以查看：
- 已创建的数据库和表
- 数据量统计
- 查询 SQL

### PostgreSQL
```bash
# 连接 PostgreSQL
docker-compose exec postgres psql -U postgres -d cdc_db

# 查看所有表
\dt

# 查看表结构
\d users

# 查看表数据
SELECT * FROM users LIMIT 10;
```

## 🚨 故障排查

### 问题 1: API 无法连接 PostgreSQL

**症状**: `curl http://localhost:8888/api/status` 返回 postgresql: disconnected

**解决方案**:
```bash
# 检查 PostgreSQL 是否运行
docker-compose ps postgres

# 查看 PostgreSQL 日志
docker-compose logs postgres

# 重启 PostgreSQL
docker-compose restart postgres
```

### 问题 2: Flink 连接失败

**症状**: `curl http://localhost:8081/api/overview` 返回 Connection refused

**解决方案**:
```bash
# 检查 Flink 容器日志
docker-compose logs jobmanager
docker-compose logs taskmanager

# 重启 Flink
docker-compose restart jobmanager taskmanager
```

### 问题 3: 表没有出现在管理界面

**症状**: 新增表后，在 API 中查看不到

**解决方案**:
```bash
# 刷新表列表（调用 API）
curl http://localhost:8888/api/tables | jq '.data.tables[].table_name'

# 或在管理界面点击"刷新"按钮
```

## 📝 注意事项

1. **首次启动**：约 2-3 分钟，容器需要初始化
2. **PostgreSQL 密码**：默认为 `postgres`，生产环境应修改
3. **StarRocks 密码**：默认为空，生产环境应设置
4. **数据持久化**：使用 Docker volumes，数据会保留
5. **清除所有数据**：`docker-compose down -v`

## 🎓 下一步

- 查看 [README.md](./README.md) 了解更多配置选项
- 查看 [sync-tables.json](./sync-tables.json) 编辑配置
- 修改 [postgres-init.sql](./postgres-init.sql) 添加自己的表

## 📞 获取帮助

遇到问题？检查以下日志：

```bash
# 所有日志
docker-compose logs -f

# 特定服务日志
docker-compose logs -f api-server
docker-compose logs -f jobmanager
docker-compose logs -f postgres
docker-compose logs -f starrocks-fe
```

祝您使用愉快！🎉
