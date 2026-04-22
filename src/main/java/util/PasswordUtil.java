package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;
import org.mindrot.jbcrypt.BCrypt;

/**
 * パスワードのハッシュ化および検証を行うユーティリティクラスです。
 * レガシーなSHA-256方式と、モダンなBCrypt方式の両方をサポートします。
 */
public class PasswordUtil {

    /**
     * BCrypt を使用してパスワードをハッシュ化します。
     * @param password 生のパスワード
     * @param pepper 共通ソルト
     * @return ハッシュ化された文字列
     */
    public static String hashBcrypt(String password, String pepper) {
        return BCrypt.hashpw(password + pepper, BCrypt.gensalt());
    }

    /**
     * BCrypt 形式のハッシュと生のパスワードが一致するか検証します。
     * @param password 生のパスワード
     * @param pepper 共通ソルト
     * @param hashedBCrypt ハッシュ化されたパスワード
     * @return 一致すれば true
     */
    public static boolean checkBcrypt(String password, String pepper, String hashedBCrypt) {
        try {
            return BCrypt.checkpw(password + pepper, hashedBCrypt);
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 【後方互換用】旧来の SHA-256 + Salt 方式でハッシュ化します。
     * @param password 生のパスワード
     * @param salt 個別ソルト
     * @param pepper 共通ソルト
     * @return ハッシュ化された文字列
     */
    public static String hashLegacy(String password, String salt, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update((password + salt + pepper).getBytes());
            byte[] cipherText = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : cipherText) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("SHA-256 algorithm not found", e);
        }
    }

    /**
     * 二つの文字列を定数時間で比較します（タイミング攻撃対策）。
     * @param a 文字列A
     * @param b 文字列B
     * @return 一致すれば true
     */
    public static boolean isEqualConstantTime(String a, String b) {
        if (a == null || b == null) return false;
        if (a.length() != b.length()) return false;
        int result = 0;
        for (int i = 0; i < a.length(); i++) {
            result |= a.charAt(i) ^ b.charAt(i);
        }
        return result == 0;
    }

    /**
     * ランダムなソルトを生成します。
     * @return ソルト文字列
     */
    public static String generateSalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
