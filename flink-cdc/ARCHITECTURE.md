# Flink CDC 系统架构与实现方案

## 系统架构图

```
┌─────────────────────────────────────────────────────────────────────┐
│                         PostgreSQL Source                           │
│  ┌──────────────────────────────────────────────────────────────┐   │
│  │ WAL (Write Ahead Log) with logical replication              │   │
│  │ Replication Slot: flink_slot                                │   │
│  │ Publication: flink_pub (ALL TABLES)                         │   │
│  └──────────────────────────────────────────────────────────────┘   │
└────────────────────────────────┬─────────────────────────────────────┘
                                 │
                    ┌────────────┴────────────┐
                    │   CDC Events Stream     │
                    │  (DDL/DML/Snapshots)   │
                    └────────────┬────────────┘
                                 │
         ┌───────────────────────┴───────────────────────┐
         │                                               │
┌────────▼──────────────────────────────┐   ┌──────────▼────────────────┐
│     Flink Cluster (JobManager +       │   │   API Service (Flask)    │
│     TaskManager)                      │   │                          │
│                                       │   │  • Table Discovery      │
│  ┌─────────────────────────────────┐ │   │  • Sync Management      │
│  │ Flink CDC Source Connector      │ │   │  • Configuration API    │
│  │ - Read full snapshot (full load)│ │   │  • System Status        │
│  │ - Parse WAL events (incremental)│ │   │                          │
│  │ - Handle DDL changes            │ │   └──────────┬──────────────┘
│  └────────┬────────────────────────┘ │              │
│           │                          │   ┌──────────▼──────────────┐
│           │ (Filtered by enabled     │   │  Web Dashboard (HTML)   │
│           │  tables from             │   │                         │
│           │  sync-tables.json)       │   │  • Table List View      │
│           │                          │   │  • Enable/Disable UI    │
│  ┌────────▼────────────────────────┐ │   │  • System Status        │
│  │ Flink Sink Connector (StarRocks)│ │   │  • Real-time Monitoring│
│  │ - Insert data into SR tables     │ │   └─────────────────────────┘
│  │ - Apply DDL/DML operations      │ │
│  └────────┬────────────────────────┘ │
│           │                          │
└───────────┼──────────────────────────┘
            │
┌───────────▼────────────────────────────────────────────────────────┐
│                    StarRocks Data Warehouse                         │
│  ┌───────────────────────────────────────────────────────────────┐ │
│  │  FE (Frontend)          │  BE (Backend)                       │ │
│  │  - Query Processing     │  - Data Storage                     │ │
│  │  - Metadata Management  │  - Columnar Storage                 │ │
│  │  - Load Balancing       │  - Query Execution                  │ │
│  └───────────────────────────────────────────────────────────────┘ │
└────────────────────────────────────────────────────────────────────┘
```

## 数据流处理流程

### 1. 启用表同步流程

```
用户操作
   │
   ├─ Web 界面点击"启用" 
   │  或
   ├─ API: POST /api/tables/{table_name}/enable
   │  或
   └─ 手动编辑 sync-tables.json

        │
        ▼
API Service
   │
   ├─ 更新 sync-tables.json (enabled=true)
   │
   └─ 返回成功响应

        │
        ▼
Flink 监听配置变更
   │
   ├─ 检测新启用的表
   │
   └─ 创建新的 CDC 任务

        │
        ▼
全量数据导入阶段
   │
   ├─ 执行 SELECT * FROM table
   ├─ 分批读取数据（chunk-based）
   ├─ 转换为 INSERT 语句
   │
   └─ 导入到 StarRocks

        │
        ▼
增量同步阶段
   │
   ├─ 记录 CDC 消费位置
   ├─ 监听 PostgreSQL WAL 日志
   ├─ 解析 INSERT/UPDATE/DELETE 事件
   ├─ 解析 DDL 变更事件
   │
   └─ 实时应用到 StarRocks
```

### 2. 表自动发现流程

```
定期或主动调用 API
   │
   ▼
GET /api/tables
   │
   ├─ 查询 PostgreSQL pg_tables 视图
   │  (SELECT tablename FROM pg_tables WHERE schemaname='public')
   │
   ├─ 对比 sync-tables.json 中已有的表
   │
   ├─ 发现新表
   │
   └─ 自动添加到 sync-tables.json
   │
   ├─ enabled=false（等待用户激活）
   ├─ target_table=原表名
   ├─ sync_mode=full_and_incremental
   │
   ▼
返回完整的表列表
```

### 3. 结构变更同步流程

```
PostgreSQL 执行 DDL
例: ALTER TABLE users ADD COLUMN phone VARCHAR(20)
   │
   ▼
WAL 日志记录 DDL 事件
   │
   ▼
Flink CDC 解析 DDL 事件
   │
   ├─ 识别变更类型 (ADD/DROP/MODIFY COLUMN)
   ├─ 生成对应的 DDL 语句
   │
   ▼
通过 Flink StarRocks Sink
   │
   ├─ 转换为 StarRocks 兼容的 DDL
   │  例: ALTER TABLE users ADD COLUMN phone VARCHAR(20)
   │
   ▼
应用到 StarRocks
   │
   └─ 表结构自动同步完成
```

## 关键特性实现

### ✅ 特性 1: 新增表自动识别

**实现方式**: 表发现机制（Discovery）

```python
# api-service/app.py 中的实现

def get_all_tables():
    """从 PostgreSQL 获取所有表"""
    cur.execute("""
        SELECT tablename FROM pg_tables
        WHERE schemaname = 'public'
    """)
    all_tables = [row['tablename'] for row in cur.fetchall()]
    
    return all_tables

@app.route('/api/tables', methods=['GET'])
def list_tables():
    config = load_config()
    all_pg_tables = get_all_tables()
    sync_config = {t['table_name']: t for t in config['sync_config']['tables']}
    
    for table_name in all_pg_tables:
        if table_name not in sync_config:
            # 新表自动添加
            new_table = {
                'table_name': table_name,
                'enabled': False,
                'sync_mode': 'full_and_incremental'
            }
            config['sync_config']['tables'].append(new_table)
    
    save_config(config)  # 持久化
```

**优势**:
- 无需修改 Flink 配置
- 新表自动出现在管理界面
- 用户自主选择是否启用

### ✅ 特性 2: 表结构变更自动同步

**实现方式**: Flink CDC 的 DDL 支持

PostgreSQL 配置:
```sql
-- 启用逻辑复制
ALTER SYSTEM SET wal_level = logical;

-- 创建复制槽
SELECT * FROM pg_create_logical_replication_slot('flink_slot', 'pgoutput');

-- 创建发布（包含所有表）
CREATE PUBLICATION flink_pub FOR ALL TABLES;
```

Flink 配置:
```json
{
  "props": {
    "debezium.slot.name": "flink_slot",
    "debezium.publication.name": "flink_pub",
    "debezium.include.schema.changes": true,  // ← 启用 DDL 事件
    "debezium.include.unknown.datatypes": true
  }
}
```

**流程**:
- Debezium 捕获 DDL 事件 → Flink CDC 解析 → StarRocks Sink 执行

### ✅ 特性 3: 存量数据全量导入

**实现方式**: 两阶段提交 (Snapshot + Incremental)

```
启用表同步时:

[阶段 1] 全量快照 (Snapshot)
├─ 执行: SELECT * FROM users
├─ 方式: 分批读取（避免大表锁定）
│  chunk.size = 8096  (每个分片 8096 行)
├─ 转换: 转为 StarRocks INSERT 语句
└─ 结果: 所有存量数据导入

[阶段 2] 增量同步 (Incremental)
├─ 记录: 快照结束的 LSN (Log Sequence Number)
├─ 监听: 从该 LSN 之后的 WAL 日志
├─ 处理: INSERT/UPDATE/DELETE 事件
└─ 周期: 实时处理（毫秒级延迟）
```

### ✅ 特性 4: 配置型表选择

**实现方式**: sync-tables.json 配置文件

```json
{
  "sync_config": {
    "tables": [
      {
        "table_name": "users",
        "enabled": true,           // ← 控制是否同步
        "target_table": "users",
        "sync_mode": "full_and_incremental"
      }
    ]
  }
}
```

**三种选择方式**:

1. **Web 界面** (推荐给非技术用户)
```
打开 dashboard.html → 选择表 → 点击"启用" → 自动更新配置
```

2. **REST API** (推荐给集成者)
```bash
curl -X POST http://localhost:8888/api/tables/users/enable
```

3. **手动编辑** (推荐给运维人员)
```bash
# 编辑 sync-tables.json
# 修改 "enabled" 字段
# 重启 API 或手动刷新
```

## 配置说明

### sync-tables.json 核心参数

| 参数 | 说明 | 示例 |
|------|------|------|
| `postgres.host` | PG 主机地址 | `postgres` |
| `postgres.database` | PG 数据库名 | `cdc_db` |
| `starrocks.host` | StarRocks FE 地址 | `starrocks-fe` |
| `table_name` | PostgreSQL 中的表名 | `users` |
| `enabled` | **是否启用同步** | `true/false` |
| `target_table` | StarRocks 中的表名 | `users` |
| `sync_mode` | 同步模式 | `full_and_incremental` |
| `chunk.size` | 快照分片大小 | `8096` |
| `incremental.parallelism` | 增量同步并行度 | `4` |

### 性能调优参数

```json
{
  "options": {
    // 快照阶段参数
    "scan.startup.mode": "initial",           // 从头开始
    "chunk.size": 8096,                       // ↑ 更大 = 更快但更耗内存
    "scan.snapshot.fetch.size": 1024,         // 每次 fetch 的行数
    
    // 增量阶段参数
    "incremental.parallelism": 4,             // ↑ 增加并行度 = 更快
    "connection.pool.size": 20,               // 连接池大小
    
    // 网络参数
    "scan.fetch.size": 1024,                  // 网络批次大小
    "server.id": 5400                         // CDC server id
  }
}
```

## 部署架构

### 开发环境（单机 Docker Compose）
```
┌──────────────────────────────┐
│  Docker Host (Local Dev)     │
│  ┌──────────────────────┐    │
│  │ PostgreSQL 15        │    │
│  │ StarRocks FE + BE    │    │
│  │ Flink JobManager     │    │
│  │ Flink TaskManager    │    │
│  │ API Service (Flask)  │    │
│  │ Web Dashboard        │    │
│  └──────────────────────┘    │
└──────────────────────────────┘
```

### 生产环境（高可用）
```
┌─────────────────────────────────────────────────┐
│              Kubernetes Cluster                 │
├──────────────┬──────────────┬──────────────────┤
│              │              │                  │
│  PostgreSQL  │ StarRocks    │ Flink            │
│  - 主从      │ - FE x3      │ - JobManager x3  │
│  - 备份      │ - BE x3      │ - TaskManager x10│
│              │ - Connector  │                  │
└──────────────┴──────────────┴──────────────────┘
```

## 监控指标

### 关键性能指标 (KPI)

| 指标 | 来源 | 告警阈值 | 说明 |
|------|------|----------|------|
| **CDC Lag** | Flink UI | > 60s | CDC 延迟，越小越好 |
| **Records/sec** | Flink Metrics | < 1000 | 每秒处理记录数 |
| **Checkpoint Success** | Flink UI | < 95% | 检查点成功率 |
| **Error Rate** | Flink Logs | > 1% | 错误发生率 |
| **Data Consistency** | Custom | 不一致 | SR 和 PG 数据对比 |

### 查看监控指标

```bash
# Flink UI
http://localhost:8081

# API 状态检查
curl http://localhost:8888/api/status | jq .

# StarRocks 表统计
mysql -h localhost -P 9030 -u root -e "SELECT * FROM information_schema.TABLES" cdc_db

# PostgreSQL 复制状态
psql -U postgres -d cdc_db -c "SELECT * FROM pg_replication_slots;"
```

## 故障恢复

### 场景 1: Flink 任务失败

```bash
# 检查日志
docker-compose logs jobmanager

# 重启 Flink
docker-compose restart jobmanager taskmanager

# 查看恢复进度
curl http://localhost:8081/api/jobs | jq '.jobs[0].status'
```

### 场景 2: CDC 延迟过高

```bash
# 增加并行度
# 编辑 sync-tables.json
{
  "options": {
    "incremental.parallelism": 8,  // 从 4 改为 8
    "chunk.size": 16384            // 从 8096 改为 16384
  }
}

# 重新启动任务
```

### 场景 3: 数据不一致

```bash
# 检查 PostgreSQL 表
psql -U postgres -d cdc_db -c "SELECT COUNT(*) FROM users;"

# 检查 StarRocks 表
mysql -h localhost -P 9030 -u root cdc_db -e "SELECT COUNT(*) FROM users;"

# 如果行数不匹配，检查 CDC 延迟
curl http://localhost:8081/api/jobs | jq '.jobs[0].metrics'

# 必要时重新全量同步
# 禁用表 -> 清空 SR 表 -> 启用表
```

## 文件说明

| 文件 | 用途 | 修改频率 |
|------|------|----------|
| `docker-compose.yml` | 容器编排配置 | 低（初始设置） |
| `sync-tables.json` | **表同步配置** | 高（频繁启用/禁用表） |
| `postgres-init.sql` | PG 初始化脚本 | 中（添加新表时） |
| `api-service/app.py` | API 服务代码 | 低（扩展功能时） |
| `dashboard.html` | Web 管理界面 | 中（UI 改进时） |
| `flink-cdc-submit.py` | 任务提交脚本 | 低（自动化时） |

## 最佳实践

1. **表命名规范**: 使用小写 + 下划线（如 `user_orders`）
2. **定期备份**: `docker-compose volumes` 定期备份
3. **监控告警**: 设置 CDC 延迟告警（推荐 < 30s）
4. **性能测试**: 先在开发环境测试，再上生产
5. **版本管理**: 跟踪 sync-tables.json 的变更历史
6. **文档维护**: 记录每个表的同步原因和业务含义

---

📚 更多信息见 [README.md](./README.md)、[QUICKSTART.md](./QUICKSTART.md) 和 [CONFIG.md](./CONFIG.md)
