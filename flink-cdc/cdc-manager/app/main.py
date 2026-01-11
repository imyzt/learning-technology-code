#!/usr/bin/env python3
"""
Flink CDC Manager - 主应用入口
完整的CDC管理和监控API服务
"""

import os
import sys
import logging
from flask import Flask
from flask_cors import CORS

# 配置日志
logging.basicConfig(
    level=logging.INFO,
    format='%(asctime)s - %(name)s - %(levelname)s - %(message)s',
    handlers=[
        logging.StreamHandler(sys.stdout),
        logging.FileHandler('/var/log/cdc-manager/app.log')
    ]
)

logger = logging.getLogger(__name__)

# 创建Flask应用
app = Flask(__name__)
CORS(app)

# 加载配置
app.config['POSTGRES_HOST'] = os.getenv('POSTGRES_HOST', 'postgres')
app.config['POSTGRES_PORT'] = int(os.getenv('POSTGRES_PORT', 5432))
app.config['POSTGRES_DB'] = os.getenv('POSTGRES_DB', 'cdc_db')
app.config['POSTGRES_USER'] = os.getenv('POSTGRES_USER', 'postgres')
app.config['POSTGRES_PASSWORD'] = os.getenv('POSTGRES_PASSWORD', 'postgres')

app.config['STARROCKS_HOST'] = os.getenv('STARROCKS_HOST', 'starrocks')
app.config['STARROCKS_QUERY_PORT'] = int(os.getenv('STARROCKS_QUERY_PORT', 9030))
app.config['STARROCKS_FE_HTTP_PORT'] = int(os.getenv('STARROCKS_FE_HTTP_PORT', 8030))
app.config['STARROCKS_USER'] = os.getenv('STARROCKS_USER', 'root')
app.config['STARROCKS_PASSWORD'] = os.getenv('STARROCKS_PASSWORD', '')

app.config['FLINK_CDC_HOST'] = os.getenv('FLINK_CDC_HOST', 'flink-cdc-runtime')
app.config['FLINK_WEB_UI_PORT'] = int(os.getenv('FLINK_WEB_UI_PORT', 8081))

app.config['PIPELINE_CONFIG_DIR'] = os.getenv('PIPELINE_CONFIG_DIR', '/opt/cdc-pipelines')

# 注册路由
from api.routes import register_routes
register_routes(app)

if __name__ == '__main__':
    logger.info("Starting Flink CDC Manager API Service...")
    logger.info(f"Pipeline Config Dir: {app.config['PIPELINE_CONFIG_DIR']}")
    logger.info(f"PostgreSQL: {app.config['POSTGRES_HOST']}:{app.config['POSTGRES_PORT']}")
    logger.info(f"StarRocks: {app.config['STARROCKS_HOST']}:{app.config['STARROCKS_QUERY_PORT']}")
    logger.info(f"Flink: {app.config['FLINK_CDC_HOST']}:{app.config['FLINK_WEB_UI_PORT']}")
    
    # 使用gunicorn时不需要这段，直接用于开发
    app.run(host='0.0.0.0', port=8888, debug=False)
