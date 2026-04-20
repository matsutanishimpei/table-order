package util;

/**
 * 入力値の検証や安全な型変換を行うユーティリティクラスです。
 */
public class ValidationUtil {

    /**
     * 文字列を整数に変換します。失敗した場合は defaultValue を返します。
     * @param value 変換対象の文字列
     * @param defaultValue 失敗時のデフォルト値
     * @return 変換後の整数
     */
    public static int parseIntSafe(String value, int defaultValue) {
        if (value == null || value.trim().isEmpty()) {
            return defaultValue;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return defaultValue;
        }
    }

    /**
     * 文字列を整数に変換します。失敗した場合は null を返します。
     * @param value 変換対象の文字列
     * @return 変換後の整数、または null
     */
    public static Integer parseIntOrNull(String value) {
        if (value == null || value.trim().isEmpty()) {
            return null;
        }
        try {
            return Integer.parseInt(value.trim());
        } catch (NumberFormatException e) {
            return null;
        }
    }

    /**
     * 文字列が空（null、空文字、空白のみ）でないかチェックします。
     * @param value チェック対象
     * @return 空でない場合は true
     */
    public static boolean isNotBlank(String value) {
        return value != null && !value.trim().isEmpty();
    }

    /**
     * 文字列が指定された最大長以内であるかチェックします。
     * @param value チェック対象
     * @param maxLength 最大長
     * @return 範囲内（または null）の場合は true
     */
    public static boolean isWithinLength(String value, int maxLength) {
        if (value == null) {
            return true;
        }
        return value.length() <= maxLength;
    }
}
