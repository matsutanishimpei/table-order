package util;

import java.security.SecureRandom;
import java.util.Base64;

/**
 * CSRF対策用のトークン生成・検証ユーティリティです。
 */
public class CsrfUtil {
    private static final SecureRandom secureRandom = new SecureRandom();
    private static final Base64.Encoder base64Encoder = Base64.getUrlEncoder();

    /**
     * 新しいCSRFトークンを生成します。
     */
    public static String generateToken() {
        byte[] randomBytes = new byte[32];
        secureRandom.nextBytes(randomBytes);
        return base64Encoder.encodeToString(randomBytes);
    }

    /**
     * トークンが一致するか検証します。
     */
    public static boolean validateToken(String requestToken, String sessionToken) {
        if (requestToken == null || sessionToken == null) {
            return false;
        }
        return requestToken.equals(sessionToken);
    }
}
