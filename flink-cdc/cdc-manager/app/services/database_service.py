"""
数据库服务
处理PostgreSQL和StarRocks的连接和操作
"""

import psycopg2
from psycopg2.extras import RealDictCursor
import pymysql
import logging
from typing import List, Dict, Optional

logger = logging.getLogger(__name__)

class DatabaseService:
    def __init__(self, config):
        self.config = config
    
    def get_postgres_connection(self):
        """获取PostgreSQL连接"""
        try:
            conn = psycopg2.connect(
                host=self.config['POSTGRES_HOST'],
                port=self.config['POSTGRES_PORT'],
                user=self.config['POSTGRES_USER'],
                password=self.config['POSTGRES_PASSWORD'],
                database=self.config['POSTGRES_DB']
            )
            return conn
        except Exception as e:
            logger.error(f"Failed to connect to PostgreSQL: {e}")
            raise
    
    def get_starrocks_connection(self):
        """获取StarRocks连接（通过MySQL协议）"""
        try:
            conn = pymysql.connect(
                host=self.config['STARROCKS_HOST'],
                port=self.config['STARROCKS_QUERY_PORT'],
                user=self.config['STARROCKS_USER'],
                password=self.config['STARROCKS_PASSWORD'],
                charset='utf8mb4'
            )
            return conn
        except Exception as e:
            logger.error(f"Failed to connect to StarRocks: {e}")
            raise
    
    def check_postgres_connection(self) -> Dict:
        """检查PostgreSQL连接状态"""
        try:
            conn = self.get_postgres_connection()
            with conn.cursor() as cur:
                cur.execute("SELECT version();")
                version = cur.fetchone()[0]
            conn.close()
            return {
                'connected': True,
                'version': version
            }
        except Exception as e:
            logger.error(f"PostgreSQL connection check failed: {e}")
            return {
                'connected': False,
                'error': str(e)
            }
    
    def check_starrocks_connection(self) -> Dict:
        """检查StarRocks连接状态"""
        try:
            conn = self.get_starrocks_connection()
            with conn.cursor() as cur:
                cur.execute("SELECT @@version;")
                version = cur.fetchone()[0]
            conn.close()
            return {
                'connected': True,
                'version': version
            }
        except Exception as e:
            logger.error(f"StarRocks connection check failed: {e}")
            return {
                'connected': False,
                'error': str(e)
            }
    
    def get_all_pg_tables(self) -> List[str]:
        """获取PostgreSQL所有表"""
        conn = self.get_postgres_connection()
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
        finally:
            conn.close()
    
    def get_table_info(self, table_name: str) -> Dict:
        """获取表的基本信息"""
        conn = self.get_postgres_connection()
        try:
            with conn.cursor(cursor_factory=RealDictCursor) as cur:
                # 获取行数
                cur.execute(f"SELECT COUNT(*) as count FROM {table_name}")
                row_count = cur.fetchone()['count']
                
                # 获取列数
                cur.execute("""
                    SELECT COUNT(*) as count
                    FROM information_schema.columns
                    WHERE table_schema = 'public' AND table_name = %s
                """, (table_name,))
                column_count = cur.fetchone()['count']
                
                # 获取表大小
                cur.execute("""
                    SELECT pg_size_pretty(pg_total_relation_size(%s)) as size
                """, (table_name,))
                table_size = cur.fetchone()['size']
            
            return {
                'table_name': table_name,
                'row_count': row_count,
                'column_count': column_count,
                'table_size': table_size
            }
        finally:
            conn.close()
    
    def get_table_columns(self, table_name: str) -> List[Dict]:
        """获取表的列信息"""
        conn = self.get_postgres_connection()
        try:
            with conn.cursor(cursor_factory=RealDictCursor) as cur:
                cur.execute("""
                    SELECT 
                        column_name,
                        data_type,
                        is_nullable,
                        column_default,
                        character_maximum_length
                    FROM information_schema.columns
                    WHERE table_schema = 'public' AND table_name = %s
                    ORDER BY ordinal_position
                """, (table_name,))
                columns = cur.fetchall()
            return columns
        finally:
            conn.close()
    
    def get_table_row_count(self, table_name: str) -> int:
        """获取表的行数"""
        conn = self.get_postgres_connection()
        try:
            with conn.cursor() as cur:
                cur.execute(f"SELECT COUNT(*) FROM {table_name}")
                count = cur.fetchone()[0]
            return count
        except Exception as e:
            logger.error(f"Failed to get row count for {table_name}: {e}")
            return 0
        finally:
            conn.close()
    
    def get_starrocks_table_row_count(self, table_name: str) -> int:
        """获取StarRocks表的行数"""
        conn = self.get_starrocks_connection()
        try:
            with conn.cursor() as cur:
                cur.execute(f"SELECT COUNT(*) FROM cdc_db.{table_name}")
                count = cur.fetchone()[0]
            return count
        except Exception as e:
            logger.error(f"Failed to get StarRocks row count for {table_name}: {e}")
            return 0
        finally:
            conn.close()
    
    def get_current_timestamp(self) -> str:
        """获取当前时间戳"""
        conn = self.get_postgres_connection()
        try:
            with conn.cursor() as cur:
                cur.execute("SELECT NOW()::text")
                timestamp = cur.fetchone()[0]
            return timestamp
        finally:
            conn.close()
