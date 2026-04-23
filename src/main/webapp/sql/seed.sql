SET NAMES utf8mb4;
SET FOREIGN_KEY_CHECKS = 0;

-- ユーザーデータの初期投入 (role: 1:管理者, 2:キッチン, 3:ホール, 4:会計, 10:テーブル)
-- 全初期ユーザーのパスワードは 'pass' に設定されています
INSERT INTO users (id, password, salt, role, table_id) VALUES ('admin', 'KLt8mBJ4EgncZ+aHSVsFw0u3gt7Y1cUnpNOfJItSg54=', 'KKAcAppu2PCIfzZLzlCUPA==', 1, NULL);
INSERT INTO users (id, password, salt, role, table_id) VALUES ('kitchen', 'SDikYo8lbVMnxhSESZfBhjZuJqLnx/GReVs9SwN9Hac=', 'J2jFaJeWlTGG23b7OD+eIQ==', 2, NULL);
INSERT INTO users (id, password, salt, role, table_id) VALUES ('hall', 'lxaEyLs415VHzyade/lv68A6jcf7buay4oMX2jIXaJs=', 'yG8UAAovEQNTNWF80brfRA==', 3, NULL);
INSERT INTO users (id, password, salt, role, table_id) VALUES ('cashier', 'FnpWlbBeQB3ixI4CBr2HDPpk9vul9KPej0yY1jtobZY=', 'qrzqyNYbdhO4PfaCzyp4cA==', 4, NULL);
INSERT INTO users (id, password, salt, role, table_id) VALUES ('t1', 'fHYAL8bsJNGX96lGDir6aZcCF4UwDFsgm52l2ruHxWM=', 'hygMtpmqxIrxoGthZKFvzw==', 10, 1);

-- カテゴリデータの初期投入
INSERT INTO categories (name) VALUES ('ドリンク');
INSERT INTO categories (name) VALUES ('フード');
INSERT INTO categories (name) VALUES ('デザート');

-- 商品データの初期投入 (category_id, name, price, description, allergy_info)
-- ドリンク (category_id: 1)
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (1, '生ビール', 500, '工場直送の新鮮な生ビールです。', 'なし');
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (1, 'ウーロン茶', 300, '香り高い茶葉を使用した定番ドリンク。', 'なし');
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (1, 'コーラ', 300, '爽快な炭酸と甘みのバランス。', 'なし');

-- フード (category_id: 2)
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (2, '枝豆', 400, '採れたてを塩茹でしました。', '大豆');
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (2, '鶏の唐揚げ', 600, '特製醤油ダレに漬け込んだジューシーな一品。', '小麦, 鶏肉');
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (2, 'マルゲリータピザ', 900, 'とろーりチーズとバジルの香り。', '小麦, 乳');

-- デザート (category_id: 3)
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (3, 'バニラアイス', 300, '濃厚なバニラの香りが広がります。', '乳');
INSERT INTO products (category_id, name, price, description, allergy_info) VALUES (3, 'チョコレートパフェ', 700, 'チョコとフルーツをふんだんに使った贅沢パフェ。', '乳, 小麦, 卵');

-- テーブルデータの初期投入
INSERT INTO shop_tables (table_name) VALUES ('A-1');
INSERT INTO shop_tables (table_name) VALUES ('A-2');
INSERT INTO shop_tables (table_name) VALUES ('B-1');
INSERT INTO shop_tables (table_name) VALUES ('B-2');

SET FOREIGN_KEY_CHECKS = 1;
