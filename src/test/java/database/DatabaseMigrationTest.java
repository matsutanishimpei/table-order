package database;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/** Flywayが空のMySQLへスキーマと学習用データを適用できることを確認します。 */
class DatabaseMigrationTest extends BaseIntegrationTest {

    @Test
    @DisplayName("Flywayの履歴と初期データが作成されること")
    void migrationsCreateSchemaAndLearningData() throws Exception {
        try (Connection connection = DBManager.getConnection();
                Statement statement = connection.createStatement()) {
            try (ResultSet history = statement.executeQuery(
                    "SELECT COUNT(*) FROM flyway_schema_history WHERE success = 1")) {
                assertTrue(history.next());
                assertEquals(2, history.getInt(1));
            }

            try (ResultSet products = statement.executeQuery("SELECT COUNT(*) FROM products")) {
                assertTrue(products.next());
                assertTrue(products.getInt(1) > 0);
            }
        }
    }
}
