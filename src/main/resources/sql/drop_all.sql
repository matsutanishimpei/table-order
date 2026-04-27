SET NAMES utf8mb4;

-- 外部キー制約の依存関係を考慮し、子テーブルから削除
DROP TABLE IF EXISTS order_items;
DROP TABLE IF EXISTS orders;
DROP TABLE IF EXISTS products;
DROP TABLE IF EXISTS categories;
DROP TABLE IF EXISTS users;
DROP TABLE IF EXISTS shop_tables;
