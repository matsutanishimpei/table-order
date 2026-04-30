-- 監査ログテーブル (Audit Log)
CREATE TABLE audit_log (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    table_name VARCHAR(50) NOT NULL,
    record_id VARCHAR(50) NOT NULL,
    action VARCHAR(20) NOT NULL,
    old_value TEXT,
    new_value TEXT,
    operated_by VARCHAR(20) NOT NULL,
    operated_at TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);
