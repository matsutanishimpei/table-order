package util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF対策用のトークン生成・検証ユーティリティです。
 */
public class CsrfUtil {
    
    private static final SecureRandom secureRandom = new SecureRandom();

    /**
     * 新しいCSRFトークンを生成します。
     * @return Base64エンコードされたトークン文字列
     */
    public static String generateToken() {
        byte[] tokenBytes = new byte[32];
        secureRandom.nextBytes(tokenBytes);
        return Base64.getUrlEncoder().withoutPadding().encodeToString(tokenBytes);
    }

    /**
     * リクエストされたトークンとセッションのトークンが一致するか検証します。
     * @param requestToken リクエストパラメータのトークン
     * @param sessionToken セッションに保存されたトークン
     * @return 一致すれば true
     */
    public static boolean validateToken(String requestToken, String sessionToken) {
        if (requestToken == null || sessionToken == null) {
            return false;
        }
        // タイミング攻撃対策として定数時間比較を行うのがベストプラクティス
        return PasswordUtil.isEqualConstantTime(requestToken, sessionToken);
    }
}
