package util;

import jakarta.servlet.http.Part;
import java.util.Arrays;
import java.util.List;

/**
 * 入力値検証などのユーティリティメソッドを提供するクラスです。
 */
public class ValidationUtil {
    
    /**
     * 文字列を安全に整数に変換します。変換できない場合はデフォルト値を返します。
     *
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
     * 文字列が null または空文字（空白のみを含む）かどうかを判定します。
     */
    public static boolean isNullOrEmpty(String str) {
        return str == null || str.trim().isEmpty();
    }

    private static final List<String> ALLOWED_IMAGE_TYPES =
            Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif");

    /**
     * 必須チェックを行います。
     */
    public static ValidationResult validateRequired(String value, String fieldName) {
        if (value == null || value.trim().isEmpty()) {
            return ValidationResult.failure(fieldName + "は必須入力です。");
        }
        return ValidationResult.success();
    }

    /**
     * 最大文字数チェックを行います。
     */
    public static ValidationResult validateMaxLength(String value, int max, String fieldName) {
        if (value != null && value.length() > max) {
            return ValidationResult.failure(fieldName + "は" + max + "文字以内で入力してください。");
        }
        return ValidationResult.success();
    }

    /**
     * 正の整数チェックを行います。
     */
    public static ValidationResult validatePositive(int value, String fieldName) {
        if (value <= 0) {
            return ValidationResult.failure(fieldName + "には正の整数を入力してください。");
        }
        return ValidationResult.success();
    }

    /**
     * アップロードされたファイルが許可された画像形式かどうかを検証します。
     */
    public static ValidationResult validateImage(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return ValidationResult.failure("画像ファイルを選択してください。");
        }

        String contentType = filePart.getContentType();
        if (contentType == null || !ALLOWED_IMAGE_TYPES.contains(
                contentType.toLowerCase(java.util.Locale.ROOT))) {
            return ValidationResult.failure("許可されていないファイル形式です。"
                    + "JPEG, PNG, WEBP, GIFのみアップロード可能です。");
        }

        return ValidationResult.success();
    }

    /**
     * アップロードされたファイルが画像形式として妥当か判定します（互換性用）。
     */
    public static boolean isValidImage(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return false;
        }
        String contentType = filePart.getContentType();
        return contentType != null && ALLOWED_IMAGE_TYPES.contains(
                contentType.toLowerCase(java.util.Locale.ROOT));
    }
}
