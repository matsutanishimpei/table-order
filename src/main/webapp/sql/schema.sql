SET NAMES utf8mb4;

-- 1. 座席・テーブル情報 (shop_tables)
CREATE TABLE shop_tables (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(20) NOT NULL
);

-- 2. ユーザー情報 (users)
CREATE TABLE users (
    id VARCHAR(20) PRIMARY KEY,
    password VARCHAR(255) NOT NULL,
    salt VARCHAR(100),
    role INT NOT NULL,
    table_id INT,
    FOREIGN KEY (table_id) REFERENCES shop_tables(id)
);

-- 3. カテゴリ情報 (categories)
CREATE TABLE categories (
    id INT AUTO_INCREMENT PRIMARY KEY,
    name VARCHAR(50) NOT NULL
);

-- 4. 商品情報 (products)
CREATE TABLE products (
    id INT AUTO_INCREMENT PRIMARY KEY,
    category_id INT NOT NULL,
    name VARCHAR(100) NOT NULL,
    price INT NOT NULL,
    image_path VARCHAR(255),
    is_available BOOLEAN DEFAULT TRUE,
    FOREIGN KEY (category_id) REFERENCES categories(id)
);

-- 5. 注文親情報 (orders)
CREATE TABLE orders (
    id INT AUTO_INCREMENT PRIMARY KEY,
    table_id INT NOT NULL,
    status INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    updated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP ON UPDATE CURRENT_TIMESTAMP,
    FOREIGN KEY (table_id) REFERENCES shop_tables(id)
);

-- 6. 注文明細情報 (order_items)
CREATE TABLE order_items (
    id INT AUTO_INCREMENT PRIMARY KEY,
    order_id INT NOT NULL,
    product_id INT NOT NULL,
    quantity INT NOT NULL,
    unit_price INT NOT NULL,
    status INT NOT NULL DEFAULT 10,
    created_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (order_id) REFERENCES orders(id),
    FOREIGN KEY (product_id) REFERENCES products(id)
);
