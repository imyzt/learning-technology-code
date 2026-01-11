"""
Pipeline服务
处理Flink CDC Pipeline的生成、提交和管理
"""

import os
import yaml
import requests
import logging
from typing import Dict, List
from datetime import datetime

logger = logging.getLogger(__name__)

class PipelineService:
    def __init__(self, config):
        self.config = config
        self.pipeline_dir = config['PIPELINE_CONFIG_DIR']
        self.flink_host = config['FLINK_CDC_HOST']
        self.flink_port = config['FLINK_WEB_UI_PORT']
        self.flink_url = f"http://{self.flink_host}:{self.flink_port}"
    
    def check_flink_status(self) -> Dict:
        """检查Flink集群状态"""
        try:
            response = requests.get(f"{self.flink_url}/", timeout=5)
            return {
                'available': response.status_code == 200,
                'url': self.flink_url
            }
        except Exception as e:
            logger.error(f"Failed to check Flink status: {e}")
            return {
                'available': False,
                'error': str(e)
            }
    
    def get_flink_overview(self) -> Dict:
        """获取Flink集群概览"""
        try:
            response = requests.get(f"{self.flink_url}/overview", timeout=5)
            if response.status_code == 200:
                data = response.json()
                return {
                    'taskmanagers': data.get('taskmanagers', 0),
                    'slots_total': data.get('slots-total', 0),
                    'slots_available': data.get('slots-available', 0),
                    'jobs_running': data.get('jobs-running', 0),
                    'jobs_finished': data.get('jobs-finished', 0),
                    'jobs_cancelled': data.get('jobs-cancelled', 0),
                    'jobs_failed': data.get('jobs-failed', 0),
                    'flink_version': data.get('flink-version', 'unknown')
                }
            else:
                return {'error': f'HTTP {response.status_code}'}
        except Exception as e:
            logger.error(f"Failed to get Flink overview: {e}")
            return {'error': str(e)}
    
    def get_all_jobs(self) -> List[Dict]:
        """获取所有Flink作业"""
        try:
            response = requests.get(f"{self.flink_url}/jobs/overview", timeout=5)
            if response.status_code == 200:
                data = response.json()
                return data.get('jobs', [])
            else:
                return []
        except Exception as e:
            logger.error(f"Failed to get jobs: {e}")
            return []
    
    def get_job_detail(self, job_id: str) -> Dict:
        """获取作业详情"""
        try:
            response = requests.get(f"{self.flink_url}/jobs/{job_id}", timeout=5)
            if response.status_code == 200:
                return response.json()
            else:
                return {'error': f'HTTP {response.status_code}'}
        except Exception as e:
            logger.error(f"Failed to get job detail: {e}")
            return {'error': str(e)}
    
    def generate_pipeline_yaml(self, config: Dict) -> str:
        """根据配置生成Pipeline YAML"""
        defaults = config.get('defaults', {})
        tables = [t for t in config.get('tables', []) if t.get('enabled', False)]
        
        if not tables:
            raise ValueError("No enabled tables found")
        
        # 构建table-list
        table_list = ','.join([f"{t.get('source_schema', 'public')}.{t['table_name']}" for t in tables])
        
        # 构建route规则
        routes = []
        for table in tables:
            route = {
                'source-table': f"{table.get('source_schema', 'public')}.{table['table_name']}",
                'sink-table': f"{table.get('sink_database', 'cdc_db')}.{table.get('sink_table', table['table_name'])}",
                'description': table.get('description', '')
            }
            routes.append(route)
        
        # 构建Pipeline配置
        pipeline = {
            'pipeline': {
                'name': f"CDC Pipeline - {datetime.now().strftime('%Y%m%d_%H%M%S')}",
                'parallelism': defaults.get('pipeline', {}).get('parallelism', 2)
            },
            'source': {
                **defaults.get('source', {}),
                'table-list': table_list
            },
            'sink': defaults.get('sink', {}),
            'route': routes
        }
        
        # 添加Pipeline全局配置
        for key, value in defaults.get('pipeline', {}).items():
            if key != 'parallelism':
                pipeline[key] = value
        
        # 转换为YAML
        yaml_content = yaml.dump(pipeline, allow_unicode=True, default_flow_style=False, sort_keys=False)
        
        return yaml_content
    
    def save_pipeline(self, pipeline_yaml: str, filename: str = None) -> str:
        """保存Pipeline YAML文件"""
        if filename is None:
            filename = f"pipeline-{datetime.now().strftime('%Y%m%d_%H%M%S')}.yaml"
        
        filepath = os.path.join(self.pipeline_dir, filename)
        
        # 确保目录存在
        os.makedirs(self.pipeline_dir, exist_ok=True)
        
        with open(filepath, 'w', encoding='utf-8') as f:
            f.write(pipeline_yaml)
        
        logger.info(f"Pipeline saved to {filepath}")
        
        return filepath
    
    def submit_pipeline(self, pipeline_file: str) -> str:
        """
        提交Pipeline到Flink
        
        注意：这需要在Flink容器内执行提交脚本
        这里返回一个模拟的job_id，实际应该通过Flink REST API或exec到容器内执行
        """
        try:
            # 实际生产环境中，应该：
            # 1. 调用Flink REST API上传JAR
            # 2. 通过docker exec执行submit-pipeline.sh脚本
            # 3. 解析返回的job_id
            
            # 这里提供一个简化版本，返回提示信息
            pipeline_path = os.path.join(self.pipeline_dir, pipeline_file)
            
            if not os.path.exists(pipeline_path):
                raise FileNotFoundError(f"Pipeline file not found: {pipeline_path}")
            
            logger.info(f"Pipeline submission requested: {pipeline_path}")
            logger.info("In production, this should execute: docker exec flink-cdc-runtime /opt/scripts/submit-pipeline.sh")
            
            # 返回一个模拟的job_id
            # 在实际环境中，应该通过Flink REST API或命令执行获取真实的job_id
            job_id = f"job-{datetime.now().strftime('%Y%m%d%H%M%S')}"
            
            return job_id
        except Exception as e:
            logger.error(f"Failed to submit pipeline: {e}")
            raise
    
    def cancel_job(self, job_id: str):
        """取消Flink作业"""
        try:
            response = requests.patch(
                f"{self.flink_url}/jobs/{job_id}",
                params={'mode': 'cancel'},
                timeout=10
            )
            
            if response.status_code == 202:
                logger.info(f"Job {job_id} cancelled successfully")
                return True
            else:
                raise Exception(f"Failed to cancel job: HTTP {response.status_code}")
        except Exception as e:
            logger.error(f"Failed to cancel job: {e}")
            raise
