"""
系统状态API
"""

from flask import Blueprint, jsonify, current_app
from services.database_service import DatabaseService
from services.pipeline_service import PipelineService
import logging

system_bp = Blueprint('system', __name__)
logger = logging.getLogger(__name__)

@system_bp.route('/status', methods=['GET'])
def get_system_status():
    """获取整个系统的状态"""
    try:
        db_service = DatabaseService(current_app.config)
        pipeline_service = PipelineService(current_app.config)
        
        # 检查PostgreSQL
        pg_status = db_service.check_postgres_connection()
        
        # 检查StarRocks
        sr_status = db_service.check_starrocks_connection()
        
        # 检查Flink
        flink_status = pipeline_service.check_flink_status()
        
        # 整体状态
        all_healthy = pg_status['connected'] and sr_status['connected'] and flink_status['available']
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'healthy': all_healthy,
                'postgres': pg_status,
                'starrocks': sr_status,
                'flink': flink_status
            }
        })
    except Exception as e:
        logger.error(f"Failed to get system status: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get system status: {str(e)}'
        }), 500

@system_bp.route('/flink', methods=['GET'])
def get_flink_status():
    """获取Flink集群状态"""
    try:
        pipeline_service = PipelineService(current_app.config)
        
        flink_info = pipeline_service.get_flink_overview()
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': flink_info
        })
    except Exception as e:
        logger.error(f"Failed to get Flink status: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get Flink status: {str(e)}'
        }), 500

@system_bp.route('/metrics', methods=['GET'])
def get_metrics():
    """获取系统指标"""
    try:
        db_service = DatabaseService(current_app.config)
        
        # 获取表统计
        pg_tables = db_service.get_all_pg_tables()
        total_rows = sum(db_service.get_table_row_count(t) for t in pg_tables)
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'total_tables': len(pg_tables),
                'total_rows': total_rows,
                'timestamp': db_service.get_current_timestamp()
            }
        })
    except Exception as e:
        logger.error(f"Failed to get metrics: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get metrics: {str(e)}'
        }), 500
