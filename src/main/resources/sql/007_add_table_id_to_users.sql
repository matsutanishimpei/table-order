ALTER TABLE users ADD COLUMN table_id INT;
ALTER TABLE users ADD FOREIGN KEY (table_id) REFERENCES shop_tables(id);
