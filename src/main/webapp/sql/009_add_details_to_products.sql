-- products テーブルに商品説明とアレルギー情報を追加するマイグレーションスクリプト
-- 既存の環境でこのスクリプトを実行することで、データを保持したままカラムを追加できます。

ALTER TABLE products ADD COLUMN description TEXT AFTER price;
ALTER TABLE products ADD COLUMN allergy_info TEXT AFTER description;

-- 必要に応じて、既存のデータにデフォルト値を設定する場合は以下のように記述します（今回は空でOK）
-- UPDATE products SET description = '説明なし' WHERE description IS NULL;
