package database;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

/**
 * Testcontainersを利用した統合テストのベースクラスです。
 * 全ての統合テストはこのクラスを継承することで、本物のMySQLコンテナに対してテストを実行できます。
 * シングルトンパターンにより、全テストクラスで1つのコンテナを共有し、高速化を図っています。
 */
public abstract class BaseIntegrationTest {

    protected static final MySQLContainer<?> mysql;

    static {
        mysql = new MySQLContainer<>("mysql:8.0")
                .withDatabaseName("table_order_test")
                .withUsername("testuser")
                .withPassword("testpass");
        mysql.start();
        
        // JVM終了時にコンテナを停止する（任意だが推奨）
        Runtime.getRuntime().addShutdownHook(new Thread(mysql::stop));
    }

    private static boolean schemaInitialized = false;

    @BeforeAll
    static void setUpDatabase() {
        // DBManagerをコンテナの接続情報で初期化
        DBManager.initForTest(
            mysql.getJdbcUrl(),
            mysql.getUsername(),
            mysql.getPassword()
        );

        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(mysql, "");
        if (!schemaInitialized) {
            // スキーマ作成は最初の1回だけ
            ScriptUtils.runInitScript(containerDelegate, "sql/schema.sql");
            schemaInitialized = true;
        }
        // データ（seed）は各クラスごとに初期状態に戻す（または各テストで制御）
        // 重複挿入を避けるため、一旦全テーブルをクリアしてから seed を流す
        try (java.sql.Connection con = DBManager.getConnection()) {
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            con.createStatement().execute("TRUNCATE TABLE order_items");
            con.createStatement().execute("TRUNCATE TABLE orders");
            con.createStatement().execute("TRUNCATE TABLE products");
            con.createStatement().execute("TRUNCATE TABLE categories");
            con.createStatement().execute("TRUNCATE TABLE users");
            con.createStatement().execute("TRUNCATE TABLE shop_tables");
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 1");
        } catch (java.sql.SQLException e) {
            throw new RuntimeException(e);
        }
        ScriptUtils.runInitScript(containerDelegate, "sql/seed.sql");
    }
}
