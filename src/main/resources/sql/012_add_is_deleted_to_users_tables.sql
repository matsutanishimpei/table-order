-- ユーザーテーブルとテーブル情報テーブルに論理削除フラグを追加
ALTER TABLE users ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE AFTER updated_by;
ALTER TABLE shop_tables ADD COLUMN is_deleted BOOLEAN NOT NULL DEFAULT FALSE AFTER updated_by;
