"""
API路由注册
"""

from flask import jsonify
from .health import health_bp
from .tables import tables_bp
from .pipeline import pipeline_bp
from .system import system_bp
from .ui import ui_bp

def register_routes(app):
    """注册所有API路由"""
    
    # 健康检查
    app.register_blueprint(health_bp)
    
    # 表管理
    app.register_blueprint(tables_bp, url_prefix='/api/tables')
    
    # Pipeline管理
    app.register_blueprint(pipeline_bp, url_prefix='/api/pipeline')
    
    # 系统状态
    app.register_blueprint(system_bp, url_prefix='/api/system')
    
    # Web UI
    app.register_blueprint(ui_bp, url_prefix='/ui')
    
    # 根路径
    @app.route('/')
    def index():
        return jsonify({
            'service': 'Flink CDC Manager',
            'version': '2.0.0',
            'endpoints': {
                'health': '/health',
                'tables': '/api/tables',
                'pipeline': '/api/pipeline',
                'system': '/api/system',
                'web_ui': '/ui'
            }
        })
    
    # 错误处理
    @app.errorhandler(404)
    def not_found(error):
        return jsonify({'code': 404, 'message': 'Not Found'}), 404
    
    @app.errorhandler(500)
    def internal_error(error):
        return jsonify({'code': 500, 'message': 'Internal Server Error'}), 500
