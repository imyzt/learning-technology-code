#!/usr/bin/env python3
"""
Flink SQL CDC 作业生成和提交脚本
根据 sync-tables.json 配置，动态生成 Flink SQL 语句并提交作业
"""

import json
import requests
import os
import time
import sys
from datetime import datetime

class FlinkCDCSubmitter:
    def __init__(self, config_file, flink_host='localhost', flink_port=8081):
        self.config_file = config_file
        self.flink_host = flink_host
        self.flink_port = flink_port
        self.flink_url = f'http://{flink_host}:{flink_port}'
        self.config = self.load_config()
    
    def load_config(self):
        """加载配置文件"""
        with open(self.config_file, 'r', encoding='utf-8') as f:
            return json.load(f)
    
    def generate_sql(self):
        """生成 Flink SQL 语句"""
        sql_statements = []
        
        pg_config = self.config['postgres']
        sr_config = self.config['starrocks']
        sync_config = self.config['sync_config']
        
        # PostgreSQL 连接器定义
        pg_connector = f"""
CREATE CATALOG postgres_source WITH (
    'type' = 'jdbc',
    'default-database' = '{pg_config["database"]}',
    'base-url' = 'jdbc:postgresql://{pg_config["host"]}:{pg_config["port"]}',
    'username' = '{pg_config["username"]}',
    'password' = '{pg_config["password"]}'
);
"""
        sql_statements.append(pg_connector)
        
        # StarRocks 连接器定义
        sr_connector = f"""
CREATE CATALOG starrocks_sink WITH (
    'type' = 'starrocks',
    'fe.nodes' = '{sr_config["host"]}:{sr_config["port"]}',
    'database.name' = 'cdc_db',
    'username' = '{sr_config["username"]}',
    'password' = '{sr_config["password"]}'
);
"""
        sql_statements.append(sr_connector)
        
        # 创建目标数据库
        sql_statements.append("""
CREATE DATABASE IF NOT EXISTS starrocks_sink.cdc_db;
""")
        
        # 为每个启用的表生成同步 SQL
        enabled_tables = [t for t in sync_config['tables'] if t.get('enabled', False)]
        
        for table in enabled_tables:
            table_name = table['table_name']
            target_table = table.get('target_table', table_name)
            
            # 创建 StarRocks 表的 DDL（简化版，实际需要更复杂的逻辑）
            create_table_sql = f"""
CREATE TABLE IF NOT EXISTS starrocks_sink.cdc_db.{target_table} AS
SELECT * FROM postgres_source.{pg_config['schema']}.{table_name}
WHERE FALSE;
"""
            sql_statements.append(create_table_sql)
            
            # 创建实时同步任务
            sync_job = f"""
INSERT INTO starrocks_sink.cdc_db.{target_table}
SELECT * FROM postgres_source.{pg_config['schema']}.{table_name};
"""
            sql_statements.append(sync_job)
        
        return '\n'.join(sql_statements)
    
    def generate_python_cdc_job(self):
        """生成 Python Flink CDC 作业"""
        sync_config = self.config['sync_config']
        enabled_tables = [t for t in sync_config['tables'] if t.get('enabled', False)]
        
        if not enabled_tables:
            print("没有启用的表")
            return None
        
        python_code = """#!/usr/bin/env python3
from pyflink.datastream import StreamExecutionEnvironment
from pyflink.datastream.functions import MapFunction
import json

env = StreamExecutionEnvironment.get_execution_environment()

# 配置 CDC Source
"""
        
        return python_code
    
    def submit_job_via_rest(self, sql):
        """通过 Flink REST API 提交作业"""
        try:
            # 首先获取 jobmanager 的 session id
            response = requests.get(f'{self.flink_url}/api/overview')
            if response.status_code != 200:
                print(f"无法连接到 Flink: {response.status_code}")
                return False
            
            # 提交 SQL
            jar_id = self.upload_jar()
            if not jar_id:
                print("上传 JAR 失败")
                return False
            
            submit_data = {
                'jarId': jar_id,
                'entryClass': 'org.apache.flink.cdc.cli.FlinkCDC',
                'programArgs': ['--sql-file', '/tmp/flink_cdc.sql'],
                'parallelism': 4
            }
            
            response = requests.post(
                f'{self.flink_url}/api/jars/{jar_id}/run',
                json=submit_data
            )
            
            if response.status_code == 200:
                job_id = response.json().get('jobid')
                print(f"作业提交成功，Job ID: {job_id}")
                return True
            else:
                print(f"作业提交失败: {response.text}")
                return False
        except Exception as e:
            print(f"提交作业失败: {e}")
            return False
    
    def get_flink_status(self):
        """获取 Flink 状态"""
        try:
            response = requests.get(f'{self.flink_url}/api/overview')
            if response.status_code == 200:
                data = response.json()
                return {
                    'taskmanagers': data.get('taskmanagers', 0),
                    'slots_available': data.get('slots_available', 0),
                    'slots_total': data.get('slots_total', 0),
                    'jobs_running': data.get('jobs-running', 0)
                }
            return None
        except Exception as e:
            print(f"获取状态失败: {e}")
            return None

def main():
    import argparse
    
    parser = argparse.ArgumentParser(description='Flink CDC 任务提交工具')
    parser.add_argument('--config', default='sync-tables.json', help='配置文件路径')
    parser.add_argument('--flink-host', default=os.environ.get('FLINK_HOST', 'localhost'), help='Flink host')
    parser.add_argument('--flink-port', type=int, default=int(os.environ.get('FLINK_PORT', 8081)), help='Flink port')
    parser.add_argument('--generate-sql', action='store_true', help='生成 SQL 语句')
    parser.add_argument('--submit', action='store_true', help='提交任务')
    parser.add_argument('--status', action='store_true', help='查看任务状态')
    
    args = parser.parse_args()
    
    submitter = FlinkCDCSubmitter(args.config, args.flink_host, args.flink_port)
    
    if args.generate_sql:
        sql = submitter.generate_sql()
        print("生成的 SQL:")
        print(sql)
    elif args.submit:
        sql = submitter.generate_sql()
        success = submitter.submit_job_via_rest(sql)
        sys.exit(0 if success else 1)
    elif args.status:
        status = submitter.get_flink_status()
        if status:
            print("Flink 状态:")
            print(json.dumps(status, indent=2))
    else:
        parser.print_help()

if __name__ == '__main__':
    main()
"""
        return python_code

if __name__ == '__main__':
    submitter = FlinkCDCSubmitter('sync-tables.json')
    print(submitter.generate_sql())
