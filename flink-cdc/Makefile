.PHONY: help up down logs status clean restart ps shell-pg shell-sr

# 默认帮助信息
help:
	@echo "Flink CDC Docker Compose 快速命令"
	@echo ""
	@echo "常用命令:"
	@echo "  make up              - 启动所有容器"
	@echo "  make down            - 停止所有容器"
	@echo "  make restart         - 重启所有容器"
	@echo "  make logs            - 查看所有日志 (实时)"
	@echo "  make logs-api        - 查看 API 服务日志"
	@echo "  make logs-flink      - 查看 Flink JobManager 日志"
	@echo "  make logs-pg         - 查看 PostgreSQL 日志"
	@echo "  make logs-sr         - 查看 StarRocks 日志"
	@echo "  make ps              - 查看容器状态"
	@echo "  make status          - 查看系统健康状态"
	@echo "  make clean           - 删除所有容器和数据（谨慎！）"
	@echo ""
	@echo "数据库操作:"
	@echo "  make shell-pg        - 连接 PostgreSQL (psql)"
	@echo "  make shell-sr        - 连接 StarRocks (mysql)"
	@echo "  make query-pg        - 查询 PG 中的表"
	@echo "  make query-sr        - 查询 SR 中的表"
	@echo "  make pg-tables       - 显示 PG 所有表"
	@echo ""
	@echo "API 操作:"
	@echo "  make list-tables     - 列出所有可同步的表"
	@echo "  make enable-TABLE    - 启用表同步 (如: make enable-users)"
	@echo "  make disable-TABLE   - 禁用表同步 (如: make disable-users)"
	@echo "  make table-detail    - 查看表详情 (如: make table-detail TABLE=users)"
	@echo "  make api-status      - 查看 API 服务状态"
	@echo ""
	@echo "Flink 操作:"
	@echo "  make flink-status    - 查看 Flink 集群状态"
	@echo "  make flink-ui        - 打开 Flink UI"
	@echo ""
	@echo "示例:"
	@echo "  make up              # 启动环境"
	@echo "  make status          # 检查服务状态"
	@echo "  make list-tables     # 查看表列表"
	@echo "  make enable-users    # 启用 users 表同步"
	@echo ""

# === 容器管理 ===

up:
	@echo "▶️  启动所有容器..."
	docker-compose up -d
	@echo "✅ 容器启动完成"
	@echo "等待服务初始化中... (约 30 秒)"
	@sleep 30
	@make status

down:
	@echo "⏹️  停止所有容器..."
	docker-compose down

restart:
	@echo "🔄 重启所有容器..."
	docker-compose restart
	@sleep 5
	@make status

ps:
	@echo "📋 容器状态:"
	@docker-compose ps

clean:
	@echo "⚠️  删除所有容器和数据卷"
	@echo "这个操作将清除所有数据！"
	@read -p "确认? (y/N) " confirm && \
	if [ "$$confirm" = "y" ]; then \
		docker-compose down -v; \
		echo "✅ 清理完成"; \
	else \
		echo "❌ 已取消"; \
	fi

# === 日志查看 ===

logs:
	docker-compose logs -f

logs-api:
	docker-compose logs -f api-server

logs-flink:
	docker-compose logs -f jobmanager

logs-pg:
	docker-compose logs -f postgres

logs-sr:
	docker-compose logs -f starrocks-fe starrocks-be

# === 状态检查 ===

status:
	@echo "🔍 系统状态检查..."
	@echo ""
	@echo "1️⃣  容器状态:"
	@docker-compose ps --format "table {{.Names}}\t{{.Status}}"
	@echo ""
	@echo "2️⃣  API 服务状态:"
	@curl -s http://localhost:8888/api/status 2>/dev/null | jq '.data' || echo "❌ 无法连接 API"
	@echo ""
	@echo "3️⃣  Flink 状态:"
	@curl -s http://localhost:8081/api/overview 2>/dev/null | jq '{taskmanagers, slots_available, slots_total}' || echo "❌ 无法连接 Flink"
	@echo ""

# === 数据库连接 ===

shell-pg:
	docker-compose exec postgres psql -U postgres -d cdc_db

shell-sr:
	docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root

query-pg:
	docker-compose exec postgres psql -U postgres -d cdc_db -c "SELECT * FROM users LIMIT 5;"

query-sr:
	docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root cdc_db -e "SELECT * FROM users LIMIT 5;"

pg-tables:
	@echo "📊 PostgreSQL 表列表:"
	docker-compose exec postgres psql -U postgres -d cdc_db -c "\dt"

sr-databases:
	@echo "📊 StarRocks 数据库:"
	docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root -e "SHOW DATABASES;"

sr-tables:
	@echo "📊 StarRocks 表列表:"
	docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root cdc_db -e "SHOW TABLES;"

# === API 操作 ===

list-tables:
	@echo "📋 可同步的表列表:"
	@curl -s http://localhost:8888/api/tables | jq '.data.tables[] | {table_name, enabled, row_count, columns}'

enable-%:
	@echo "✅ 启用表同步: $*"
	@curl -s -X POST http://localhost:8888/api/tables/$*/enable | jq '.'

disable-%:
	@echo "❌ 禁用表同步: $*"
	@curl -s -X POST http://localhost:8888/api/tables/$*/disable | jq '.'

table-detail:
	@echo "📄 表详情: $(TABLE)"
	@curl -s http://localhost:8888/api/tables/$(TABLE) | jq '.data'

api-status:
	@echo "🔍 API 服务状态:"
	@curl -s http://localhost:8888/api/status | jq '.'

# === Flink 操作 ===

flink-status:
	@echo "Flink 集群状态:"
	@curl -s http://localhost:8081/api/overview | jq '.'

flink-ui:
	@echo "🌐 打开 Flink UI..."
	@open http://localhost:8081 2>/dev/null || xdg-open http://localhost:8081 || echo "请在浏览器中打开: http://localhost:8081"

starrocks-ui:
	@echo "🌐 打开 StarRocks UI..."
	@open http://localhost:8030 2>/dev/null || xdg-open http://localhost:8030 || echo "请在浏览器中打开: http://localhost:8030"

dashboard:
	@echo "🌐 打开管理仪表板..."
	@open dashboard.html 2>/dev/null || xdg-open dashboard.html || echo "请在浏览器中打开: file://$(PWD)/dashboard.html"

# === 快速演示 ===

demo:
	@echo "🚀 快速演示流程"
	@echo ""
	@echo "1️⃣  启动环境..."
	@make up
	@echo ""
	@echo "2️⃣  检查服务状态..."
	@make status
	@echo ""
	@echo "3️⃣  列出可同步的表..."
	@make list-tables
	@echo ""
	@echo "4️⃣  启用 users 表同步..."
	@make enable-users
	@echo ""
	@echo "5️⃣  打开管理界面..."
	@echo "请在浏览器中打开: file://$(PWD)/dashboard.html"
	@echo ""
	@echo "✨ 演示完成！"

# === 信息 ===

info:
	@echo "📚 Flink CDC PostgreSQL to StarRocks"
	@echo ""
	@echo "🔗 服务地址:"
	@echo "  PostgreSQL:      localhost:5432 (postgres/postgres)"
	@echo "  StarRocks FE:    http://localhost:8030 (root/empty)"
	@echo "  Flink UI:        http://localhost:8081"
	@echo "  API 服务:        http://localhost:8888"
	@echo "  Web 仪表板:      file://$(PWD)/dashboard.html"
	@echo ""
	@echo "📖 文档:"
	@echo "  README.md        - 完整说明"
	@echo "  QUICKSTART.md    - 快速开始"
	@echo "  CONFIG.md        - 配置说明"
	@echo "  ARCHITECTURE.md  - 架构设计"
	@echo ""
	@echo "🎯 常用操作:"
	@echo "  make up          - 启动环境"
	@echo "  make list-tables - 查看表"
	@echo "  make enable-XXXX - 启用表同步"
	@echo "  make status      - 检查状态"
	@echo ""

# === 测试命令 ===

test-pg-connection:
	@echo "测试 PostgreSQL 连接..."
	docker-compose exec postgres pg_isready -U postgres && echo "✅ 连接成功" || echo "❌ 连接失败"

test-sr-connection:
	@echo "测试 StarRocks 连接..."
	docker-compose exec starrocks-fe mysql -h 127.0.0.1 -P 9030 -u root -e "SELECT 1" > /dev/null && echo "✅ 连接成功" || echo "❌ 连接失败"

test-flink-connection:
	@echo "测试 Flink 连接..."
	curl -s http://localhost:8081/api/overview > /dev/null && echo "✅ 连接成功" || echo "❌ 连接失败"

test-api-connection:
	@echo "测试 API 连接..."
	curl -s http://localhost:8888/api/status > /dev/null && echo "✅ 连接成功" || echo "❌ 连接失败"

test-all:
	@echo "🧪 连接性测试..."
	@make test-pg-connection
	@make test-sr-connection
	@make test-flink-connection
	@make test-api-connection
	@echo "✅ 所有测试完成"
