# Table Order System: 設計思想 (Design Philosophy)

本プロジェクトにおける、開発および今後のAIエージェントによるコード生成で遵守すべきアーキテクチャ・設計に関する基本原則とガイドラインです。

## 1. アーキテクチャの基本方針 (Architectural Principles)
- **関心の分離 (Separation of Concerns)**:
  - **Servlet レイヤー**: リクエストパラメータの収集、入力チェック（簡易）、例外のキャッチ、レスポンス（JSPへのフォワード/リダイレクト）といった「入出力の制御」に専念します。
  - **Service レイヤー**: アプリケーションのビジネスロジックをカプセル化します。複雑な業務ルールやバリデーション（在庫チェックなど）はすべてここに集約します。
  - **DAO レイヤー**: データベースへのCRUD操作のみを担当し、業務ルールは含めません。
- **依存性の注入 (Dependency Injection)**:
  - `ServiceFactory` パターン等を導入し、コンポーネント間の結合度を下げます。サーブレット内で具体的な実装クラス（`new XXXServiceImpl()`）を直接インスタンス化せず、Factory経由でインターフェースとして取得します。
  - （テスト容易性の確保のため、DIコンストラクタも併記します）

## 2. モダンな Java 機能の活用 (Modern Java Features)
- **不変性 (Immutability) の徹底**:
  - DTOやデータモデル（`User`, `Product`, `Category`, `CartItem`, `TableOrderSummary` など）は、原則として Java 17 の **`record`** を使用し、言語レベルでの不変性を保証します。
- **宣言的プログラミング**:
  - コレクションの変換やフィルタリング操作には従来の `for` ループではなく、**Stream API** を積極的に活用し、意図が伝わりやすい宣言的なコードを記述します。

## 3. エラー・例外ハンドリング (Exception Handling)
- **階層化・役割の明確化**:
  - `BaseException` (RuntimeException) を頂点とした例外の階層化を行います。
  - **`BusinessException`**: ユーザー起因または業務ルール違反のエラー（バリデーション失敗、権限不足、在庫不足等）。HTTP 400系で返し、ユーザーへ適切なメッセージを通知します。
  - **`SystemException`**: システム起因の致命的なエラー（データベース接続エラー等）。HTTP 500系で返し、ログ出力と管理者への通知を行います。
- **一元管理**:
  - `BaseServlet` などの基底クラスで共通の例外ハンドリングを行い、エラー時の振る舞いを統一します。

## 4. セキュリティと堅牢性 (Security & Robustness)
- **セキュアなパスワード管理**:
  - SHA-256などの古いハッシュアルゴリズムを廃止し、**BCrypt (with Pepper)** を用いて強力な暗号化を行います。（既存ユーザーはログイン時に透過的に移行）
- **SQLインジェクションの完全排除**:
  - 全てのクエリで `PreparedStatement` のプレースホルダ (`?`) を使用します。
  - SQL文は `SqlConstants` などの定数クラスに集約し、コード内で文字列結合による動的なSQL生成は禁止（コーディング規約）とします。

## 5. 保守性と品質保証 (Maintainability & QA)
- **ビルド・プロジェクト管理**:
  - レガシーな構成から **Maven** をベースとした標準ディレクトリ構成 (`src/main/java`, `src/main/resources`, `src/test/java`) で管理します。
- **ドキュメンテーション (Javadoc)**:
  - 全てのパブリックAPI（Service, DAOのインターフェース）および基盤クラスには、`@param`, `@return`, `@throws` を含む完全な Javadoc を付与し、意図と仕様を明確にします。
- **テストとCI/CD**:
  - JUnit 5 を用いたテスト（特にService層のビジネスロジックやDAOのインテグレーション）を書き、GitHub Actionsなどを用いた自動テスト・デプロイ環境で品質を担保します。
