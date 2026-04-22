package util;

/**
 * 入力値検証などのユーティリティメソッドを提供するクラスです。
 */
public class ValidationUtil {
    
    /**
     * 文字列を安全に整数に変換します。変換できない場合はデフォルト値を返します。
     * @param str 変換対象の文字列
     * @param defaultValue 変換失敗時のデフォルト値
     * @return 変換後の整数、またはデフォルト値
     */
    public static int parseIntSafe(String str, int defaultValue) {
        if (str == null || str.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(str.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }
    
    /**
     * 文字列が null または空文字かどうかを判定します。
     * @param str 判定対象の文字列
     * @return null または空文字の場合は true
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }
}
