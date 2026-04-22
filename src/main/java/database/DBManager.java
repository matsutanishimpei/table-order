package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.io.InputStream;
import java.util.Properties;

/**
 * データベース接続を管理するクラスです。
 */
public class DBManager {
    private static String url;
    private static String user;
    private static String pass;
    private static String pepper;

    static {
        // 設定ファイルの読み込み
        try (InputStream is = DBManager.class.getClassLoader().getResourceAsStream("database.properties")) {
            if (is == null) {
                throw new RuntimeException("database.properties が見つかりません。src/main/resources に配置されているか確認してください。");
            }
            Properties props = new Properties();
            props.load(is);

            boolean isOracle = Boolean.parseBoolean(props.getProperty("db.is_oracle", "false"));
            if (isOracle) {
                url = props.getProperty("db.oracle.url");
                user = props.getProperty("db.oracle.user");
                pass = props.getProperty("db.oracle.pass");
                Class.forName(props.getProperty("db.oracle.driver", "oracle.jdbc.OracleDriver"));
            } else {
                url = props.getProperty("db.mysql.url");
                user = props.getProperty("db.mysql.user");
                pass = props.getProperty("db.mysql.pass");
                Class.forName(props.getProperty("db.mysql.driver", "com.mysql.cj.jdbc.Driver"));
            }
            
            pepper = props.getProperty("app.security.pepper");

        } catch (Exception e) {
            e.printStackTrace();
            throw new RuntimeException("DBManagerの初期化に失敗しました: " + e.getMessage(), e);
        }
    }

    /**
     * データベースへの接続を取得します。
     * @return Connectionオブジェクト
     * @throws SQLException 接続失敗時
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, user, pass);
    }

    /**
     * パスワードハッシュ化用の共通ソルト(Pepper)を取得します。
     * @return Pepper文字列
     */
    public static String getPepper() {
        return pepper;
    }

    /**
     * コネクションをクローズします（ユーティリティ）。
     * @param con Connectionオブジェクト
     */
    public static void closeConnection(Connection con) {
        if (con != null) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }
}
