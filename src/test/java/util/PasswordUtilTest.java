package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PasswordUtilTest {

    @Test
    @DisplayName("BCrypt でハッシュ化されたパスワードが正しく検証できること")
    void testBcryptHashAndCheck() {
        String password = "mySecurePassword123";
        String pepper = "appPepperSecret";

        String hashed = PasswordUtil.hashBcrypt(password, pepper);
        assertNotNull(hashed);
        assertTrue(hashed.startsWith("$2a$")); // BCryptのプレフィックス

        // 正しいパスワードとペッパーでの検証
        assertTrue(PasswordUtil.checkBcrypt(password, pepper, hashed), "正規の組み合わせで一致するべき");

        // 誤ったパスワード
        assertFalse(PasswordUtil.checkBcrypt("wrongPassword", pepper, hashed), "パスワード違いは失敗するべき");

        // 誤ったペッパー
        assertFalse(PasswordUtil.checkBcrypt(password, "wrongPepper", hashed), "ペッパー違いは失敗するべき");
    }

    @Test
    @DisplayName("同じパスワードでもBCryptが内部で自動生成するソルトにより異なるハッシュになること")
    void testBcryptSaltUniqueness() {
        String password = "password";
        String pepper = "secret";

        String hash1 = PasswordUtil.hashBcrypt(password, pepper);
        String hash2 = PasswordUtil.hashBcrypt(password, pepper);

        assertNotEquals(hash1, hash2, "BCryptは自動ソルトにより毎回異なるハッシュになるべき");
        
        // しかし、どちらも検証は成功する
        assertTrue(PasswordUtil.checkBcrypt(password, pepper, hash1));
        assertTrue(PasswordUtil.checkBcrypt(password, pepper, hash2));
    }

    @Test
    @DisplayName("定数時間比較(isEqualConstantTime)が正しく動作すること")
    void testIsEqualConstantTime() {
        String hash1 = "Aabbccddeeffgg";
        String hash2 = "Aabbccddeeffgg";
        String hash3 = "AabbccddeeffgG"; // 小文字大文字違い

        assertTrue(PasswordUtil.isEqualConstantTime(hash1, hash2));
        assertFalse(PasswordUtil.isEqualConstantTime(hash1, hash3));
        assertFalse(PasswordUtil.isEqualConstantTime(null, hash2));
    }

    @Test
    @DisplayName("nullのペッパーが渡されても実行時例外が発生しないこと")
    void testNullPepperHandling() {
        assertDoesNotThrow(() -> {
            String hash = PasswordUtil.hashBcrypt("password", null);
            assertTrue(PasswordUtil.checkBcrypt("password", null, hash));
        });
    }
}
