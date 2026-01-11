#!/usr/bin/env python3
"""
Flink CDC 同步配置和管理 API 服务
支持：
1. 查看可同步的表列表
2. 启用/禁用表同步
3. 查看同步状态
4. 修改同步配置
"""

from flask import Flask, jsonify, request
from flask_cors import CORS
import json
import os
import psycopg2
from psycopg2.extras import RealDictCursor
import requests
from datetime import datetime
import logging

app = Flask(__name__)
CORS(app)

# 配置日志
logging.basicConfig(level=logging.INFO)
logger = logging.getLogger(__name__)

# 环境变量
POSTGRES_HOST = os.environ.get('POSTGRES_HOST', 'localhost')
POSTGRES_PORT = int(os.environ.get('POSTGRES_PORT', 5432))
STARROCKS_HOST = os.environ.get('STARROCKS_HOST', 'localhost')
STARROCKS_PORT = int(os.environ.get('STARROCKS_PORT', 9030))
FLINK_HOST = os.environ.get('FLINK_HOST', 'localhost')
FLINK_PORT = int(os.environ.get('FLINK_PORT', 8081))
CONFIG_FILE = os.environ.get('CONFIG_FILE', './sync-tables.json')

# 加载配置文件
def load_config():
    with open(CONFIG_FILE, 'r', encoding='utf-8') as f:
        return json.load(f)

def save_config(config):
    with open(CONFIG_FILE, 'w', encoding='utf-8') as f:
        json.dump(config, f, indent=2, ensure_ascii=False)

def get_postgres_connection():
    """获取 PostgreSQL 连接"""
    try:
        conn = psycopg2.connect(
            host=POSTGRES_HOST,
            port=POSTGRES_PORT,
            user='postgres',
            password='postgres',
            database='cdc_db'
        )
        return conn
    except Exception as e:
        logger.error(f"PostgreSQL 连接失败: {e}")
        return None

def get_all_tables():
    """从 PostgreSQL 获取所有表"""
    conn = get_postgres_connection()
    if not conn:
        return []
    
    try:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute("""
                SELECT tablename
                FROM pg_tables
                WHERE schemaname = 'public'
                ORDER BY tablename
            """)
            tables = [row['tablename'] for row in cur.fetchall()]
        return tables
    except Exception as e:
        logger.error(f"获取表列表失败: {e}")
        return []
    finally:
        conn.close()

def get_table_columns(table_name):
    """获取表的列信息"""
    conn = get_postgres_connection()
    if not conn:
        return []
    
    try:
        with conn.cursor(cursor_factory=RealDictCursor) as cur:
            cur.execute(f"""
                SELECT column_name, data_type, is_nullable
                FROM information_schema.columns
                WHERE table_name = '{table_name}'
                ORDER BY ordinal_position
            """)
            columns = cur.fetchall()
        return columns
    except Exception as e:
        logger.error(f"获取列信息失败: {e}")
        return []
    finally:
        conn.close()

def get_table_row_count(table_name):
    """获取表的行数"""
    conn = get_postgres_connection()
    if not conn:
        return 0
    
    try:
        with conn.cursor() as cur:
            cur.execute(f"SELECT COUNT(*) as count FROM {table_name}")
            count = cur.fetchone()[0]
        return count
    except Exception as e:
        logger.error(f"获取行数失败: {e}")
        return 0
    finally:
        conn.close()

@app.route('/', methods=['GET'])
def index():
    """根路由 - 返回 API 文档和 UI"""
    html = """
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Flink CDC 管理中心</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 40px 20px;
            }
            .container {
                max-width: 1000px;
                margin: 0 auto;
            }
            .header {
                text-align: center;
                color: white;
                margin-bottom: 40px;
            }
            .header h1 {
                font-size: 2.5em;
                margin-bottom: 10px;
            }
            .cards {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
                margin-bottom: 40px;
            }
            .card {
                background: white;
                border-radius: 10px;
                padding: 25px;
                box-shadow: 0 10px 30px rgba(0,0,0,0.2);
                transition: transform 0.3s, box-shadow 0.3s;
            }
            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 15px 40px rgba(0,0,0,0.3);
            }
            .card h2 {
                color: #667eea;
                margin-bottom: 15px;
                font-size: 1.3em;
            }
            .card p {
                color: #666;
                line-height: 1.6;
                margin-bottom: 15px;
            }
            .card a {
                display: inline-block;
                background: #667eea;
                color: white;
                padding: 10px 20px;
                border-radius: 5px;
                text-decoration: none;
                transition: background 0.3s;
            }
            .card a:hover {
                background: #764ba2;
            }
            .status-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                margin-top: 25px;
                padding: 20px;
                background: #f5f5f5;
                border-radius: 8px;
            }
            .status-item {
                text-align: center;
                padding: 15px;
                background: white;
                border-radius: 5px;
            }
            .status-label {
                color: #999;
                font-size: 0.9em;
                margin-bottom: 5px;
            }
            .status-value {
                font-size: 1.2em;
                font-weight: bold;
                color: #667eea;
            }
            .footer {
                text-align: center;
                color: white;
                margin-top: 40px;
                font-size: 0.9em;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>🔄 Flink CDC 管理中心</h1>
                <p>PostgreSQL → StarRocks 实时数据同步平台</p>
            </div>
            
            <div class="cards">
                <div class="card">
                    <h2>📊 表管理</h2>
                    <p>查看 PostgreSQL 所有表，选择要同步的表，配置同步策略</p>
                    <a href="/ui/tables">进入表管理 →</a>
                </div>
                
                <div class="card">
                    <h2>⚙️ 同步配置</h2>
                    <p>配置数据同步参数，设置同步模式和目标表映射</p>
                    <a href="/ui/config">进入同步配置 →</a>
                </div>
                
                <div class="card">
                    <h2>📈 监控面板</h2>
                    <p>实时查看同步状态、性能指标和错误日志</p>
                    <a href="/ui/monitor">进入监控面板 →</a>
                </div>
            </div>
            
            <div class="card">
                <h2>🚀 快速开始</h2>
                <p><strong>API 基础地址:</strong> <code>http://localhost:8888/api/</code></p>
                
                <div class="status-grid">
                    <div class="status-item">
                        <div class="status-label">系统状态</div>
                        <div class="status-value" id="system-status">加载中...</div>
                    </div>
                    <div class="status-item">
                        <div class="status-label">可同步表数</div>
                        <div class="status-value" id="tables-count">加载中...</div>
                    </div>
                    <div class="status-item">
                        <div class="status-label">已启用同步</div>
                        <div class="status-value" id="enabled-count">加载中...</div>
                    </div>
                </div>
                
                <h3 style="margin-top: 25px; color: #333; font-size: 1.1em;">常用 API：</h3>
                <ul style="margin-top: 15px; list-style: none; color: #666;">
                    <li>📍 <code>GET /api/tables</code> - 获取所有表</li>
                    <li>📍 <code>GET /api/status</code> - 获取系统状态</li>
                    <li>📍 <code>POST /api/tables/{table_name}/enable</code> - 启用表同步</li>
                    <li>📍 <code>POST /api/tables/{table_name}/disable</code> - 禁用表同步</li>
                    <li>📍 <code>GET /api/sync-config</code> - 获取同步配置</li>
                    <li>📍 <code>GET /api/flink/status</code> - 获取 Flink 状态</li>
                </ul>
            </div>
            
            <div class="footer">
                <p>© 2024 Flink CDC 数据同步平台 | <a href="/api/status" style="color: white;">系统状态</a></p>
            </div>
        </div>
        
        <script>
            // 加载系统状态
            fetch('/api/status')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        const d = data.data;
                        document.getElementById('system-status').textContent = 
                            d.postgres === 'connected' && d.flink === 'running' && d.starrocks === 'running' 
                            ? '✅ 正常' : '⚠️ 异常';
                    }
                });
            
            // 加载表信息
            fetch('/api/tables')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        document.getElementById('tables-count').textContent = data.data.total;
                        document.getElementById('enabled-count').textContent = data.data.enabled_count;
                    }
                });
        </script>
    </body>
    </html>
    """
    return html, 200, {'Content-Type': 'text/html; charset=utf-8'}

@app.route('/ui/tables', methods=['GET'])
def ui_tables():
    """表管理 UI"""
    html = """
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>表管理 - Flink CDC</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f5f5;
                padding: 20px;
            }
            .container { max-width: 1200px; margin: 0 auto; }
            .header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .header h1 { color: #667eea; margin-bottom: 10px; }
            table {
                width: 100%;
                background: white;
                border-collapse: collapse;
                border-radius: 8px;
                overflow: hidden;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            th {
                background: #667eea;
                color: white;
                padding: 15px;
                text-align: left;
                font-weight: 600;
            }
            td {
                padding: 12px 15px;
                border-bottom: 1px solid #eee;
            }
            tr:hover { background: #f9f9f9; }
            .toggle {
                width: 50px;
                height: 24px;
                background: #ccc;
                border-radius: 12px;
                position: relative;
                cursor: pointer;
                transition: background 0.3s;
            }
            .toggle.enabled { background: #4caf50; }
            .toggle::after {
                content: '';
                position: absolute;
                width: 20px;
                height: 20px;
                background: white;
                border-radius: 50%;
                top: 2px;
                left: 2px;
                transition: left 0.3s;
            }
            .toggle.enabled::after { left: 28px; }
            .action-btn {
                padding: 6px 12px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                margin-right: 5px;
                transition: background 0.3s;
            }
            .btn-edit { background: #2196F3; color: white; }
            .btn-edit:hover { background: #0b7dda; }
            .loading { text-align: center; padding: 40px; color: #999; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>📊 表管理</h1>
                <p>选择要同步到 StarRocks 的表</p>
            </div>
            <div id="content" class="loading">加载中...</div>
        </div>
        
        <script>
            function loadTables() {
                fetch('/api/tables')
                    .then(r => r.json())
                    .then(data => {
                        console.log('API Response:', data);
                        if (data.code === 0 && data.data && data.data.tables) {
                            let html = '<table><thead><tr><th>表名</th><th>行数</th><th>列数</th><th>描述</th><th>同步状态</th><th>操作</th></tr></thead><tbody>';
                            data.data.tables.forEach(t => {
                                const toggleClass = t.enabled ? 'toggle enabled' : 'toggle';
                                html += '<tr><td>' + (t.table_name || '') + '</td><td>' + (t.row_count || 0) + '</td><td>' + (t.columns || 0) + '</td>';
                                html += '<td>' + (t.description || '') + '</td>';
                                html += '<td><div class="' + toggleClass + '" onclick="toggleTable(\'' + t.table_name + '\', this)"></div></td>';
                                html += '<td><button class="action-btn btn-edit" onclick="viewDetail(\'' + t.table_name + '\')">详情</button></td></tr>';
                            });
                            html += '</tbody></table>';
                            document.getElementById('content').innerHTML = html;
                        } else {
                            document.getElementById('content').innerHTML = '<p style="color: red;">加载失败: ' + (data.message || 'Unknown error') + '</p>';
                        }
                    })
                    .catch(err => {
                        console.error('Fetch error:', err);
                        document.getElementById('content').innerHTML = '<p style="color: red;">加载失败: ' + err.message + '</p>';
                    });
            }
            
            function toggleTable(tableName, elem) {
                const enabled = !elem.classList.contains('enabled');
                const endpoint = enabled ? 'enable' : 'disable';
                fetch('/api/tables/' + tableName + '/' + endpoint, {method: 'POST'})
                    .then(r => r.json())
                    .then(d => {
                        if (d.code === 0) {
                            elem.classList.toggle('enabled');
                            alert(d.message || '操作成功');
                        } else {
                            alert('操作失败: ' + d.message);
                        }
                    })
                    .catch(err => {
                        alert('操作失败: ' + err.message);
                    });
            }
            
            function viewDetail(tableName) {
                fetch('/api/tables/' + tableName)
                    .then(r => r.json())
                    .then(d => {
                        if (d.code === 0) {
                            const data = d.data;
                            let msg = '表名: ' + data.table_name + '\n';
                            msg += '行数: ' + data.row_count + '\n';
                            msg += '列数: ' + data.columns + '\n';
                            msg += '列信息: ' + JSON.stringify(data.columns || []).substring(0, 100);
                            alert(msg);
                        }
                    });
            }
            
            // 页面加载时获取表列表
            loadTables();
        </script>
    </body>
    </html>
    """
    return html, 200, {'Content-Type': 'text/html; charset=utf-8'}

@app.route('/ui/config', methods=['GET'])
def ui_config():
    """同步配置 UI"""
    html = """
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>同步配置 - Flink CDC</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f5f5;
                padding: 20px;
            }
            .container { max-width: 1200px; margin: 0 auto; }
            .header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .header h1 { color: #667eea; margin-bottom: 10px; }
            .header a { color: #667eea; text-decoration: none; margin-left: 20px; }
            .config-section {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .config-section h2 { color: #333; font-size: 1.2em; margin-bottom: 15px; border-bottom: 2px solid #667eea; padding-bottom: 10px; }
            .config-item { margin-bottom: 15px; }
            .config-item label { display: block; color: #666; margin-bottom: 5px; font-weight: 500; }
            .config-item input, .config-item textarea {
                width: 100%;
                padding: 8px 12px;
                border: 1px solid #ddd;
                border-radius: 4px;
                font-family: monospace;
                font-size: 0.95em;
            }
            .config-item textarea { min-height: 100px; }
            .btn {
                background: #667eea;
                color: white;
                padding: 10px 20px;
                border: none;
                border-radius: 4px;
                cursor: pointer;
                margin-top: 10px;
                transition: background 0.3s;
            }
            .btn:hover { background: #764ba2; }
            .loading { text-align: center; padding: 40px; color: #999; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>⚙️ 同步配置</h1>
                <p>配置数据同步参数 <a href="/">← 返回首页</a></p>
            </div>
            <div id="content" class="loading">加载中...</div>
        </div>
        
        <script>
            fetch('/api/sync-config')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        const cfg = data.data;
                        let html = '<div class="config-section">';
                        html += '<h2>PostgreSQL 源</h2>';
                        html += '<div class="config-item"><label>主机:</label><input type="text" value="' + cfg.postgres.host + '" readonly></div>';
                        html += '<div class="config-item"><label>端口:</label><input type="text" value="' + cfg.postgres.port + '" readonly></div>';
                        html += '<div class="config-item"><label>数据库:</label><input type="text" value="' + cfg.postgres.database + '" readonly></div>';
                        html += '</div>';
                        
                        html += '<div class="config-section">';
                        html += '<h2>StarRocks 目标</h2>';
                        html += '<div class="config-item"><label>主机:</label><input type="text" value="' + cfg.starrocks.host + '" readonly></div>';
                        html += '<div class="config-item"><label>端口:</label><input type="text" value="' + cfg.starrocks.port + '" readonly></div>';
                        html += '</div>';
                        
                        html += '<div class="config-section">';
                        html += '<h2>同步选项</h2>';
                        html += '<div class="config-item"><label>启动模式:</label><input type="text" value="' + (cfg.sync_config.options["scan.startup.mode"] || "initial") + '" readonly></div>';
                        html += '<div class="config-item"><label>块大小:</label><input type="text" value="' + (cfg.sync_config.options["chunk.size"] || "8096") + '" readonly></div>';
                        html += '<div class="config-item"><label>启用增量快照:</label><input type="text" value="' + (cfg.sync_config.options["scan.incremental.snapshot.enabled"] ? "是" : "否") + '" readonly></div>';
                        html += '</div>';
                        
                        document.getElementById('content').innerHTML = html;
                    }
                });
        </script>
    </body>
    </html>
    """
    return html, 200, {'Content-Type': 'text/html; charset=utf-8'}

@app.route('/ui/monitor', methods=['GET'])
def ui_monitor():
    """监控面板 UI"""
    html = """
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>监控面板 - Flink CDC</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body {
                font-family: 'Segoe UI', Tahoma, Geneva, Verdana, sans-serif;
                background: #f5f5f5;
                padding: 20px;
            }
            .container { max-width: 1200px; margin: 0 auto; }
            .header {
                background: white;
                padding: 20px;
                border-radius: 8px;
                margin-bottom: 20px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .header h1 { color: #667eea; margin-bottom: 10px; }
            .header a { color: #667eea; text-decoration: none; margin-left: 20px; }
            .status-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(250px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            .status-card {
                background: white;
                padding: 20px;
                border-radius: 8px;
                box-shadow: 0 2px 8px rgba(0,0,0,0.1);
            }
            .status-card h3 { color: #667eea; margin-bottom: 15px; }
            .status-item { display: flex; justify-content: space-between; padding: 8px 0; border-bottom: 1px solid #eee; }
            .status-item label { color: #666; }
            .status-item .value { font-weight: bold; color: #333; }
            .status-item .value.ok { color: #4caf50; }
            .status-item .value.error { color: #f44336; }
            .loading { text-align: center; padding: 40px; color: #999; }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>📈 监控面板</h1>
                <p>实时查看同步状态 <a href="/">← 返回首页</a></p>
            </div>
            
            <div class="status-grid">
                <div class="status-card" id="system-card">
                    <h3>系统状态</h3>
                    <div class="loading">加载中...</div>
                </div>
                <div class="status-card" id="flink-card">
                    <h3>Flink 集群</h3>
                    <div class="loading">加载中...</div>
                </div>
                <div class="status-card" id="tables-card">
                    <h3>表同步统计</h3>
                    <div class="loading">加载中...</div>
                </div>
            </div>
        </div>
        
        <script>
            // 加载系统状态
            fetch('/api/status')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        const d = data.data;
                        const pgClass = d.postgres === 'connected' ? 'ok' : 'error';
                        const flinkClass = d.flink === 'running' ? 'ok' : 'error';
                        const srClass = d.starrocks === 'running' ? 'ok' : 'error';
                        let html = '<div class="status-item"><label>PostgreSQL</label><span class="value ' + pgClass + '">' + d.postgres + '</span></div>';
                        html += '<div class="status-item"><label>Flink</label><span class="value ' + flinkClass + '">' + d.flink + '</span></div>';
                        html += '<div class="status-item"><label>StarRocks</label><span class="value ' + srClass + '">' + d.starrocks + '</span></div>';
                        document.getElementById('system-card').innerHTML = '<h3>系统状态</h3>' + html;
                    }
                });
            
            // 加载 Flink 状态
            fetch('/api/flink/status')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        const d = data.data;
                        let html = '<div class="status-item"><label>状态</label><span class="value ok">' + d.status + '</span></div>';
                        html += '<div class="status-item"><label>TaskManager</label><span class="value">' + d.taskmanagers + '</span></div>';
                        html += '<div class="status-item"><label>可用槽位</label><span class="value">' + d.slots_available + '/' + d.slots_total + '</span></div>';
                        html += '<div class="status-item"><label>运行任务</label><span class="value">' + d.jobs_running + '</span></div>';
                        document.getElementById('flink-card').innerHTML = '<h3>Flink 集群</h3>' + html;
                    }
                });
            
            // 加载表统计
            fetch('/api/tables')
                .then(r => r.json())
                .then(data => {
                    if (data.code === 0) {
                        const d = data.data;
                        let html = '<div class="status-item"><label>总表数</label><span class="value">' + d.total + '</span></div>';
                        html += '<div class="status-item"><label>已启用</label><span class="value ok">' + d.enabled_count + '</span></div>';
                        html += '<div class="status-item"><label>未启用</label><span class="value">' + (d.total - d.enabled_count) + '</span></div>';
                        document.getElementById('tables-card').innerHTML = '<h3>表同步统计</h3>' + html;
                    }
                });
        </script>
    </body>
    </html>
    """
    return html, 200, {'Content-Type': 'text/html; charset=utf-8'}

@app.route('/health', methods=['GET'])
def health():
    """健康检查"""
    return jsonify({'status': 'healthy', 'timestamp': datetime.now().isoformat()})

@app.route('/api/tables', methods=['GET'])
def list_tables():
    """
    获取所有表的列表
    返回：所有可同步的表及其同步状态
    """
    try:
        config = load_config()
        all_pg_tables = get_all_tables()
        sync_config = {table['table_name']: table for table in config['sync_config']['tables']}
        
        result = []
        for table_name in all_pg_tables:
            if table_name in sync_config:
                table_info = sync_config[table_name]
                table_info['row_count'] = get_table_row_count(table_name)
                table_info['columns'] = len(get_table_columns(table_name))
                result.append(table_info)
            else:
                # 新增表自动加入配置
                table_info = {
                    'table_name': table_name,
                    'enabled': False,
                    'target_table': table_name,
                    'sync_mode': 'full_and_incremental',
                    'description': '',
                    'row_count': get_table_row_count(table_name),
                    'columns': len(get_table_columns(table_name)),
                    'created_at': datetime.now().isoformat()
                }
                result.append(table_info)
                # 自动保存到配置
                config['sync_config']['tables'].append(table_info)
        
        save_config(config)
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'tables': result,
                'total': len(result),
                'enabled_count': sum(1 for t in result if t['enabled'])
            }
        })
    except Exception as e:
        logger.error(f"获取表列表失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/tables/<table_name>', methods=['GET'])
def get_table_detail(table_name):
    """获取表的详细信息"""
    try:
        columns = get_table_columns(table_name)
        row_count = get_table_row_count(table_name)
        config = load_config()
        sync_config = next((t for t in config['sync_config']['tables'] if t['table_name'] == table_name), None)
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'table_name': table_name,
                'columns': columns,
                'row_count': row_count,
                'sync_config': sync_config
            }
        })
    except Exception as e:
        logger.error(f"获取表详情失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/tables/<table_name>/enable', methods=['POST'])
def enable_table(table_name):
    """启用表同步"""
    try:
        config = load_config()
        
        # 查找或创建表配置
        table_config = next((t for t in config['sync_config']['tables'] if t['table_name'] == table_name), None)
        
        if not table_config:
            table_config = {
                'table_name': table_name,
                'target_table': table_name,
                'sync_mode': 'full_and_incremental',
                'description': ''
            }
            config['sync_config']['tables'].append(table_config)
        
        table_config['enabled'] = True
        save_config(config)
        
        logger.info(f"启用表同步: {table_name}")
        
        return jsonify({
            'code': 0,
            'message': f'表 {table_name} 同步已启用',
            'data': table_config
        })
    except Exception as e:
        logger.error(f"启用表同步失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/tables/<table_name>/disable', methods=['POST'])
def disable_table(table_name):
    """禁用表同步"""
    try:
        config = load_config()
        table_config = next((t for t in config['sync_config']['tables'] if t['table_name'] == table_name), None)
        
        if table_config:
            table_config['enabled'] = False
            save_config(config)
            logger.info(f"禁用表同步: {table_name}")
        
        return jsonify({
            'code': 0,
            'message': f'表 {table_name} 同步已禁用',
            'data': table_config
        })
    except Exception as e:
        logger.error(f"禁用表同步失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/sync-config', methods=['GET'])
def get_sync_config():
    """获取同步配置"""
    try:
        config = load_config()
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': config
        })
    except Exception as e:
        logger.error(f"获取配置失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/sync-config', methods=['PUT'])
def update_sync_config():
    """更新同步配置"""
    try:
        config = load_config()
        updates = request.json
        
        # 更新 sync_config 中的指定字段
        if 'sync_config' in updates:
            for key, value in updates['sync_config'].items():
                config['sync_config'][key] = value
        
        save_config(config)
        logger.info("同步配置已更新")
        
        return jsonify({
            'code': 0,
            'message': '配置已更新',
            'data': config
        })
    except Exception as e:
        logger.error(f"更新配置失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

@app.route('/api/flink/status', methods=['GET'])
def get_flink_status():
    """获取 Flink 集群状态"""
    try:
        response = requests.get(f'http://{FLINK_HOST}:{FLINK_PORT}/api/overview', timeout=5)
        if response.status_code == 200:
            data = response.json()
            return jsonify({
                'code': 0,
                'message': 'success',
                'data': {
                    'status': 'running',
                    'taskmanagers': data.get('taskmanagers', 0),
                    'slots_available': data.get('slots_available', 0),
                    'slots_total': data.get('slots_total', 0),
                    'jobs_running': data.get('jobs-running', 0)
                }
            })
        else:
            return jsonify({'code': 1, 'message': 'Flink 不可用'}), 503
    except Exception as e:
        logger.error(f"获取 Flink 状态失败: {e}")
        return jsonify({'code': 1, 'message': '无法连接到 Flink'}), 503

@app.route('/api/status', methods=['GET'])
def get_status():
    """获取整个系统的状态"""
    try:
        pg_conn = get_postgres_connection()
        pg_status = 'connected' if pg_conn else 'disconnected'
        if pg_conn:
            pg_conn.close()
        
        flink_response = requests.get(f'http://{FLINK_HOST}:{FLINK_PORT}/api/overview', timeout=3)
        flink_status = 'running' if flink_response.status_code == 200 else 'unavailable'
        
        sr_response = requests.get(f'http://{STARROCKS_HOST}:8030/api/bootstrap', timeout=3)
        sr_status = 'running' if sr_response.status_code == 200 else 'unavailable'
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'postgres': pg_status,
                'flink': flink_status,
                'starrocks': sr_status,
                'timestamp': datetime.now().isoformat()
            }
        })
    except Exception as e:
        logger.error(f"获取系统状态失败: {e}")
        return jsonify({'code': 1, 'message': str(e)}), 500

if __name__ == '__main__':
    app.run(host='0.0.0.0', port=8888, debug=False)
