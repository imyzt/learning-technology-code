"""
配置服务
处理Pipeline配置文件的读写和管理
"""

import os
import yaml
import logging
from typing import Dict, List
from datetime import datetime

logger = logging.getLogger(__name__)

class ConfigService:
    def __init__(self, config):
        self.config = config
        self.config_dir = config['PIPELINE_CONFIG_DIR']
        self.config_file = os.path.join(self.config_dir, 'pipeline-config.yaml')
    
    def load_pipeline_config(self) -> Dict:
        """加载Pipeline配置"""
        try:
            if not os.path.exists(self.config_file):
                logger.warning(f"Config file not found: {self.config_file}, creating default")
                return self._create_default_config()
            
            with open(self.config_file, 'r', encoding='utf-8') as f:
                config = yaml.safe_load(f)
            
            return config
        except Exception as e:
            logger.error(f"Failed to load pipeline config: {e}")
            raise
    
    def save_pipeline_config(self, config: Dict):
        """保存Pipeline配置"""
        try:
            # 确保目录存在
            os.makedirs(self.config_dir, exist_ok=True)
            
            with open(self.config_file, 'w', encoding='utf-8') as f:
                yaml.dump(config, f, allow_unicode=True, default_flow_style=False)
            
            logger.info(f"Pipeline config saved to {self.config_file}")
        except Exception as e:
            logger.error(f"Failed to save pipeline config: {e}")
            raise
    
    def _create_default_config(self) -> Dict:
        """创建默认配置"""
        config = {
            'defaults': {
                'source': {
                    'type': 'postgres',
                    'hostname': self.config['POSTGRES_HOST'],
                    'port': self.config['POSTGRES_PORT'],
                    'username': self.config['POSTGRES_USER'],
                    'password': self.config['POSTGRES_PASSWORD'],
                    'database': self.config['POSTGRES_DB'],
                    'schema': 'public',
                    'slot.name': 'flink_cdc_slot',
                    'scan.startup.mode': 'initial',
                    'scan.incremental.snapshot.enabled': True,
                    'scan.incremental.snapshot.chunk.size': 8192,
                    'heartbeat.interval': '10s'
                },
                'sink': {
                    'type': 'starrocks',
                    'jdbc-url': f"jdbc:mysql://{self.config['STARROCKS_HOST']}:{self.config['STARROCKS_QUERY_PORT']}",
                    'load-url': f"{self.config['STARROCKS_HOST']}:{self.config['STARROCKS_FE_HTTP_PORT']}",
                    'username': self.config['STARROCKS_USER'],
                    'password': self.config['STARROCKS_PASSWORD'],
                    'database': 'cdc_db',
                    'sink.properties.format': 'json',
                    'sink.properties.strip_outer_array': True,
                    'sink.buffer-flush.max-rows': 50000,
                    'sink.buffer-flush.max-bytes': 10485760,
                    'sink.buffer-flush.interval': '10s',
                    'sink.parallelism': 2,
                    'table.create.properties.replication_num': 1,
                    'table.create.properties.storage_medium': 'HDD'
                },
                'pipeline': {
                    'parallelism': 2,
                    'schema.change.behavior': 'evolve',
                    'execution.checkpointing.interval': '60s',
                    'execution.checkpointing.mode': 'EXACTLY_ONCE',
                    'state.backend.type': 'rocksdb',
                    'state.backend.incremental': True
                }
            },
            'tables': [],
            'created_at': datetime.now().isoformat(),
            'updated_at': datetime.now().isoformat()
        }
        
        # 保存默认配置
        self.save_pipeline_config(config)
        
        return config
    
    def get_table_config(self, table_name: str) -> Dict:
        """获取单个表的配置"""
        config = self.load_pipeline_config()
        tables = config.get('tables', [])
        
        for table in tables:
            if table['table_name'] == table_name:
                return table
        
        return None
    
    def enable_table(self, table_name: str):
        """启用表同步"""
        config = self.load_pipeline_config()
        tables = config.get('tables', [])
        
        found = False
        for table in tables:
            if table['table_name'] == table_name:
                table['enabled'] = True
                table['updated_at'] = datetime.now().isoformat()
                found = True
                break
        
        if not found:
            # 新增表配置
            tables.append({
                'table_name': table_name,
                'enabled': True,
                'source_schema': 'public',
                'source_table': table_name,
                'sink_database': 'cdc_db',
                'sink_table': table_name,
                'description': '',
                'created_at': datetime.now().isoformat(),
                'updated_at': datetime.now().isoformat()
            })
        
        config['tables'] = tables
        config['updated_at'] = datetime.now().isoformat()
        
        self.save_pipeline_config(config)
    
    def disable_table(self, table_name: str):
        """禁用表同步"""
        config = self.load_pipeline_config()
        tables = config.get('tables', [])
        
        for table in tables:
            if table['table_name'] == table_name:
                table['enabled'] = False
                table['updated_at'] = datetime.now().isoformat()
                break
        
        config['updated_at'] = datetime.now().isoformat()
        
        self.save_pipeline_config(config)
    
    def get_enabled_tables(self) -> List[str]:
        """获取所有启用的表"""
        config = self.load_pipeline_config()
        tables = config.get('tables', [])
        
        return [t['table_name'] for t in tables if t.get('enabled', False)]
