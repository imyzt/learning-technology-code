"""
Pipeline管理API
"""

from flask import Blueprint, jsonify, request, current_app
from services.pipeline_service import PipelineService
from services.config_service import ConfigService
import logging

pipeline_bp = Blueprint('pipeline', __name__)
logger = logging.getLogger(__name__)

@pipeline_bp.route('/config', methods=['GET'])
def get_pipeline_config():
    """获取当前Pipeline配置"""
    try:
        config_service = ConfigService(current_app.config)
        config = config_service.load_pipeline_config()
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': config
        })
    except Exception as e:
        logger.error(f"Failed to get pipeline config: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get pipeline config: {str(e)}'
        }), 500

@pipeline_bp.route('/generate', methods=['POST'])
def generate_pipeline():
    """
    根据当前配置生成Pipeline YAML文件
    """
    try:
        config_service = ConfigService(current_app.config)
        pipeline_service = PipelineService(current_app.config)
        
        # 加载配置
        config = config_service.load_pipeline_config()
        
        # 生成Pipeline
        pipeline_yaml = pipeline_service.generate_pipeline_yaml(config)
        
        # 保存Pipeline文件
        pipeline_file = pipeline_service.save_pipeline(pipeline_yaml, 'generated-pipeline.yaml')
        
        return jsonify({
            'code': 0,
            'message': 'Pipeline generated successfully',
            'data': {
                'pipeline_file': pipeline_file,
                'pipeline_yaml': pipeline_yaml
            }
        })
    except Exception as e:
        logger.error(f"Failed to generate pipeline: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to generate pipeline: {str(e)}'
        }), 500

@pipeline_bp.route('/submit', methods=['POST'])
def submit_pipeline():
    """
    提交Pipeline作业到Flink
    """
    try:
        data = request.get_json() or {}
        pipeline_file = data.get('pipeline_file', 'generated-pipeline.yaml')
        
        pipeline_service = PipelineService(current_app.config)
        
        # 提交Pipeline
        job_id = pipeline_service.submit_pipeline(pipeline_file)
        
        return jsonify({
            'code': 0,
            'message': 'Pipeline submitted successfully',
            'data': {
                'job_id': job_id,
                'pipeline_file': pipeline_file
            }
        })
    except Exception as e:
        logger.error(f"Failed to submit pipeline: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to submit pipeline: {str(e)}'
        }), 500

@pipeline_bp.route('/cancel/<job_id>', methods=['POST'])
def cancel_pipeline(job_id):
    """取消Pipeline作业"""
    try:
        pipeline_service = PipelineService(current_app.config)
        
        # 取消作业
        pipeline_service.cancel_job(job_id)
        
        return jsonify({
            'code': 0,
            'message': f'Job {job_id} cancelled successfully',
            'data': {'job_id': job_id}
        })
    except Exception as e:
        logger.error(f"Failed to cancel pipeline: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to cancel pipeline: {str(e)}'
        }), 500

@pipeline_bp.route('/jobs', methods=['GET'])
def list_jobs():
    """获取所有Flink作业"""
    try:
        pipeline_service = PipelineService(current_app.config)
        
        jobs = pipeline_service.get_all_jobs()
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': {
                'jobs': jobs,
                'total': len(jobs)
            }
        })
    except Exception as e:
        logger.error(f"Failed to list jobs: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to list jobs: {str(e)}'
        }), 500

@pipeline_bp.route('/jobs/<job_id>', methods=['GET'])
def get_job_detail(job_id):
    """获取作业详情"""
    try:
        pipeline_service = PipelineService(current_app.config)
        
        job_detail = pipeline_service.get_job_detail(job_id)
        
        return jsonify({
            'code': 0,
            'message': 'success',
            'data': job_detail
        })
    except Exception as e:
        logger.error(f"Failed to get job detail: {e}", exc_info=True)
        return jsonify({
            'code': 500,
            'message': f'Failed to get job detail: {str(e)}'
        }), 500
