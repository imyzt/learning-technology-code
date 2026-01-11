-- StarRocks 初始化脚本
-- 创建数据库和表

-- ==================== 创建数据库 ====================

CREATE DATABASE IF NOT EXISTS cdc_db;
USE cdc_db;

-- ==================== 创建表 ====================
-- 注意：StarRocks中的表结构应该与PostgreSQL对应

-- 用户表
CREATE TABLE IF NOT EXISTS users (
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    age INT,
    city VARCHAR(50),
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=OLAP
DUPLICATE KEY(id)
DISTRIBUTED BY HASH(id) BUCKETS 4
PROPERTIES (
    "replication_num" = "1",
    "storage_medium" = "HDD"
);

-- 订单表
CREATE TABLE IF NOT EXISTS orders (
    id INT NOT NULL,
    user_id INT NOT NULL,
    order_no VARCHAR(50) NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20),
    order_date DATETIME,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=OLAP
DUPLICATE KEY(id)
DISTRIBUTED BY HASH(id) BUCKETS 4
PROPERTIES (
    "replication_num" = "1",
    "storage_medium" = "HDD"
);

-- 产品表
CREATE TABLE IF NOT EXISTS products (
    id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    stock INT,
    description STRING,
    created_at DATETIME,
    updated_at DATETIME
) ENGINE=OLAP
DUPLICATE KEY(id)
DISTRIBUTED BY HASH(id) BUCKETS 4
PROPERTIES (
    "replication_num" = "1",
    "storage_medium" = "HDD"
);

-- ==================== 完成 ====================

SELECT 'StarRocks initialization completed!' as status;
