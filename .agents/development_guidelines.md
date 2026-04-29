# 開発・修正ガイドライン (Table Order System)

このプロジェクトで修正や機能追加を行う際に、AIエージェントが必ず確認し、遵守すべき事項をまとめます。

## 1. 定数・設定の参照
推測で定数名や値を使用せず、必ず以下のクラスを参照すること。
- **`util.AppConstants`**: セッション属性名、リダイレクトパスなどのシステム共通設定。
- **`model.OrderConstants`**: 注文・明細のステータス値。
- **`model.UserConstants`**: ユーザー権限（ロール）のID値。
- **`database.SqlConstants`**: データベース操作で使用するすべてのSQLクエリ。

## 2. 品質ゲートと静的解析
ビルド（`mvn verify`）が常に成功することを確認し、以下の警告を放置しないこと。

### SpotBugs (静的解析)
- **`EI_EXPOSE_STATIC_REP2`**: 可変オブジェクトを直接 static フィールドに格納する際に発生。
  - 対応策: `edu.umd.cs.findbugs.annotations.SuppressFBWarnings` をクラスまたはメソッドに付与する。
- **`MS_EXPOSE_REP`**: 内部の可変オブジェクトを外部に露出させる際に発生。
  - 対応策: 防御的コピーを行うか、アノテーションで抑制する。

### Checkstyle (コーディング規約)
- Javadoc の `@param` や `@return` の前には必ず空行を入れること。
- インポートの順序や未使用インポートに注意すること。

### JaCoCo (テストカバレッジ)
- **目標**: `util`, `service`, `service.impl`, `controller`, `database` の各パッケージで **80% 以上**。
- `record` クラスや定数クラスも、コンストラクタやアクセサをテストしてカバレッジを稼ぐこと。

## 3. Java 構文・コーディング規則
- **アノテーションの制限**: `static { ... }` (静的初期化ブロック) に対して直接アノテーションを付与することはできない。クラスレベルまたはフィールドレベルに付与すること。
- **レコード (Records)**: Java 17 のレコードを積極的に活用し、アクセサ（`id()` 等）を正しく使用すること。
- **防御的コピー**: `Timestamp` や `List` などの可変オブジェクトをコンストラクタやアクセサで扱う際は、必ず `new Timestamp(...)` や `List.copyOf(...)` による防御的コピーを行うこと。

## 4. テスト実装
- **統合テスト**: データベースが絡むテストは `database.BaseIntegrationTest` を継承し、Testcontainers (MySQL) を利用すること。
- **単体テスト**: 外部依存（Cloudinary 等）がある場合は Mockito を使用して分離すること。
- **リソース管理**: `MySQLContainer` などの Closeable なリソースを扱う際は、リソースリークの警告が出ないよう適切に管理・抑制（`@SuppressWarnings("resource")`）すること。

## 5. ドキュメントと図の保守
- **Javadoc**: 
  - パブリックなクラス・インターフェース・メソッドには必ず Javadoc を付与すること。
  - `@param`, `@return`, `@throws` などのブロックタグには説明を記述し、その前には空行を入れること（Checkstyle遵守）。
- **アーキテクチャ図**:
  - データベーススキーマに変更を加えた場合は、`.agents/architecture.md` の **ER図 (Mermaid)** を必ず更新すること。
  - クラス構造やレイヤー間の依存関係に大きな変更があった場合は、**クラス図 (Mermaid)** も更新すること。

## 6. DRY原則とデータベースアクセス
- **定型処理の排除**: 
  - JDBC の接続管理、Statement 作成、ResultSet ループ、例外処理などの定型処理を各 DAO に記述しないこと（DRY原則）。
  - データベース操作には必ず **`database.JdbcExecutor`** を使用し、ボイラープレートコードを最小化すること。
- **RowMapper の活用**:
  - `ResultSet` からオブジェクトへのマッピングには `database.RowMapper` インターフェースを使用し、ロジックの再利用性を高めること。
- **トランザクション管理**:
  - 複数テーブルにまたがる操作は、`database.TransactionManager.execute` を使用してサービス層で制御すること。その際、DAO メソッドには `java.sql.Connection` を引き渡して処理を継続すること。
