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

    private static final List<String> ALLOWED_IMAGE_TYPES = Arrays.asList("image/jpeg", "image/png", "image/webp", "image/gif");

    /**
     * アップロードされたファイルが許可された画像形式かどうかを検証します。
     * @param filePart 検証対象のPart
     * @return 許可された画像形式（JPEG, PNG, WEBP, GIF）であればtrue
     */
    public static boolean isValidImage(Part filePart) {
        if (filePart == null || filePart.getSize() == 0) {
            return false;
        }

        String contentType = filePart.getContentType();
        if (contentType == null) {
            return false;
        }

        return ALLOWED_IMAGE_TYPES.contains(contentType.toLowerCase());
    }
}
