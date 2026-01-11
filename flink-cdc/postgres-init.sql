-- 创建测试表用于 CDC 演示
CREATE TABLE users (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(100),
    age INT,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE orders (
    id SERIAL PRIMARY KEY,
    user_id INT NOT NULL REFERENCES users(id),
    order_no VARCHAR(50) NOT NULL,
    total_amount DECIMAL(10, 2),
    status VARCHAR(20),
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE TABLE products (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    price DECIMAL(10, 2),
    category VARCHAR(50),
    stock INT DEFAULT 0,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

-- 创建逻辑复制槽和发布
SELECT * FROM pg_create_logical_replication_slot('flink_slot', 'pgoutput');

CREATE PUBLICATION flink_pub FOR ALL TABLES;

-- 插入样本数据
INSERT INTO users (name, email, age) VALUES
('Alice', 'alice@example.com', 25),
('Bob', 'bob@example.com', 30),
('Charlie', 'charlie@example.com', 28);

INSERT INTO products (name, price, category, stock) VALUES
('Laptop', 999.99, 'Electronics', 50),
('Mouse', 29.99, 'Electronics', 200),
('Keyboard', 79.99, 'Electronics', 150);

INSERT INTO orders (user_id, order_no, total_amount, status) VALUES
(1, 'ORD-001', 1029.98, 'completed'),
(2, 'ORD-002', 109.98, 'pending');
