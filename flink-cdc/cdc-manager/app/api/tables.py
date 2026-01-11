"""
表管理API
"""

from flask import Blueprint, jsonify, current_app
from services.database_service import DatabaseService
from services.config_service import ConfigService
import logging

tables_bp = Blueprint('tables', __name__)
logger = logging.getLogger(__name__)

@tables_bp.route('/', methods=['GET'])
def list_tables():
    """
    获取所有表列表
    包含表的基本信息和同步状态
    """
    try:
        db_service = DatabaseService(current_app.config)
        config_service = ConfigService(current_app.config)
        
        # 从PostgreSQL获取所有表
        pg_tables = db_service.get_all_pg_tables()
        
        # 获取配置文件中的表状态
        config = config_service.load_pipeline_config()
        config_tables = {t['table_name']: t for t in config.get('tables', [])}
        
        result = []
        for table_name in pg_tables:
            # 获取表的详细信息
            table_info = db_service.get_table_info(table_name)
            
            # 合并配置信息
            if table_name in config_tables:
                table_info.update(config_tables[table_name])
            else:
                # 新表默认未启用
                table_info.update({
                    'enabled': False,
                    'description': '',
                    'sink_table': table_name
                })
            
            result.append(table_info)
        
        enabled_count = sum(1 for t in result if t.get('enabled', False))
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'tables': result,
                'total': len(result),
                'enabled': enabled_count,
                'disabled': len(result) - enabled_count
            }
        })
    except Exception as e:
        logger.error(f"Failed to list tables: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to list tables: {str(e)}'
        }), 500

@tables_bp.route('/<table_name>', methods=['GET'])
def get_table_detail(table_name):
    """获取表的详细信息"""
    try:
        db_service = DatabaseService(current_app.config)
        config_service = ConfigService(current_app.config)
        
        # 获取表信息
        table_info = db_service.get_table_info(table_name)
        
        # 获取列信息
        columns = db_service.get_table_columns(table_name)
        table_info['column_details'] = columns
        
        # 获取配置
        config = config_service.load_pipeline_config()
        config_tables = {t['table_name']: t for t in config.get('tables', [])}
        
        if table_name in config_tables:
            table_info.update(config_tables[table_name])
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': table_info
        })
    except Exception as e:
        logger.error(f"Failed to get table detail: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get table detail: {str(e)}'
        }), 500

@tables_bp.route('/<table_name>/enable', methods=['POST'])
def enable_table(table_name):
    """启用表同步"""
    try:
        config_service = ConfigService(current_app.config)
        
        # 更新配置
        config_service.enable_table(table_name)
        
        logger.info(f"Table '{table_name}' enabled for sync")
        
        return jsonify({
            'code': 0,
            'message': f"Table '{table_name}' enabled successfully",
            'data': {'table_name': table_name, 'enabled': True}
        })
    except Exception as e:
        logger.error(f"Failed to enable table: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to enable table: {str(e)}'
        }), 500

@tables_bp.route('/<table_name>/disable', methods=['POST'])
def disable_table(table_name):
    """禁用表同步"""
    try:
        config_service = ConfigService(current_app.config)
        
        # 更新配置
        config_service.disable_table(table_name)
        
        logger.info(f"Table '{table_name}' disabled for sync")
        
        return jsonify({
            'code': 0,
            'message': f"Table '{table_name}' disabled successfully",
            'data': {'table_name': table_name, 'enabled': False}
        })
    except Exception as e:
        logger.error(f"Failed to disable table: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to disable table: {str(e)}'
        }), 500

@tables_bp.route('/<table_name>/sync-status', methods=['GET'])
def get_sync_status(table_name):
    """获取表的同步状态"""
    try:
        db_service = DatabaseService(current_app.config)
        
        # 获取源表和目标表的行数
        source_count = db_service.get_table_row_count(table_name)
        sink_count = db_service.get_starrocks_table_row_count(table_name)
        
        is_synced = source_count == sink_count
        difference = source_count - sink_count
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'table_name': table_name,
                'source_rows': source_count,
                'sink_rows': sink_count,
                'is_synced': is_synced,
                'difference': difference,
                'sync_percentage': (sink_count / source_count * 100) if source_count > 0 else 0
            }
        })
    except Exception as e:
        logger.error(f"Failed to get sync status: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get sync status: {str(e)}'
        }), 500
