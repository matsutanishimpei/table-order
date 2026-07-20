package database;

import org.junit.jupiter.api.BeforeAll;
import org.testcontainers.containers.MySQLContainer;
import org.testcontainers.ext.ScriptUtils;
import org.testcontainers.jdbc.JdbcDatabaseDelegate;

/**
 * Testcontainersを利用した統合テストのベースクラスです。
 * 全ての統合テストはこのクラスを継承することで、本物のMySQLコンテナに対してテストを実行できます。
 * シングルトンパターンにより、全テストクラスで1つのコンテナを共有し、高速化を図っています。
 */
@SuppressWarnings("resource")
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

    @BeforeAll
    static void setUpDatabase() {
        // DBManager初期化時にFlywayがスキーマと初期データを適用する
        DBManager.initForTest(
            mysql.getJdbcUrl(),
            mysql.getUsername(),
            mysql.getPassword()
        );

        // 各テストクラスを同じ初期データから開始できるように戻す
        try (java.sql.Connection con = DBManager.getConnection()) {
            con.createStatement().execute("SET FOREIGN_KEY_CHECKS = 0");
            con.createStatement().execute("TRUNCATE TABLE audit_log");
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

        JdbcDatabaseDelegate delegate = new JdbcDatabaseDelegate(mysql, "");
        ScriptUtils.runInitScript(delegate, "db/migration/V2__seed_learning_data.sql");
    }
}
