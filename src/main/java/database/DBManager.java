package database;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * データベース接続を管理するクラスです。
 * 機密情報は database.properties から読み込み、HikariCPによるコネクションプーリングを使用します。
 */
public class DBManager {
    private static final Logger logger = Logger.getLogger(DBManager.class.getName());

    // プロパティファイル(database.properties)の読み込み
    private static final ResourceBundle rb = ResourceBundle.getBundle("database");

    // HikariCP データソース
    private static HikariDataSource dataSource;

    // クラスロード時に一度だけプールを初期化する
    static {
        try {
            HikariConfig config = new HikariConfig();
            
            boolean isOracle = Boolean.parseBoolean(rb.getString("db.is_oracle"));
            if (isOracle) {
                config.setDriverClassName(rb.getString("db.oracle.driver"));
                config.setJdbcUrl(rb.getString("db.oracle.url"));
                config.setUsername(rb.getString("db.oracle.user"));
                config.setPassword(rb.getString("db.oracle.pass"));
            } else {
                config.setDriverClassName(rb.getString("db.mysql.driver"));
                config.setJdbcUrl(rb.getString("db.mysql.url"));
                config.setUsername(rb.getString("db.mysql.user"));
                config.setPassword(rb.getString("db.mysql.pass"));
            }

            // --- HikariCP プーリング設定 ---
            // 接続プール内の最大コネクション数。高負荷時でもこれ以上は生成されない。
            config.setMaximumPoolSize(20);
            // プール内で待機させる最小コネクション数。
            config.setMinimumIdle(5);
            // アイドル状態のコネクションが保持される最大時間 (ミリ秒) - 5分
            config.setIdleTimeout(300000);
            // コネクションの取得を待機する最大時間 (ミリ秒) - 20秒
            config.setConnectionTimeout(20000);
            // キャッシュ機能の有効化によるパフォーマンス最適化 (MySQL推奨設定)
            config.addDataSourceProperty("cachePrepStmts", "true");
            config.addDataSourceProperty("prepStmtCacheSize", "250");
            config.addDataSourceProperty("prepStmtCacheSqlLimit", "2048");
            
            dataSource = new HikariDataSource(config);
            logger.info("HikariCP Connection Pool initialized successfully.");

        } catch (Exception e) {
            logger.log(Level.SEVERE, "HikariCP の初期化に失敗しました。データベースプロパティの設定に誤りがあるか、ドライバが見つかりません。", e);
            throw new ExceptionInInitializerError(e);
        }
    }

    /**
     * データベースへの接続を（プールから）取得します。
     * 
     * @return Connectionオブジェクト。失敗した場合はnullを返します。
     */
    public static Connection getConnection() {
        try {
            return dataSource.getConnection();
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "データベースプーリングからの接続取得に失敗しました。", e);
            return null;
        }
    }

    /**
     * アプリケーション共通の秘密鍵（Pepper）を返します。
     * @return Pepper文字列
     */
    public static String getPepper() {
        return rb.getString("app.security.pepper");
    }

    /**
     * データベース接続を閉じます（実際はプールへ返却されます）。
     * 
     * @param con Connectionオブジェクト
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "データベース接続のクローズ(プール返却)中にエラーが発生しました。", e);
            }
        }
    }

    /**
     * アプリケーション終了時にコネクションプールを破棄します。
     * （ServletContextListener などから呼び出すことを想定）
     */
    public static void closePool() {
        if (dataSource != null && !dataSource.isClosed()) {
            dataSource.close();
            logger.info("HikariCP Connection Pool destroyed.");
        }
    }
}
