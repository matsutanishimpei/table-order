# Table Order & Store Management System

![Java CI](https://github.com/matsutanishimpei/table-order/actions/workflows/ci.yml/badge.svg)
![Java 17](https://img.shields.io/badge/Java-17-orange?logo=openjdk&logoColor=white)
![Maven](https://img.shields.io/badge/Maven-3.9-C71A36?logo=apache-maven&logoColor=white)
![MySQL](https://img.shields.io/badge/MySQL-8.0-4479A1?logo=mysql&logoColor=white)
![Cloudinary](https://img.shields.io/badge/Cloudinary-Managed-3448C5?logo=cloudinary&logoColor=white)
![Docker](https://img.shields.io/badge/Docker-Supported-2496ED?logo=docker&logoColor=white)

Java 17 と Jakarta Servlet を基盤とした、テーブルオーダー・店舗管理システムです。モバイル注文から厨房管理、売上分析までを題材に、Cloudinary による画像管理や BCrypt を用いた認証を実装しています。CI では JUnit、Checkstyle、SpotBugs、JaCoCo、OWASP Dependency-Check を実行し、テストと静的解析の結果を継続的に確認します。

## 📘 学習者向けガイド

MVC と Java Servlet の基礎を学び終えた方は、[コードリーディング学習ロードマップ](docs/学習ロードマップ.md)から始めてください。ログインから注文、厨房、配膳、会計までを実際のコードに沿って1周し、Controller・Service・DAO・DBのつながりを段階的に理解できる構成です。

アプリケーションを1周した後は、[運用基礎の学習ガイド](docs/運用学習ガイド.md)で、DBマイグレーション、秘密情報、スモークテスト、ヘルスチェック、ログを学べます。運用機能はコアのMVCコードと分離してあり、必要な段階で読み進められます。教材の見通しを守るために意図的に採用していない技術と、その判断条件も同ガイドへ記録しています。

## 🌟 主な機能

### 1. お客様用モバイル注文 (Menu & Details)
- **ページ遷移型の商品詳細**: モーダルから専用ページ遷移へ刷新。100文字の長文名や、詳細な商品説明、アレルギー情報を確実に閲覧可能。
- **注文履歴の確認 (History)**: カート内の合計だけでなく、これまでの注文履歴と総額をいつでも確認可能。
- **プレミアムな操作感**: 確定ボタンの質感、ホバー効果、視認性の高いレイアウトなど、高級感を追求。

### 2. 店舗運営支援
- **キッチンボード (Kitchen)**: 調理待ちアイテムを一覧表示。調理完了をワンタップでホールに通知。
- **配膳管理 (Hall)**: 配膳が必要なテーブルと商品をリアルタイムに把握。
- **レジ精算 (Cashier)**: テーブルごとの注文明細を自動集計。未精算額のリアルタイム算出に対応。

### 3. 管理者ダッシュボード (Admin)
- **プレミアム・エメラルド・テーマ**: 管理・運営向け画面を一貫した高品質なライトテーマへ統一。
- **フロア監視ボード**: 店舗全体の稼働状況をリアルタイムで俯瞰。
- **商品情報の完全な CRUD 管理**: メニュー名、価格、カテゴリーに加え「商品説明」「アレルギー情報」「販売状態」を自在に編集・更新可能。
- **売上統計分析**: 累計売上、日次トレンド、商品別ランキングの可視化。

## 📂 プロジェクト構成 (Maven Layout)
標準的な Maven 構成を採用し、保守性と拡張性を高めています。

- `src/main/java`: バックエンド・ソースコード (Servlet, Service, DAO, Util)
- `src/main/resources`: 設定とFlywayマイグレーション
- `src/main/webapp`: フロントエンド資産 (JSP, CSS, SQL スクリプト)
- `src/test/java`: テストコード
- `pom.xml`: 依存関係管理・ビルド構成

## 🎨 デザインシステムと最新アーキテクチャ
Tailwind CSS を全面採用し、従来の静的CSS設計から、柔軟で保守性の高いユーティリティファースト設計へと進化しました。

- **Tailwind CSS & Common Components**: `header.jsp` による共通設定と Tailwind CSS (Play CDN) の活用により、全12画面で寸分違わぬ一貫したプレミアム・デザインを実現。
- **Maven Project**: Maven による標準的な依存関係管理を採用。ライブラリのバージョン管理やビルド・テストが自動化されています。
- **堅牢なバックエンド (MVC, Service層)**: コントローラーとDAOの中間に **OrderService** 等のビジネスロジック層を新設。トランザクション管理をアプリケーション層で正確に制御します。
- **Premium Emerald Theme**: 清潔感と高級感を両立させたエメラルド・アクセントのライトテーマ。グラスモーフィズム、カードシャドウ、マイクロアニメーションを駆使した直感的な UI。

## 🛡️ システムの堅牢性・安全性
商用レベルの運用に耐えうる、高度なセキュリティ基盤を構築しています。

- **多層防御のパスワード保護と BCrypt**: 業界標準の **BCrypt** へパスワードハッシュを完全移行。旧ハッシュ(SHA-256)からの「オンザフライ・マイグレーション」により、ユーザーに意識させることなくダウンタイム・ゼロでセキュリティ規格を引き上げました。
- **Webセキュリティ標準**: XSS, CSRF (`CsrfUtil`による全POST保護), セッション固定攻撃の防止策を網羅。
- **ロールベースアクセス制御**: `AuthFilter` による厳格な権限・セッション管理。
- **外部ストレージの抽象化**: `ImageStorageProvider` インターフェースにより、**Cloudinary** 等のクラウドストレージと密結合することなく安全に画像を管理。

## 🚀 技術スタック
- **Backend**: Java 17 (Jakarta EE 10), Servlet/JSP, **Maven**
- **Frontend**: Tailwind CSS (Play CDN), JSTL, Google Fonts (Inter, Outfit)
- **Library**: **HikariCP** (CP), **jBCrypt** (Hash), **Cloudinary Java SDK**, **MySQL Connector/J**, **Lombok**, **JUnit 5**, **Mockito**
- **CI/CD**: GitHub Actions (Java CI with Maven) - 自動テスト、静的解析、依存関係スキャン、80% のカバレッジ基準を検証。

## 🛠️ セットアップ方法

1. **前提条件**: JDK 17+, Apache Tomcat 10+, Maven, MySQL 8.0+
2. **設定の準備**:
   - ローカルでは`database.properties.template`を`database.properties`へコピー。
   - 本番ではDBパスワードやPepperを環境変数で設定。
3. **データベース構築**: 起動時にFlywayが未適用のSQLを自動適用。
4. **ビルド**: `mvn clean package` を実行。
5. **デプロイ**: 生成された `target/table-order.war` を Tomcat の `webapps` に配置。

DBを変更するときは、適用済みSQLを編集せず、`src/main/resources/db/migration`へ次のバージョンのSQLを追加します。環境変数と運用確認手順は[運用基礎の学習ガイド](docs/運用学習ガイド.md)にまとめています。

---

Developed & Refactored by Antigravity (Advanced Agentic Coding AI)
