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
 */
@Testcontainers
public abstract class BaseIntegrationTest {

    @Container
    protected static final MySQLContainer<?> mysql = new MySQLContainer<>("mysql:8.0")
            .withDatabaseName("table_order_test")
            .withUsername("testuser")
            .withPassword("testpass");

    @BeforeAll
    static void setUpDatabase() {
        // DBManagerをコンテナの接続情報で初期化
        DBManager.initForTest(
            mysql.getJdbcUrl(),
            mysql.getUsername(),
            mysql.getPassword()
        );

        // スキーマの初期化
        JdbcDatabaseDelegate containerDelegate = new JdbcDatabaseDelegate(mysql, "");
        ScriptUtils.runInitScript(containerDelegate, "sql/schema.sql");
        ScriptUtils.runInitScript(containerDelegate, "sql/seed.sql");
    }
}
