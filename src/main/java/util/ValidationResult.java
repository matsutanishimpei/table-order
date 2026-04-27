package util;

import lombok.AllArgsConstructor;
import lombok.Getter;

/**
 * バリデーション結果を保持するクラスです。
 */
@Getter
@AllArgsConstructor
public class ValidationResult {
    private final boolean valid;
    private final String message;

    /**
     * 成功の結果を生成します。
     */
    public static ValidationResult success() {
        return new ValidationResult(true, null);
    }

    /**
     * 失敗の結果を生成します。
     */
    public static ValidationResult failure(String message) {
        return new ValidationResult(false, message);
    }

    /**
     * 検証結果が失敗かどうかを返します。
     */
    public boolean isInvalid() {
        return !valid;
    }
    
    /**
     * 検証結果が成功かどうかを返します。
     */
    public boolean isValid() {
        return valid;
    }
}
