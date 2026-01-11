"""
Web UI路由
"""

from flask import Blueprint, render_template_string, jsonify

ui_bp = Blueprint('ui', __name__)

@ui_bp.route('/')
def index():
    """Web UI首页"""
    html = """
    <!DOCTYPE html>
    <html lang="zh-CN">
    <head>
        <meta charset="UTF-8">
        <meta name="viewport" content="width=device-width, initial-scale=1.0">
        <title>Flink CDC Manager</title>
        <style>
            * { margin: 0; padding: 0; box-sizing: border-box; }
            body {
                font-family: -apple-system, BlinkMacSystemFont, 'Segoe UI', Roboto, sans-serif;
                background: linear-gradient(135deg, #667eea 0%, #764ba2 100%);
                min-height: 100vh;
                padding: 20px;
            }
            .container { max-width: 1200px; margin: 0 auto; }
            .header {
                background: white;
                padding: 30px;
                border-radius: 12px;
                box-shadow: 0 10px 40px rgba(0,0,0,0.2);
                margin-bottom: 30px;
                text-align: center;
            }
            .header h1 { color: #667eea; font-size: 2.5em; margin-bottom: 10px; }
            .header p { color: #666; font-size: 1.1em; }
            .cards {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(300px, 1fr));
                gap: 20px;
                margin-bottom: 30px;
            }
            .card {
                background: white;
                padding: 25px;
                border-radius: 12px;
                box-shadow: 0 8px 30px rgba(0,0,0,0.15);
                transition: transform 0.3s, box-shadow 0.3s;
            }
            .card:hover {
                transform: translateY(-5px);
                box-shadow: 0 12px 40px rgba(0,0,0,0.25);
            }
            .card h2 { color: #333; font-size: 1.3em; margin-bottom: 15px; }
            .card p { color: #666; line-height: 1.6; margin-bottom: 20px; }
            .btn {
                display: inline-block;
                background: linear-gradient(135deg, #667eea, #764ba2);
                color: white;
                padding: 12px 24px;
                border-radius: 6px;
                text-decoration: none;
                transition: opacity 0.3s;
                border: none;
                cursor: pointer;
                font-size: 1em;
            }
            .btn:hover { opacity: 0.9; }
            .status-grid {
                display: grid;
                grid-template-columns: repeat(auto-fit, minmax(200px, 1fr));
                gap: 15px;
                margin-top: 20px;
            }
            .status-item {
                background: #f8f9fa;
                padding: 15px;
                border-radius: 8px;
                text-align: center;
            }
            .status-label { color: #666; font-size: 0.9em; margin-bottom: 8px; }
            .status-value { font-size: 1.4em; font-weight: bold; color: #667eea; }
            .status-value.ok { color: #10b981; }
            .status-value.error { color: #ef4444; }
            .footer {
                text-align: center;
                color: white;
                margin-top: 40px;
                padding: 20px;
            }
        </style>
    </head>
    <body>
        <div class="container">
            <div class="header">
                <h1>🔄 Flink CDC Manager</h1>
                <p>PostgreSQL → StarRocks 实时数据同步平台 v2.0</p>
            </div>
            
            <div class="cards">
                <div class="card">
                    <h2>📊 表管理</h2>
                    <p>查看数据库表，选择需要同步的表，管理同步策略</p>
                    <button class="btn" onclick="location.href='/ui/tables'">进入管理</button>
                </div>
                
                <div class="card">
                    <h2>⚙️ Pipeline配置</h2>
                    <p>生成和管理Flink CDC Pipeline配置，提交同步作业</p>
                    <button class="btn" onclick="location.href='/ui/pipeline'">配置Pipeline</button>
                </div>
                
                <div class="card">
                    <h2>📈 监控面板</h2>
                    <p>实时查看同步状态、作业运行情况和性能指标</p>
                    <button class="btn" onclick="location.href='/ui/monitor'">查看监控</button>
                </div>
            </div>
            
            <div class="card">
                <h2>🚀 系统状态</h2>
                <div class="status-grid" id="status-grid">
                    <div class="status-item">
                        <div class="status-label">PostgreSQL</div>
                        <div class="status-value" id="pg-status">检查中...</div>
                    </div>
                    <div class="status-item">
                        <div class="status-label">StarRocks</div>
                        <div class="status-value" id="sr-status">检查中...</div>
                    </div>
                    <div class="status-item">
                        <div class="status-label">Flink集群</div>
                        <div class="status-value" id="flink-status">检查中...</div>
                    </div>
                    <div class="status-item">
                        <div class="status-label">运行作业</div>
                        <div class="status-value" id="jobs-count">0</div>
                    </div>
                </div>
            </div>
            
            <div class="footer">
                <p>&copy; 2026 Flink CDC Manager | 
                   <a href="http://localhost:8081" target="_blank" style="color: white;">Flink UI</a> | 
                   <a href="/api/system/status" style="color: white;">API Status</a>
                </p>
            </div>
        </div>
        
        <script>
            // 加载系统状态
            async function loadStatus() {
                try {
                    const res = await fetch('/api/system/status');
                    const data = await res.json();
                    
                    if (data.code === 0) {
                        const d = data.data;
                        document.getElementById('pg-status').textContent = 
                            d.postgres.connected ? '✅ 正常' : '❌ 异常';
                        document.getElementById('pg-status').className = 
                            'status-value ' + (d.postgres.connected ? 'ok' : 'error');
                        
                        document.getElementById('sr-status').textContent = 
                            d.starrocks.connected ? '✅ 正常' : '❌ 异常';
                        document.getElementById('sr-status').className = 
                            'status-value ' + (d.starrocks.connected ? 'ok' : 'error');
                        
                        document.getElementById('flink-status').textContent = 
                            d.flink.available ? '✅ 运行中' : '❌ 不可用';
                        document.getElementById('flink-status').className = 
                            'status-value ' + (d.flink.available ? 'ok' : 'error');
                    }
                } catch (err) {
                    console.error('Failed to load status:', err);
                }
                
                // 加载作业数量
                try {
                    const res = await fetch('/api/pipeline/jobs');
                    const data = await res.json();
                    
                    if (data.code === 0) {
                        const running = data.data.jobs.filter(j => j.state === 'RUNNING').length;
                        document.getElementById('jobs-count').textContent = running;
                    }
                } catch (err) {
                    console.error('Failed to load jobs:', err);
                }
            }
            
            // 页面加载时执行
            loadStatus();
            
            // 每30秒刷新一次
            setInterval(loadStatus, 30000);
        </script>
    </body>
    </html>
    """
    return html

@ui_bp.route('/tables')
def tables_ui():
    """表管理UI"""
    return render_template_string("Tables UI - 开发中")

@ui_bp.route('/pipeline')
def pipeline_ui():
    """Pipeline配置UI"""
    return render_template_string("Pipeline UI - 开发中")

@ui_bp.route('/monitor')
def monitor_ui():
    """监控面板UI"""
    return render_template_string("Monitor UI - 开发中")
