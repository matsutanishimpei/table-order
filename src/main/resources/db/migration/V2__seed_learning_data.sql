INSERT INTO shop_tables (table_name, updated_by) VALUES
    ('A-1', 'system'),
    ('A-2', 'system'),
    ('B-1', 'system'),
    ('B-2', 'system');

-- 学習環境用アカウントです。本番環境では別マイグレーションや管理手順で作成してください。
INSERT INTO users (id, password, salt, role, table_id, updated_by) VALUES
    ('admin', 'KLt8mBJ4EgncZ+aHSVsFw0u3gt7Y1cUnpNOfJItSg54=', 'KKAcAppu2PCIfzZLzlCUPA==', 1, NULL, 'system'),
    ('kitchen', 'SDikYo8lbVMnxhSESZfBhjZuJqLnx/GReVs9SwN9Hac=', 'J2jFaJeWlTGG23b7OD+eIQ==', 2, NULL, 'system'),
    ('hall', 'lxaEyLs415VHzyade/lv68A6jcf7buay4oMX2jIXaJs=', 'yG8UAAovEQNTNWF80brfRA==', 3, NULL, 'system'),
    ('cashier', 'FnpWlbBeQB3ixI4CBr2HDPpk9vul9KPej0yY1jtobZY=', 'qrzqyNYbdhO4PfaCzyp4cA==', 4, NULL, 'system'),
    ('t1', 'fHYAL8bsJNGX96lGDir6aZcCF4UwDFsgm52l2ruHxWM=', 'hygMtpmqxIrxoGthZKFvzw==', 10, 1, 'system');

INSERT INTO categories (name, updated_by) VALUES
    ('ドリンク', 'system'),
    ('フード', 'system'),
    ('デザート', 'system');

INSERT INTO products (category_id, name, price, description, allergy_info, updated_by) VALUES
    (1, '生ビール', 500, '工場直送の新鮮な生ビールです。', 'なし', 'system'),
    (1, 'ウーロン茶', 300, '香り高い茶葉を使用した定番ドリンク。', 'なし', 'system'),
    (1, 'コーラ', 300, '爽快な炭酸と甘みのバランス。', 'なし', 'system'),
    (2, '枝豆', 400, '採れたてを塩茹でしました。', '大豆', 'system'),
    (2, '鶏の唐揚げ', 600, '特製醤油ダレに漬け込んだジューシーな一品。', '小麦, 鶏肉', 'system'),
    (2, 'マルゲリータピザ', 900, 'とろーりチーズとバジルの香り。', '小麦, 乳', 'system'),
    (3, 'バニラアイス', 300, '濃厚なバニラの香りが広がります。', '乳', 'system'),
    (3, 'チョコレートパフェ', 700, 'チョコとフルーツをふんだんに使った贅沢パフェ。', '乳, 小麦, 卵', 'system');
