# Flink CDC PostgreSQL to StarRocks 同步方案

完整的 Flink CDC 数据同步解决方案，支持从 PostgreSQL 实时同步数据到 StarRocks。

## 🚀 快速开始

### 1. 启动环境

```bash
# 给脚本添加执行权限
chmod +x start.sh

# 启动所有容器
./start.sh
```

### 2. 访问服务

- **Flink UI**: http://localhost:8081
- **StarRocks UI**: http://localhost:8030 (用户名: root, 密码为空)
- **API 服务**: http://localhost:8888
- **PostgreSQL**: localhost:5432 (用户: postgres, 密码: postgres)

## 📋 功能特性

### ✅ 已实现的特性

1. **新增表自动识别** 
   - PG 新增表会自动添加到配置中
   - 无需手动修改 Flink CDC 配置

2. **表结构变更自动同步**
   - Flink CDC 支持 DDL 变更事件
   - 表结构变更自动应用到 StarRocks

3. **存量数据全量导入**
   - 启用表同步时，自动执行全量快照
   - 配合增量同步确保数据完整性

4. **灵活的表选择机制**
   - 通过 API 启用/禁用表
   - 支持 Web 界面配置（见前端项目）
   - 配置文件可手动编辑

## 🔧 API 文档

### 查看可同步的表

```bash
curl http://localhost:8888/api/tables | jq .
```

**响应示例:**
```json
{
  "code": 0,
  "message": "success",
  "data": {
    "tables": [
      {
        "table_name": "users",
        "enabled": true,
        "target_table": "users",
        "sync_mode": "full_and_incremental",
        "description": "用户表",
        "row_count": 3,
        "columns": 6
      }
    ],
    "total": 3,
    "enabled_count": 1
  }
}
```

### 启用表同步

```bash
curl -X POST http://localhost:8888/api/tables/users/enable | jq .
```

### 禁用表同步

```bash
curl -X POST http://localhost:8888/api/tables/users/disable | jq .
```

### 获取表详情

```bash
curl http://localhost:8888/api/tables/users | jq .
```

### 获取同步配置

```bash
curl http://localhost:8888/api/sync-config | jq .
```

### 查看系统状态

```bash
curl http://localhost:8888/api/status | jq .
```

### 查看 Flink 状态

```bash
curl http://localhost:8888/api/flink/status | jq .
```

## 📁 项目结构

```
.
├── docker-compose.yml           # Docker Compose 配置
├── postgres-init.sql            # PostgreSQL 初始化脚本
├── sync-tables.json             # 同步表配置文件
├── api-service/
│   ├── app.py                   # Python Flask API 服务
│   └── Dockerfile               # API 服务 Docker 镜像
├── flink-config/                # Flink 配置目录
├── flink-cdc-submit.py          # Flink CDC 任务提交脚本
└── start.sh                      # 启动脚本
```

## ⚙️ 配置说明

### sync-tables.json 配置文件

```json
{
  "postgres": {
    "host": "postgres",
    "port": 5432,
    "username": "postgres",
    "password": "postgres",
    "database": "cdc_db",
    "schema": "public"
  },
  "starrocks": {
    "host": "starrocks-fe",
    "port": 9030,
    "username": "root",
    "password": ""
  },
  "sync_config": {
    "database": "cdc_db",
    "tables": [
      {
        "table_name": "users",
        "enabled": true,              // 是否启用同步
        "target_table": "users",      // 目标表名（可自定义）
        "sync_mode": "full_and_incremental",  // 同步模式
        "description": "用户表"
      }
    ],
    "options": {
      "scan.startup.mode": "initial",           // 全量快照模式
      "scan.incremental.snapshot.enabled": true, // 启用增量快照
      "chunk.size": 8096,                       // 分片大小
      "incremental.parallelism": 4              // 并行度
    }
  }
}
```

## 🔄 工作流程

### 1. 表自动发现

```
PG 新增表 → API 自动扫描 → 添加到 sync-tables.json → 展示在表列表
```

### 2. 启用同步

```
选择表 → 调用 /api/tables/{table_name}/enable
→ 更新配置文件 → Flink 检测配置变更 → 创建同步任务
```

### 3. 全量导入

```
表启用 → Flink CDC 读取快照 → 导入所有存量数据到 SR
→ 记录 CDC 位置 → 开始增量同步
```

### 4. 增量同步

```
PG WAL → Flink CDC 解析 → DDL/DML 事件 → StarRocks
```

## 📝 常见操作

### 查看 PostgreSQL 中的表

```bash
docker-compose exec postgres psql -U postgres -d cdc_db -c "\dt"
```

### 查看 StarRocks 中的表

```bash
docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root
> SHOW DATABASES;
> USE cdc_db;
> SHOW TABLES;
```

### 查看 Flink 日志

```bash
docker-compose logs -f jobmanager
```

### 查看 API 服务日志

```bash
docker-compose logs -f api-server
```

### 停止所有容器

```bash
docker-compose down
```

### 清理数据卷

```bash
docker-compose down -v
```

## 🐛 故障排查

### Flink 无法连接 PostgreSQL

1. 检查 PostgreSQL 是否启动：`curl localhost:5432`
2. 检查日志：`docker-compose logs postgres`
3. 验证连接参数在 sync-tables.json 中是否正确

### StarRocks 无法连接

1. 检查 FE 是否启动：`curl http://localhost:8030/api/bootstrap`
2. 检查日志：`docker-compose logs starrocks-fe`
3. 确认 BE 已连接到 FE

### API 返回 500 错误

1. 查看 API 服务日志：`docker-compose logs api-server`
2. 检查配置文件是否存在：`ls sync-tables.json`
3. 确保文件有正确的 JSON 格式

## 🚢 生产部署建议

1. **使用持久化存储**
   - 将 PostgreSQL 数据、StarRocks 数据单独备份
   - 配置数据卷到高可用存储

2. **启用认证**
   - 修改 PostgreSQL 密码
   - 配置 StarRocks 用户密码
   - 保护 API 服务（添加认证中间件）

3. **监控和告警**
   - 监控 Flink 任务状态
   - 监控 CDC 延迟
   - 监控数据质量

4. **高可用**
   - 多个 TaskManager
   - StarRocks 多副本配置
   - PostgreSQL 主从复制

## 📚 相关文档

- [Flink CDC 官方文档](https://nightlies.apache.org/flink/flink-cdc-docs-master/)
- [StarRocks 官方文档](https://docs.starrocks.io/)
- [PostgreSQL Logical Replication](https://www.postgresql.org/docs/current/logical-replication.html)

## 📄 许可证

MIT

## 🤝 贡献

欢迎提交 Issue 和 Pull Request！
