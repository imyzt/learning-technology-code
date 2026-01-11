-- PostgreSQL 初始化脚本
-- 创建测试表和配置CDC

-- ==================== 创建测试表 ====================

CREATE TABLE IF NOT EXISTS users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100) UNIQUE,
    age INT,
    city VARCHAR(50),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE IF NOT EXISTS orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL,
    order_no VARCHAR(50) UNIQUE NOT NULL,
    total_amount DECIMAL(12, 2) NOT NULL,
    status VARCHAR(20) DEFAULT 'pending',
    order_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES users(id) ON DELETE CASCADE
);

CREATE TABLE IF NOT EXISTS products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    category VARCHAR(50),
    price DECIMAL(10, 2) NOT NULL,
    stock INT DEFAULT 0,
    description TEXT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建索引
CREATE INDEX IF NOT EXISTS idx_users_email ON users(email);
CREATE INDEX IF NOT EXISTS idx_orders_user_id ON orders(user_id);
CREATE INDEX IF NOT EXISTS idx_orders_status ON orders(status);
CREATE INDEX IF NOT EXISTS idx_products_category ON products(category);

-- ==================== 插入测试数据 ====================

INSERT INTO users (name, email, age, city) VALUES
('张三', 'zhangsan@example.com', 28, '北京'),
('李四', 'lisi@example.com', 32, '上海'),
('王五', 'wangwu@example.com', 25, '深圳'),
('赵六', 'zhaoliu@example.com', 30, '杭州'),
('钱七', 'qianqi@example.com', 27, '成都')
ON CONFLICT (email) DO NOTHING;

INSERT INTO products (name, category, price, stock, description) VALUES
('iPhone 15', '手机', 5999.00, 100, '苹果最新款手机'),
('MacBook Pro', '电脑', 12999.00, 50, '专业笔记本电脑'),
('AirPods Pro', '耳机', 1999.00, 200, '主动降噪耳机'),
('iPad Air', '平板', 4799.00, 80, '轻薄便携平板'),
('Apple Watch', '手表', 2999.00, 150, '智能手表')
ON CONFLICT DO NOTHING;

INSERT INTO orders (user_id, order_no, total_amount, status) VALUES
(1, 'ORD20260111001', 7998.00, 'completed'),
(2, 'ORD20260111002', 12999.00, 'pending'),
(3, 'ORD20260111003', 1999.00, 'shipped'),
(1, 'ORD20260111004', 4799.00, 'completed'),
(4, 'ORD20260111005', 8998.00, 'pending')
ON CONFLICT (order_no) DO NOTHING;

-- ==================== 配置CDC ====================

-- 创建逻辑复制槽（如果不存在）
SELECT 'Checking replication slot...' as status;

DO $$
BEGIN
    -- 检查复制槽是否存在
    IF NOT EXISTS (
        SELECT 1 FROM pg_replication_slots WHERE slot_name = 'flink_cdc_slot'
    ) THEN
        -- 创建复制槽
        PERFORM pg_create_logical_replication_slot('flink_cdc_slot', 'pgoutput');
        RAISE NOTICE 'Created replication slot: flink_cdc_slot';
    ELSE
        RAISE NOTICE 'Replication slot already exists: flink_cdc_slot';
    END IF;
END $$;

-- 创建发布（Publication）
-- 发布所有表的变更
DO $$
BEGIN
    IF NOT EXISTS (
        SELECT 1 FROM pg_publication WHERE pubname = 'flink_cdc_pub'
    ) THEN
        CREATE PUBLICATION flink_cdc_pub FOR ALL TABLES;
        RAISE NOTICE 'Created publication: flink_cdc_pub';
    ELSE
        RAISE NOTICE 'Publication already exists: flink_cdc_pub';
    END IF;
END $$;

-- ==================== 创建更新触发器 ====================

-- 自动更新updated_at字段
CREATE OR REPLACE FUNCTION update_updated_at_column()
RETURNS TRIGGER AS $$
BEGIN
    NEW.updated_at = CURRENT_TIMESTAMP;
    RETURN NEW;
END;
$$ LANGUAGE plpgsql;

-- 为每个表创建触发器
DROP TRIGGER IF EXISTS update_users_updated_at ON users;
CREATE TRIGGER update_users_updated_at
    BEFORE UPDATE ON users
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_orders_updated_at ON orders;
CREATE TRIGGER update_orders_updated_at
    BEFORE UPDATE ON orders
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

DROP TRIGGER IF EXISTS update_products_updated_at ON products;
CREATE TRIGGER update_products_updated_at
    BEFORE UPDATE ON products
    FOR EACH ROW
    EXECUTE FUNCTION update_updated_at_column();

-- ==================== 权限配置 ====================

-- 确保CDC用户有足够权限
GRANT SELECT ON ALL TABLES IN SCHEMA public TO postgres;
GRANT USAGE ON SCHEMA public TO postgres;

-- ==================== 完成 ====================

SELECT 'PostgreSQL initialization completed!' as status;
SELECT 'Tables created: users, orders, products' as info;
SELECT 'Replication slot: flink_cdc_slot' as info;
SELECT 'Publication: flink_cdc_pub' as info;
SELECT 'Sample data inserted' as info;
