-- usersテーブルにソルト用カラムを追加
ALTER TABLE users ADD COLUMN salt VARCHAR(100) AFTER password;

-- 既存の管理ユーザー(admin)をハッシュ化対応に更新。
-- パスワード: admin123
-- ソルト: 固定値（初期設定用。実際にはSecureRandomで再生成が望ましい）
-- ハッシュ: admin123 + "NmJjZGVmZ2hpa2xtbm9wcQ==" を PasswordUtil でハッシュ化したもの
UPDATE users SET 
    password = 'A3+FmEnG9iUoI568mI9GkC9Wf+5T8p6c5Hq6u8F4W8Y=', 
    salt = 'NmJjZGVmZ2hpa2xtbm9wcQ==' 
WHERE id = 'admin';
