package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * データベース接続を管理するクラスです。
 * 機密情報は database.properties から読み込みます。
 */
public class DBManager {
    private static final Logger logger = Logger.getLogger(DBManager.class.getName());

    // プロパティファイル(database.properties)の読み込み
    private static final ResourceBundle rb = ResourceBundle.getBundle("database");

    // 接続先を切り替えるフラグ (true: Oracle, false: MySQL)
    private static final boolean IS_ORACLE = Boolean.parseBoolean(rb.getString("db.is_oracle"));

    // Oracle 設定
    private static final String ORACLE_DRIVER = rb.getString("db.oracle.driver");
    private static final String ORACLE_URL = rb.getString("db.oracle.url");
    private static final String ORACLE_USER = rb.getString("db.oracle.user");
    private static final String ORACLE_PASS = rb.getString("db.oracle.pass");

    // MySQL 設定
    private static final String MYSQL_DRIVER = rb.getString("db.mysql.driver");
    private static final String MYSQL_URL = rb.getString("db.mysql.url");
    private static final String MYSQL_USER = rb.getString("db.mysql.user");
    private static final String MYSQL_PASS = rb.getString("db.mysql.pass");

    /**
     * データベースへの接続を取得します。
     * 
     * @return Connectionオブジェクト。失敗した場合はnullを返します。
     */
    public static Connection getConnection() {
        Connection con = null;
        try {
            if (IS_ORACLE) {
                // Oracle JDBCドライバのロード
                Class.forName(ORACLE_DRIVER);
                // データベース接続の確立
                con = DriverManager.getConnection(ORACLE_URL, ORACLE_USER, ORACLE_PASS);
            } else {
                // MySQL JDBCドライバのロード
                Class.forName(MYSQL_DRIVER);
                // データベース接続の確立
                con = DriverManager.getConnection(MYSQL_URL, MYSQL_USER, MYSQL_PASS);
            }
        } catch (ClassNotFoundException e) {
            logger.log(Level.SEVERE, "JDBCドライバのロードに失敗しました。", e);
        } catch (SQLException e) {
            logger.log(Level.SEVERE, "データベース接続の取得に失敗しました。", e);
        }
        return con;
    }

    /**
     * アプリケーション共通の秘密鍵（Pepper）を返します。
     * @return Pepper文字列
     */
    public static String getPepper() {
        return rb.getString("app.security.pepper");
    }

    /**
     * データベース接続を閉じます。
     * 
     * @param con Connectionオブジェクト
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                logger.log(Level.SEVERE, "データベース接続のクローズ中にエラーが発生しました。", e);
            }
        }
    }
}
