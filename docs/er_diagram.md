# データベースER図

テーブルオーダーシステムのデータベース構造を視覚化したものです。

```mermaid
erDiagram
    users ||--o| shop_tables : "identifies (for tables)"
    categories ||--o{ products : "contains"
    shop_tables ||--o{ orders : "places"
    orders ||--o{ order_items : "consists of"
    products ||--o{ order_items : "ordered as"

    users {
        string id PK
        string password
        int role
        int table_id FK
    }

    shop_tables {
        int id PK
        string table_name
    }

    categories {
        int id PK
        string name
    }

    products {
        int id PK
        int category_id FK
        string name
        int price
        string image_path
        boolean is_available
    }

    orders {
        int id PK
        int table_id FK
        int status
        datetime created_at
        datetime updated_at
    }

    order_items {
        int id PK
        int order_id FK
        int product_id FK
        int quantity
        int unit_price
        int status
        datetime created_at
    }
```

## テーブル説明

- **users**: システムを利用するユーザー。`role` カラムにより以下の権限を区別します。
    - **1: 管理者 (Admin)** - システム全体の管理（商品登録、ユーザー管理など）
    - **2: キッチン (Kitchen)** - 注文確認、調理完了操作、商品の販売停止操作
    - **3: ホール (Hall)** - 配膳完了操作
    - **4: 会計 (Cashier)** - 会計処理、精算完了操作
    - **10: テーブル用端末 (Table)** - 客席からの注文操作
- **shop_tables**: 店内の座席情報。
- **categories**: 商品のジャンル（ドリンク、フード等）。
- **products**: 商品マスタ。キッチンの判断で販売停止（`is_available=0`）が可能。
- **orders**: 来店から会計までの「1回の訪問」を管理。
- **order_items**: 注文された個々の商品とそのステータス（調理中・配膳済等）を管理。
