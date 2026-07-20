package util;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;
import java.util.Properties;

/**
 * アプリケーション設定の読み込み方法を1か所にまとめるユーティリティです。
 *
 * <p>設定値は「環境変数 → database.properties → デフォルト値」の順で解決します。
 * 開発環境では設定ファイルを使い、本番環境では秘密情報をリポジトリやWARへ
 * 含めずに環境変数から渡せます。</p>
 */
public final class AppConfig {

    private static final String CONFIG_FILE = "database.properties";

    /**
     * クラスパス上の設定ファイルを読み込みます。
     *
     * @return 設定ファイル。ファイルがない場合は空のProperties
     * @throws IOException 設定ファイルの読み込みに失敗した場合
     */
    public static Properties loadProperties() throws IOException {
        Properties properties = new Properties();
        try (InputStream input = AppConfig.class.getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                properties.load(input);
            }
        }
        return properties;
    }

    /**
     * 環境変数を優先して設定値を取得します。
     * たとえば {@code db.mysql.pass} は {@code DB_MYSQL_PASS} で上書きできます。
     *
     * @param properties 設定ファイルから読み込んだ値
     * @param key プロパティ名
     * @param defaultValue どちらにも値がない場合の値
     * @return 解決された設定値
     */
    public static String get(Properties properties, String key, String defaultValue) {
        return resolve(properties, System.getenv(), key, defaultValue);
    }

    static String resolve(Properties properties, Map<String, String> environment,
            String key, String defaultValue) {
        String environmentValue = environment.get(toEnvironmentVariable(key));
        if (environmentValue != null && !environmentValue.isBlank()) {
            return environmentValue;
        }
        return properties.getProperty(key, defaultValue);
    }

    static String toEnvironmentVariable(String key) {
        return key.replace('.', '_').toUpperCase(java.util.Locale.ROOT);
    }

    private AppConfig() {
        // インスタンス化しない
    }
}
