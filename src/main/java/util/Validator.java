package util;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * 入力値のバリデーションを流れるようなインターフェース（Fluent API）で提供するユーティリティクラスです。
 */
public class Validator {

    private final List<String> errors = new ArrayList<>();

    private Validator() { }

    /**
     * バリデーションを開始します。
     * @return Validator インスタンス
     */
    public static Validator create() {
        return new Validator();
    }

    /**
     * 値が必須であることを検証します。
     * @param value 検証する値
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator required(String value, String message) {
        if (value == null || value.trim().isEmpty()) {
            errors.add(message);
        }
        return this;
    }

    /**
     * 最大文字数を検証します。
     * @param value 検証する値
     * @param max 最大文字数
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator maxLength(String value, int max, String message) {
        if (value != null && value.length() > max) {
            errors.add(message);
        }
        return this;
    }

    /**
     * 数値形式であることを検証します。
     * @param value 検証する値
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator isNumeric(String value, String message) {
        if (value != null && !value.isEmpty()) {
            try {
                Integer.parseInt(value);
            } catch (NumberFormatException e) {
                errors.add(message);
            }
        }
        return this;
    }

    /**
     * 数値の範囲を検証します。
     * @param value 検証する値（数値文字列）
     * @param min 最小値
     * @param max 最大値
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator range(String value, int min, int max, String message) {
        if (value != null && !value.isEmpty()) {
            try {
                int val = Integer.parseInt(value);
                if (val < min || val > max) {
                    errors.add(message);
                }
            } catch (NumberFormatException e) {
                // isNumeric で別途チェックすることを想定し、ここではスルー
            }
        }
        return this;
    }

    /**
     * 指定された値より大きいことを検証します。
     * @param value 検証する値
     * @param min 最小値（これより大きい必要がある）
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator greaterThan(int value, int min, String message) {
        if (value <= min) {
            errors.add(message);
        }
        return this;
    }

    /**
     * 正規表現パターンに一致するか検証します。
     * @param value 検証する値
     * @param regex 正規表現
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator match(String value, String regex, String message) {
        if (value != null && !value.isEmpty()) {
            if (!Pattern.matches(regex, value)) {
                errors.add(message);
            }
        }
        return this;
    }

    /**
     * アップロードされたファイルが画像形式であることを検証します。
     * @param part ファイルパート
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator isImage(jakarta.servlet.http.Part part, String message) {
        if (part != null && part.getSize() > 0) {
            String contentType = part.getContentType();
            if (contentType == null || !AppConstants.ALLOWED_IMAGE_TYPES.contains(
                    contentType.toLowerCase(java.util.Locale.ROOT))) {
                errors.add(message);
            }
        }
        return this;
    }

    /**
     * 画像が必須であることを検証します。
     * @param part ファイルパート
     * @param message エラーメッセージ
     * @return Validator インスタンス
     */
    public Validator isImageRequired(jakarta.servlet.http.Part part, String message) {
        if (part == null || part.getSize() == 0) {
            errors.add(message);
        }
        return this;
    }

    /**
     * バリデーションエラーがあるか確認します。
     * @return エラーがある場合 true
     */
    public boolean hasErrors() {
        return !errors.isEmpty();
    }

    /**
     * バリデーションエラーがある場合に BusinessException をスローします。
     * @throws exception.BusinessException エラーがある場合
     */
    public void throwOnErrors() {
        if (hasErrors()) {
            throw new exception.BusinessException(String.join(" ", errors));
        }
    }

    /**
     * 収集されたエラーメッセージのリストを返します。
     * @return エラーメッセージリスト
     */
    public List<String> getErrors() {
        return new ArrayList<>(errors);
    }
}
