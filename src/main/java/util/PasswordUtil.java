package util;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.util.Base64;

import org.mindrot.jbcrypt.BCrypt;

/**
 * パスワードのハッシュ化（BCrypt）、およびレガシー移行用のユーティリティ。
 */
public class PasswordUtil {

    private static final String LEGACY_ALGORITHM = "SHA-256";
    private static final int LEGACY_STRETCH_COUNT = 10000;

    /**
     * 新しいパスワードを BCrypt アルゴリズムでハッシュ化します。
     * 自動的に強力なソルトが生成・内包されます。
     * 
     * @param password 平文パスワード
     * @param pepper アプリケーション共通のPepper（BCrypt前の前処理として使用）
     * @return BCryptハッシュ文字列（例: $2a$12$...）
     */
    public static String hashBcrypt(String password, String pepper) {
        // Pepperが存在する場合は平文の末尾に付与してからBCrypt化する（より強固に）
        String saltedPassword = (pepper != null) ? password + pepper : password;
        // workload factor 12 は現在の標準的な計算量
        return BCrypt.hashpw(saltedPassword, BCrypt.gensalt(12));
    }

    /**
     * 入力されたパスワードが、保存されている BCrypt ハッシュと一致するか検証します。
     * 内部でタイミング攻撃を防ぐ定数時間比較が行われます。
     * 
     * @param password 入力された平文パスワード
     * @param pepper アプリケーション共通のPepper
     * @param hashed データベースに保存されているBCryptハッシュ
     * @return 一致すればtrue
     */
    public static boolean checkBcrypt(String password, String pepper, String hashed) {
        String saltedPassword = (pepper != null) ? password + pepper : password;
        try {
            return BCrypt.checkpw(saltedPassword, hashed);
        } catch (IllegalArgumentException e) {
            // "Invalid salt version" などの例外発生時は false
            return false;
        }
    }

    /**
     * 【レガシー互換】安全な定数時間比較を行います（タイミング攻撃対策）
     * 
     * @param hash1 比較対象1
     * @param hash2 比較対象2
     * @return 完全に一致すればtrue
     */
    public static boolean isEqualConstantTime(String hash1, String hash2) {
        if (hash1 == null || hash2 == null) {
            return false;
        }
        return MessageDigest.isEqual(hash1.getBytes(), hash2.getBytes());
    }

    /**
     * 【レガシー互換・移行用】
     * 旧システムで使用されていた SHA-256 + 10,000回ストレッチング を再現します。
     * 
     * @param password 平文パスワード
     * @param salt ユーザー固有のソルト（Base64形式）
     * @param pepper アプリケーション共通の秘密鍵（Pepper）
     * @return ハッシュ値（Base64形式）
     */
    public static String hashLegacy(String password, String salt, String pepper) {
        try {
            MessageDigest md = MessageDigest.getInstance(LEGACY_ALGORITHM);
            byte[] saltBytes = Base64.getDecoder().decode(salt);
            
            // ソルトとペッパーを混ぜる
            md.update(saltBytes);
            if (pepper != null) {
                md.update(pepper.getBytes());
            }
            byte[] hashed = md.digest(password.getBytes());

            // ストレッチング
            for (int i = 0; i < LEGACY_STRETCH_COUNT; i++) {
                md.reset();
                hashed = md.digest(hashed);
            }

            return Base64.getEncoder().encodeToString(hashed);
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("ハッシュアルゴリズムが見つかりません。", e);
        }
    }

    /**
     * 【レガシー互換・移行用】
     * 新しいランダムなソルトを生成します。（新規ユーザーはBCryptを使うため基本は不要）
     */
    public static String generateLegacySalt() {
        SecureRandom random = new SecureRandom();
        byte[] salt = new byte[16];
        random.nextBytes(salt);
        return Base64.getEncoder().encodeToString(salt);
    }
}
