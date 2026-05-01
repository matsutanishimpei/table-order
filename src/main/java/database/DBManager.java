package database;

import java.sql.Connection;
import java.sql.SQLException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;
import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import lombok.extern.slf4j.Slf4j;

/**
 * データベース接続を管理するクラスです。
 */
@Slf4j
public class DBManager {

    private static HikariDataSource dataSource;
    private static String pepper;

    static {
        try {
            initialize();
        } catch (Exception e) {
            log.error("DBManagerの静的初期化中に予期せぬエラーが発生しました。クラスはロードされますが、接続には initForTest() による再初期化が必要です。", e);
        }
    }

    private static synchronized void initialize() {
        if (dataSource != null) {
            return;
        }

        // 設定ファイルの読み込み
        try (InputStream is = DBManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            Properties props = new Properties();
            if (is == null) {
                log.warn("database.properties が見つかりません。デフォルト設定を使用します。");
            } else {
                props.load(is);
            }

            pepper = props.getProperty("app.security.pepper", "test-pepper");

            HikariConfig config = new HikariConfig();
            boolean isOracle = Boolean.parseBoolean(props.getProperty("db.is_oracle", "false"));
            if (isOracle) {
                config.setJdbcUrl(props.getProperty("db.oracle.url"));
                config.setUsername(props.getProperty("db.oracle.user"));
                config.setPassword(props.getProperty("db.oracle.pass"));
            } else {
                config.setJdbcUrl(props.getProperty("db.mysql.url"));
                config.setUsername(props.getProperty("db.mysql.user"));
                config.setPassword(props.getProperty("db.mysql.pass"));
            }
            config.setDriverClassName(props.getProperty(isOracle ? "db.oracle.driver" : "db.mysql.driver",
                    isOracle ? "oracle.jdbc.OracleDriver" : "com.mysql.cj.jdbc.Driver"));

            config.setMaximumPoolSize(10);
            config.setConnectionTimeout(3000); // 初期化チェック用にタイムアウトを短縮

            try {
                dataSource = new HikariDataSource(config);
            } catch (Exception e) {
                log.warn("デフォルト設定でのデータソース初期化に失敗しました。DBが未起動または設定が誤っている可能性があります。: {}", e.getMessage());
            }
        } catch (IOException e) {
            log.error("設定ファイルの読み込みに失敗しました", e);
        }
    }

    /**
     * テスト用にデータソースを再初期化します（Testcontainers用）。
     *
     * @param url 接続URL
     * @param user ユーザ名
     * @param password パスワード
     */
    public static synchronized void initForTest(String url, String user, String password) {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
        }

        HikariConfig config = new HikariConfig();
        config.setJdbcUrl(url);
        config.setUsername(user);
        config.setPassword(password);
        config.setDriverClassName("com.mysql.cj.jdbc.Driver");

        config.setMaximumPoolSize(5);
        config.setConnectionTimeout(30000);

        dataSource = new HikariDataSource(config);

        // Pepperが未設定の場合はデフォルト値を設定
        if (pepper == null) {
            pepper = "test-pepper";
        }
    }

    /**
     * データベースへの接続を取得します。
     * プールからコネクションを払い出します。
     *
     * @return Connectionオブジェクト
     * @throws SQLException 接続失敗時
     */
    public static Connection getConnection() throws SQLException {
        return dataSource.getConnection();
    }

    /**
     * パスワードハッシュ化用の共通ソルト(Pepper)を取得します。
     *
     * @return Pepper文字列
     */
    public static String getPepper() {
        return pepper;
    }

    /**
     * コネクションをクローズします（ユーティリティ）。
     *
     * @param con Connectionオブジェクト
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                log.warn("コネクションのクローズ中にエラーが発生しました", e);
            }
        }
    }

    private DBManager() {
        // インスタンス化防止
    }
}
