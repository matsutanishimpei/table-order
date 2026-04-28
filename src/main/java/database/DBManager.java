package database;

import java.sql.Connection;
import java.sql.SQLException;
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
        // 設定ファイルの読み込み
        try (InputStream is = DBManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new RuntimeException("database.properties が見つかりません。src/main/resources に配置されているか確認してください。");
            }
            Properties props = new Properties();
            props.load(is);

            HikariConfig config = new HikariConfig();
            
            boolean isOracle = Boolean.parseBoolean(props.getProperty("db.is_oracle", "false"));
            if (isOracle) {
                config.setJdbcUrl(props.getProperty("db.oracle.url"));
                config.setUsername(props.getProperty("db.oracle.user"));
                config.setPassword(props.getProperty("db.oracle.pass"));
                config.setDriverClassName(props.getProperty("db.oracle.driver", "oracle.jdbc.OracleDriver"));
            } else {
                config.setJdbcUrl(props.getProperty("db.mysql.url"));
                config.setUsername(props.getProperty("db.mysql.user"));
                config.setPassword(props.getProperty("db.mysql.pass"));
                config.setDriverClassName(props.getProperty("db.mysql.driver", "com.mysql.cj.jdbc.Driver"));
            }
            
            // プーリングの詳細設定
            config.setMaximumPoolSize(10);
            config.setMinimumIdle(2);
            config.setConnectionTimeout(30000); // 30秒
            config.setIdleTimeout(600000);      // 10分
            config.setMaxLifetime(1800000);     // 30分
            
            dataSource = new HikariDataSource(config);
            
            pepper = props.getProperty("app.security.pepper");

        } catch (Exception e) {
            log.error("DBManagerの初期化に失敗しました", e);
            throw new RuntimeException("DBManagerの初期化に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * テスト用にデータソースを再初期化します（Testcontainers用）。
     *
     * @param url 接続URL
     * @param user ユーザ名
     * @param password パスワード
     */
    public static void initForTest(String url, String user, String password) {
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
}
