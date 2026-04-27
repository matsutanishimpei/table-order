package util;

/**
 * バリデーション結果を保持するレコードです。
 * Java 17 の record を使用して、不変性と簡潔性を確保しています。
 * 
 * @param valid 検証結果が成功かどうか
 * @param message 失敗時のエラーメッセージ
 */
public record ValidationResult(boolean valid, String message) {

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
