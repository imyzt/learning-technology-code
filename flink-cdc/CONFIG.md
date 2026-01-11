# 配置说明文档

## 1. docker-compose.yml

### PostgreSQL 配置
```yaml
postgres:
  environment:
    POSTGRES_USER: postgres           # 用户名
    POSTGRES_PASSWORD: postgres       # 密码（需要改为强密码）
    POSTGRES_DB: cdc_db              # 初始数据库名
  command:
    - "-c"
    - "wal_level=logical"            # 启用逻辑复制（必需！）
    - "-c"
    - "max_wal_senders=4"            # 最大 WAL 发送器数
    - "-c"
    - "max_replication_slots=4"      # 最大复制槽数
```

**重要**: PG 必须配置 `wal_level=logical` 才能支持 CDC

### StarRocks 配置
```yaml
starrocks-fe:
  image: starrocks/fe:3.1.0          # FE (Frontend) 版本
  ports:
    - "8030:8030"                    # HTTP 端口
    - "9030:9030"                    # MySQL 协议端口

starrocks-be:
  image: starrocks/be:3.1.0          # BE (Backend) 版本
  ports:
    - "8040:8040"                    # HTTP 端口
```

### Flink 配置
```yaml
jobmanager:
  environment:
    - FLINK_PROPERTIES=...
    jobmanager.rpc.address: jobmanager
    taskmanager.numberOfTaskSlots: 4         # 每个 TaskManager 的 slot 数
    parallelism.default: 4                   # 默认并行度
```

**调整建议**:
- 开发环境：`numberOfTaskSlots: 2`, `parallelism.default: 2`
- 生产环境：根据 CPU 核数调整，通常为 `(CPU核数 - 1) / 2`

### API 服务配置
```yaml
api-server:
  environment:
    - FLASK_ENV=production
    - POSTGRES_HOST=postgres         # PostgreSQL 地址
    - STARROCKS_HOST=starrocks-fe    # StarRocks FE 地址
    - FLINK_HOST=jobmanager          # Flink JobManager 地址
```

## 2. sync-tables.json

### PostgreSQL 连接配置
```json
{
  "postgres": {
    "host": "postgres",              // Docker 服务名 或 IP 地址
    "port": 5432,
    "username": "postgres",          // PostgreSQL 用户
    "password": "postgres",          // PostgreSQL 密码
    "database": "cdc_db",            // 要同步的数据库
    "schema": "public"               // 要同步的 schema（通常为 public）
  }
}
```

### StarRocks 连接配置
```json
{
  "starrocks": {
    "host": "starrocks-fe",          // 连接 FE 节点
    "port": 9030,                    // MySQL 协议端口
    "username": "root",              // StarRocks 用户（默认 root）
    "password": ""                   // 密码（默认为空）
  }
}
```

### 同步表配置
```json
{
  "sync_config": {
    "database": "cdc_db",            // 目标数据库名
    "tables": [
      {
        "table_name": "users",       // PostgreSQL 中的表名
        "enabled": true,             // 是否启用同步（核心！）
        "target_table": "users",     // StarRocks 中的表名（可不同）
        "sync_mode": "full_and_incremental",  // 同步模式
        "description": "用户表"      // 表描述（可选）
      },
      {
        "table_name": "orders",
        "enabled": false,            // 禁用此表同步
        "target_table": "orders",
        "sync_mode": "full_and_incremental",
        "description": "订单表"
      }
    ],
    "options": {
      "scan.startup.mode": "initial",           // 初始启动模式
      "scan.incremental.snapshot.enabled": true, // 启用增量快照
      "chunk.size": 8096,                       // 快照分片大小
      "incremental.parallelism": 4,             // 增量同步并行度
      "server.id": 5400,                        // PostgreSQL CDC 的 server id
      "scan.snapshot.fetch.size": 1024,         // 快照提取大小
      "connection.pool.size": 20                // 连接池大小
    }
  }
}
```

## 3. 高级配置

### 性能优化

#### 增加并行度（加快同步速度）
```json
{
  "sync_config": {
    "options": {
      "chunk.size": 16384,              // 增加分片大小（更快但更耗内存）
      "incremental.parallelism": 8      // 增加并行度（需要更多 slot）
    }
  }
}
```

#### 减少资源占用（适合开发环境）
```json
{
  "sync_config": {
    "options": {
      "chunk.size": 4096,               // 减小分片大小
      "incremental.parallelism": 2      // 减少并行度
    }
  }
}
```

### 选择性同步

只同步需要的表：
```json
{
  "tables": [
    {
      "table_name": "users",
      "enabled": true,    // ✅ 同步
      "sync_mode": "full_and_incremental"
    },
    {
      "table_name": "logs",
      "enabled": false,   // ❌ 不同步
      "sync_mode": "full_and_incremental"
    },
    {
      "table_name": "temp_data",
      "enabled": false,   // ❌ 不同步
      "sync_mode": "incremental_only"
    }
  ]
}
```

### 表名映射

将 PostgreSQL 表同步到不同名称的 StarRocks 表：
```json
{
  "table_name": "user_profiles",       // PG 中的原始表名
  "target_table": "user_data",         // SR 中的目标表名
  "enabled": true
}
```

## 4. 环境变量

在 docker-compose.yml 中或 `.env` 文件中设置：

```bash
# PostgreSQL
POSTGRES_USER=postgres
POSTGRES_PASSWORD=your_secure_password
POSTGRES_DB=cdc_db

# StarRocks
SR_FE_HOST=starrocks-fe
SR_FE_PORT=9030
SR_USER=root
SR_PASSWORD=your_password

# Flink
FLINK_TASKMANAGER_SLOTS=4
FLINK_PARALLELISM=4

# API
API_PORT=8888
API_HOST=0.0.0.0
```

## 5. 监控和告警指标

### 关键指标

1. **CDC 延迟**: Flink UI 中的 `record-lag`
2. **处理速率**: Flink UI 中的 `records-in-per-sec`
3. **错误率**: Flink UI 中的 `num-failed-checkpoints`
4. **连接状态**: `/api/status` 端点

### 设置告警阈值

```json
{
  "alerts": {
    "cdc_lag_ms": 60000,              // CDC 延迟超过 60s
    "error_rate": 0.01,               // 错误率超过 1%
    "checkpoint_timeout": 600000,     // 检查点超时
    "connection_timeout": 5000        // 连接超时
  }
}
```

## 6. 生产环境建议

### 安全配置
```yaml
postgres:
  environment:
    POSTGRES_PASSWORD: ${SECURE_PASSWORD}  # 使用环境变量
  ports:
    - "127.0.0.1:5432:5432"              # 仅本地访问

starrocks-fe:
  ports:
    - "127.0.0.1:8030:8030"              # 内部网络访问
```

### 高可用配置
```yaml
# 多个 JobManager 和 TaskManager
jobmanager:
  replicas: 3  # 如果使用 swarm

taskmanager:
  replicas: 3
```

### 资源限制
```yaml
services:
  postgres:
    deploy:
      resources:
        limits:
          cpus: '2'
          memory: 4G
        reservations:
          cpus: '1'
          memory: 2G
```

## 7. 故障恢复

### 配置持久化存储

```yaml
volumes:
  postgres_data:
    driver: local
    driver_opts:
      type: nfs
      o: addr=nas.example.com,vers=4
      device: ":/exports/postgres"
```

### 保存 Flink 检查点

```json
{
  "sync_config": {
    "options": {
      "checkpoint.dir": "file:///flink-checkpoints",
      "state.checkpoints.num-retained": 5
    }
  }
}
```

## 8. 常见配置错误

### ❌ 错误 1: WAL 级别未设置为 logical
```yaml
# 错误
command: ["postgres"]

# 正确
command:
  - "postgres"
  - "-c"
  - "wal_level=logical"
```

### ❌ 错误 2: 表未启用同步
```json
// 错误
"enabled": false  // 没有启用！

// 正确
"enabled": true
```

### ❌ 错误 3: 连接地址错误
```json
// 错误（如果在 Docker 中运行）
"host": "localhost"    // localhost 在容器中指向容器本身

// 正确
"host": "postgres"     // 使用 Docker 服务名
```

更新配置后，需要重启容器：
```bash
docker-compose restart
```

某些配置（如 PostgreSQL WAL 级别）需要完全重建：
```bash
docker-compose down -v
docker-compose up -d
```
