# Flink CDC 3.5 数据同步平台 - 完整使用指南

## 📖 项目简介

这是一个基于 **Flink CDC 3.5** 的生产级数据同步解决方案，实现 PostgreSQL 到 StarRocks 的实时数据同步。

### ✨ 核心特性

- ✅ **基于Flink CDC 3.5** - 使用最新稳定版本
- ✅ **零代码配置** - 通过YAML文件配置同步规则
- ✅ **全量+增量同步** - 自动处理存量数据和增量变更
- ✅ **Schema Evolution** - DDL变更自动同步到目标表
- ✅ **整库/多表同步** - 支持同时同步多张表
- ✅ **Web管理界面** - 便捷的可视化管理
- ✅ **生产环境分离** - 测试和生产配置完全分离
- ✅ **Exactly-Once** - 保证数据一致性

### 🏗️ 架构设计

```
┌─────────────────┐
│   PostgreSQL    │  (WAL Logical Replication)
│   (Source DB)   │
└────────┬────────┘
         │
         ▼
┌────────────────────────────┐
│  Flink CDC 3.5 Runtime     │
│  ┌──────────────────────┐  │
│  │  CDC Pipeline        │  │
│  │  - Full Snapshot     │  │
│  │  - Incremental WAL   │  │
│  │  - Schema Evolution  │  │
│  └──────────────────────┘  │
└────────┬───────────────────┘
         │
         ▼
┌─────────────────┐
│   StarRocks     │  (OLAP Database)
│   (Target DB)   │
└─────────────────┘

┌─────────────────┐
│  CDC Manager    │  (Web UI + API)
│  - 表管理       │
│  - Pipeline配置 │
│  - 监控面板     │
└─────────────────┘
```

## 🚀 快速开始

### 1. 环境要求

- Docker >= 20.10
- Docker Compose >= 2.0
- 可用内存 >= 8GB
- 可用磁盘 >= 20GB

### 2. 启动服务

```bash
# 进入项目目录
cd /Users/imyzt/dev/ideaWorkspace/learning-technology-code/flink-cdc

# 给启动脚本添加执行权限
chmod +x start-new.sh stop-new.sh

# 启动所有服务（首次启动需要构建镜像，约5-10分钟）
./start-new.sh
```

### 3. 访问服务

启动成功后，可以访问以下服务：

- **Flink Web UI**: http://localhost:8081
- **CDC Manager**: http://localhost:8888/ui
- **StarRocks FE**: http://localhost:8030

数据库连接：
- **PostgreSQL**: `localhost:5432` (postgres/postgres)
- **StarRocks**: `localhost:9030` (root/)

### 4. 验证服务状态

```bash
# 查看系统状态
curl http://localhost:8888/api/system/status | jq

# 查看所有表
curl http://localhost:8888/api/tables | jq

# 查看Flink集群状态
curl http://localhost:8081/overview | jq
```

## 📋 使用指南

### 启用表同步

#### 方法1: 使用Web UI

1. 访问 http://localhost:8888/ui
2. 点击"表管理"
3. 在表列表中找到要同步的表
4. 点击"启用"按钮

#### 方法2: 使用API

```bash
# 启用users表同步
curl -X POST http://localhost:8888/api/tables/users/enable

# 启用orders表同步
curl -X POST http://localhost:8888/api/tables/orders/enable

# 启用products表同步
curl -X POST http://localhost:8888/api/tables/products/enable
```

#### 方法3: 直接编辑配置文件

编辑 `cdc-pipelines/pipeline-config.yaml`，设置 `enabled: true`：

```yaml
tables:
  - table_name: users
    enabled: true  # 修改这里
    source_schema: public
    source_table: users
    sink_database: cdc_db
    sink_table: users
    description: "用户表"
```

### 生成并提交Pipeline

```bash
# 1. 生成Pipeline配置
curl -X POST http://localhost:8888/api/pipeline/generate | jq

# 2. 提交Pipeline到Flink
curl -X POST http://localhost:8888/api/pipeline/submit \
  -H "Content-Type: application/json" \
  -d '{"pipeline_file": "generated-pipeline.yaml"}' | jq

# 3. 查看运行中的作业
curl http://localhost:8888/api/pipeline/jobs | jq
```

### 手动提交Pipeline（高级用法）

如果需要手动提交Pipeline：

```bash
# 进入Flink CDC容器
docker exec -it flink-cdc-runtime bash

# 使用flink-cdc.sh提交
cd /opt/flink-cdc
./bin/flink-cdc.sh /opt/cdc-pipelines/pg-to-sr-pipeline.yaml
```

### 验证数据同步

```bash
# 检查PostgreSQL数据
docker exec -it flink-cdc-postgres psql -U postgres -d cdc_db -c "SELECT COUNT(*) FROM users;"

# 检查StarRocks数据
docker exec -it flink-cdc-starrocks mysql -P9030 -h127.0.0.1 -uroot -e "SELECT COUNT(*) FROM cdc_db.users;"

# 通过API查看同步状态
curl http://localhost:8888/api/tables/users/sync-status | jq
```

### 查看日志

```bash
# 查看所有服务日志
docker-compose -f docker-compose-new.yml logs -f

# 查看特定服务日志
docker-compose -f docker-compose-new.yml logs -f flink-cdc-runtime
docker-compose -f docker-compose-new.yml logs -f cdc-manager
docker-compose -f docker-compose-new.yml logs -f postgres
docker-compose -f docker-compose-new.yml logs -f starrocks

# 查看Flink日志文件
docker exec flink-cdc-runtime tail -f /opt/flink/log/flink-*.log
```

## 🔧 配置说明

### Pipeline配置文件

主配置文件位于 `cdc-pipelines/pg-to-sr-pipeline.yaml`：

```yaml
# Source配置
source:
  type: postgres
  hostname: postgres  # 生产环境改为实际地址
  port: 5432
  username: postgres
  password: postgres
  database: cdc_db
  schema-list: public
  table-list: public.users,public.orders,public.products
  slot.name: flink_cdc_slot
  scan.startup.mode: initial  # initial=全量+增量, latest-offset=仅增量

# Sink配置
sink:
  type: starrocks
  jdbc-url: jdbc:mysql://starrocks:9030
  load-url: starrocks:8040
  username: root
  password: ""
  table.create.properties.replication_num: 1

# Route配置（表映射）
route:
  - source-table: public.users
    sink-table: cdc_db.users
  - source-table: public.orders
    sink-table: cdc_db.orders

# Pipeline配置
pipeline:
  name: PostgreSQL to StarRocks CDC Pipeline
  parallelism: 2
```

### 环境变量配置

在 `docker-compose-new.yml` 中修改环境变量：

```yaml
# PostgreSQL配置
POSTGRES_HOST: postgres
POSTGRES_PORT: 5432
POSTGRES_DB: cdc_db
POSTGRES_USER: postgres
POSTGRES_PASSWORD: postgres

# StarRocks配置
STARROCKS_HOST: starrocks
STARROCKS_FE_HTTP_PORT: 8030
STARROCKS_QUERY_PORT: 9030
STARROCKS_USER: root
STARROCKS_PASSWORD: ""
```

## 🏭 生产环境部署

### 1. 修改数据库连接

编辑 `cdc-pipelines/pipeline-config.yaml`：

```yaml
production:
  source:
    hostname: "${PROD_POSTGRES_HOST}"
    port: "${PROD_POSTGRES_PORT:-5432}"
    username: "${PROD_POSTGRES_USER}"
    password: "${PROD_POSTGRES_PASSWORD}"
    database: "${PROD_POSTGRES_DB}"
  
  sink:
    jdbc-url: "jdbc:mysql://${PROD_STARROCKS_HOST}:${PROD_STARROCKS_QUERY_PORT:-9030}"
    load-url: "${PROD_STARROCKS_HOST}:${PROD_STARROCKS_BE_HTTP_PORT:-8040}"
    username: "${PROD_STARROCKS_USER}"
    password: "${PROD_STARROCKS_PASSWORD}"
```

### 2. 设置环境变量

```bash
export ENV=production
export PROD_POSTGRES_HOST=your-postgres-host
export PROD_POSTGRES_PORT=5432
export PROD_POSTGRES_USER=your-username
export PROD_POSTGRES_PASSWORD=your-password
export PROD_POSTGRES_DB=your-database

export PROD_STARROCKS_HOST=your-starrocks-host
export PROD_STARROCKS_QUERY_PORT=9030
export PROD_STARROCKS_USER=root
export PROD_STARROCKS_PASSWORD=your-password
```

### 3. 移除测试数据库容器

修改 `docker-compose-new.yml`，注释掉或删除 `postgres` 和 `starrocks` 服务，仅保留：
- flink-cdc-runtime
- cdc-manager
- cdc-web-ui (可选)

### 4. PostgreSQL准备工作

确保生产PostgreSQL已配置CDC：

```sql
-- 1. 启用逻辑复制
ALTER SYSTEM SET wal_level = logical;
ALTER SYSTEM SET max_wal_senders = 10;
ALTER SYSTEM SET max_replication_slots = 10;

-- 重启PostgreSQL后验证
SHOW wal_level;  -- 应该返回 'logical'

-- 2. 创建复制槽
SELECT pg_create_logical_replication_slot('flink_cdc_slot', 'pgoutput');

-- 3. 创建发布
CREATE PUBLICATION flink_cdc_pub FOR ALL TABLES;

-- 4. 授权
GRANT SELECT ON ALL TABLES IN SCHEMA public TO your_user;
GRANT USAGE ON SCHEMA public TO your_user;
```

## 🔍 监控和运维

### 查看作业状态

```bash
# 通过API查看
curl http://localhost:8888/api/pipeline/jobs | jq

# 通过Flink Web UI
open http://localhost:8081
```

### 取消作业

```bash
# 获取job_id
curl http://localhost:8888/api/pipeline/jobs | jq '.data.jobs[] | select(.state=="RUNNING") | .jid'

# 取消作业
curl -X POST http://localhost:8888/api/pipeline/cancel/<job_id>
```

### 重启作业

```bash
# 1. 取消当前作业
curl -X POST http://localhost:8888/api/pipeline/cancel/<job_id>

# 2. 重新生成并提交
curl -X POST http://localhost:8888/api/pipeline/generate | jq
curl -X POST http://localhost:8888/api/pipeline/submit -d '{"pipeline_file": "generated-pipeline.yaml"}'
```

### 检查数据一致性

```bash
# 比较源表和目标表行数
curl http://localhost:8888/api/tables/users/sync-status | jq
```

## 🛠️ 故障排查

### 问题1: Flink作业提交失败

**原因**: Flink集群资源不足或连接问题

**解决**:
```bash
# 检查Flink集群状态
curl http://localhost:8081/overview | jq

# 查看TaskManager
docker-compose -f docker-compose-new.yml logs flink-cdc-runtime

# 增加资源（修改docker-compose-new.yml）
taskmanager.numberOfTaskSlots: 8  # 增加slot数量
```

### 问题2: PostgreSQL连接失败

**原因**: 复制槽未创建或权限不足

**解决**:
```bash
# 进入PostgreSQL
docker exec -it flink-cdc-postgres psql -U postgres -d cdc_db

# 检查复制槽
SELECT * FROM pg_replication_slots;

# 重新创建
SELECT pg_create_logical_replication_slot('flink_cdc_slot', 'pgoutput');
```

### 问题3: StarRocks写入失败

**原因**: 表不存在或权限问题

**解决**:
```bash
# 进入StarRocks
docker exec -it flink-cdc-starrocks mysql -P9030 -h127.0.0.1 -uroot

# 检查数据库和表
SHOW DATABASES;
USE cdc_db;
SHOW TABLES;

# 手动创建数据库
CREATE DATABASE IF NOT EXISTS cdc_db;
```

### 问题4: 数据同步延迟

**原因**: 网络延迟或资源不足

**解决**:
- 增加parallelism（并行度）
- 增加Flink资源
- 检查网络连接
- 优化StarRocks表结构（增加bucket数量）

## 📊 性能优化

### Flink配置优化

编辑 `flink-cdc-runtime/flink-conf.yaml`：

```yaml
# 增加内存
taskmanager.memory.process.size: 4096m
jobmanager.memory.process.size: 2048m

# 增加并行度
taskmanager.numberOfTaskSlots: 8
parallelism.default: 4

# 优化checkpoint
execution.checkpointing.interval: 30000  # 30秒
execution.checkpointing.max-concurrent-checkpoints: 1
```

### StarRocks优化

```sql
-- 增加表的bucket数量
ALTER TABLE cdc_db.users SET ("bucket_num" = "16");

-- 启用轻量级Schema变更
ALTER TABLE cdc_db.users SET ("light_schema_change" = "true");
```

## 🔐 安全建议

1. **修改默认密码**
   - PostgreSQL: postgres/postgres
   - StarRocks: root/（空密码）

2. **使用环境变量**
   - 不要在配置文件中硬编码密码
   - 使用Docker secrets或环境变量

3. **网络隔离**
   - 生产环境使用专用网络
   - 限制端口访问

4. **启用SSL**
   - PostgreSQL SSL连接
   - StarRocks SSL连接

## 📞 支持与反馈

- 官方文档: https://nightlies.apache.org/flink/flink-cdc-docs-release-3.5/
- GitHub: https://github.com/apache/flink-cdc
- 问题反馈: 在项目中创建Issue

## 📄 许可证

本项目基于 Apache License 2.0
