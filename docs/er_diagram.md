# データベースER図

テーブルオーダーシステムのデータベース構造を視覚化したものです。

```mermaid
erDiagram
    shop_tables ||--o{ orders : "has"
    categories ||--o{ products : "categorizes"
    products ||--o{ order_items : "contained in"
    orders ||--o{ order_items : "consists of"
    users }|--|| shop_tables : "assigned to"

    shop_tables {
        int id PK
        string table_name
    }

    users {
        string id PK
        string password
        int role
        int table_id FK
        string updated_by
    }

    categories {
        int id PK
        string name
        string updated_by
    }

    products {
        int id PK
        int category_id FK
        string name
        int price
        string description
        string allergy_info
        string image_path
        boolean is_available
        string updated_by
    }

    orders {
        int id PK
        int table_id FK
        int status
        string updated_by
        timestamp created_at
        timestamp updated_at
    }

    order_items {
        int id PK
        int order_id FK
        int product_id FK
        int quantity
        int unit_price
        int status
        string updated_by
        timestamp created_at
        timestamp updated_at
    }
```

## テーブル説明

- **users**: システムを利用するユーザー。パスワードは `salt` を併用した BCrypt でハッシュ化されます。
    - **1: 管理者 (Admin)** - システム全体の管理（商品登録、ユーザー管理など）
    - **2: キッチン (Kitchen)** - 注文確認、調理完了操作、商品の販売停止操作
    - **3: ホール (Hall)** - 配膳完了操作
    - **4: 会計 (Cashier)** - 会計処理、精算完了操作
    - **10: テーブル用端末 (Table)** - 客席からの注文操作
- **shop_tables**: 店内の座席情報。
- **categories**: 商品のジャンル（ドリンク、フード等）。
- **products**: 商品マスタ。説明文やアレルギー情報の管理が可能。キッチンの判断で販売停止（`is_available=0`）が可能。
- **orders**: 来店から会計までの「1回の訪問」を管理。
- **order_items**: 注文された個々の商品とそのステータス（調理中・配膳済等）を管理。
