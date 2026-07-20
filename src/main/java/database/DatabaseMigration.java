package database;

import javax.sql.DataSource;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.output.MigrateResult;
import lombok.extern.slf4j.Slf4j;

/**
 * Flywayを使って未適用のDBマイグレーションを実行します。
 *
 * <p>SQLは{@code src/main/resources/db/migration}へ追加します。
 * 一度適用したファイルは変更せず、スキーマ変更ごとに次のバージョンのSQLを
 * 追加することで、環境ごとの差異と手作業を減らします。</p>
 */
@Slf4j
public final class DatabaseMigration {

    private static final String MIGRATION_LOCATION = "classpath:db/migration";

    /**
     * 指定されたデータソースへ未適用のマイグレーションを適用します。
     *
     * @param dataSource マイグレーション対象のデータソース
     */
    public static void migrate(DataSource dataSource) {
        Flyway flyway = Flyway.configure()
                .dataSource(dataSource)
                .locations(MIGRATION_LOCATION)
                .validateMigrationNaming(true)
                .load();

        MigrateResult result = flyway.migrate();
        log.info("Database migration completed: schemaVersion={}, migrationsExecuted={}",
                result.targetSchemaVersion, result.migrationsExecuted);
    }

    private DatabaseMigration() {
        // インスタンス化しない
    }
}
