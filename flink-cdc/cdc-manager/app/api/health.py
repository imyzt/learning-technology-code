"""
健康检查API
"""

from flask import Blueprint, jsonify
from datetime import datetime

health_bp = Blueprint('health', __name__)

@health_bp.route('/health', methods=['GET'])
def health_check():
    """健康检查端点"""
    return jsonify({
        'status': 'healthy',
        'timestamp': datetime.now().isoformat(),
        'service': 'Flink CDC Manager'
    })

@health_bp.route('/ready', methods=['GET'])
def readiness():
    """就绪检查"""
    # 可以添加更复杂的检查逻辑
    return jsonify({
        'ready': True,
        'timestamp': datetime.now().isoformat()
    })

@health_bp.route('/live', methods=['GET'])
def liveness():
    """存活检查"""
    return jsonify({
        'alive': True,
        'timestamp': datetime.now().isoformat()
    })
