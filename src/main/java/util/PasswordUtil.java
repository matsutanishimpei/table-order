package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

/**
 * パスワードのハッシュ化、ソルト生成、ストレッチングを行うユーティリティ。
 */
public class PasswordUtil {

    private static final String ALGORITHM = "SHA-256";
    private static final int STRETCH_COUNT = 10000; // ストレッチング回数

    /**
     * 新しいランダムなソルトを生成します。
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }

    /**
     * パスワード、ソルト、およびペッパーを組み合わせて、ストレッチングを行いハッシュ化します。
     * 計算式: Stretching(hash(password + salt + pepper))
     * 
     * @param password 平文パスワード
     * @param salt ユーザー固有のソルト（Base64形式）
     * @param pepper アプリケーション共通の秘密鍵（Pepper）
     * @return ハッシュ値（Base64形式）
     */
    public static String hashPassword(String password, String salt, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance(ALGORITHM);
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            
            // ソルトとペッパーを混ぜる
            md.update(saltBytes);
            if (pepper != null) {
                md.update(pepper.getBytes());
            }
            byte[] hashed = md.digest(password.getBytes());

            // ストレッチング
            for (int i = 0; i < STRETCH_COUNT; i++) {
                md.reset();
                hashed = md.digest(hashed);
            }

            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ハッシュアルゴリズムが見つかりません。", e);
        }
    }
}
