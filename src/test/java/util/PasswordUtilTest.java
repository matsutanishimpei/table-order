package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

public class PasswordUtilTest {

    @Test
    @DisplayName("ソルトが正しく生成されること（Base64形式）")
    void testGenerateSalt() {
        String salt = PasswordUtil.generateSalt();
        assertNotNull(salt);
        assertFalse(salt.isEmpty());
        // Base64形式（16バイトの場合は22〜24文字程度）
        assertTrue(salt.length() >= 22);
    }

    @Test
    @DisplayName("同じパスワードでもソルトが異なればハッシュも異なること")
    void testHashVariation() {
        String password = "password123";
        String pepper = "pepper-secret";
        
        String salt1 = PasswordUtil.generateSalt();
        String salt2 = PasswordUtil.generateSalt();
        
        String hash1 = PasswordUtil.hashPassword(password, salt1, pepper);
        String hash2 = PasswordUtil.hashPassword(password, salt2, pepper);
        
        assertNotEquals(hash1, hash2, "ソルトが異なればハッシュも異なるべき");
    }

    @Test
    @DisplayName("パスワード、ソルト、ペッパーが一致すれば常に同じハッシュが生成されること")
    void testHashConsistency() {
        String password = "mypassword";
        String salt = "D2gNQacpqZV448SjgNuLWA==";
        String pepper = "secret-key";
        
        String hash1 = PasswordUtil.hashPassword(password, salt, pepper);
        String hash2 = PasswordUtil.hashPassword(password, salt, pepper);
        
        assertEquals(hash1, hash2, "同じ入力値からは同じハッシュが生成されるべき");
        assertEquals(44, hash1.length(), "SHA-256のBase64エンコード値は44文字であるべき");
    }

    @Test
    @DisplayName("ペッパーが1文字でも異なれば、生成されるハッシュが完全に異なること")
    void testPepperSensitivity() {
        String password = "pass";
        String salt = PasswordUtil.generateSalt();
        
        String hash1 = PasswordUtil.hashPassword(password, salt, "pepperA");
        String hash2 = PasswordUtil.hashPassword(password, salt, "pepperB");
        
        assertNotEquals(hash1, hash2, "ペッパーが異なればハッシュも完全に異なるべき");
    }

    @Test
    @DisplayName("空文字列や多言語文字（日本語・絵文字）が含まれていても正しくハッシュ化できること")
    void testSpecialCharacters() {
        String salt = PasswordUtil.generateSalt();
        String pepper = "pepper";
        
        // 漢字・絵文字を含むパスワード
        String pw1 = "パスワード🌟123";
        assertNotNull(PasswordUtil.hashPassword(pw1, salt, pepper));
        
        // 空文字
        String pw2 = "";
        String hashEmpty = PasswordUtil.hashPassword(pw2, salt, pepper);
        assertNotNull(hashEmpty);
        assertFalse(hashEmpty.isEmpty());
        
        // 非常に長いパスワード
        StringBuilder longPw = new StringBuilder();
        for(int i = 0; i < 1000; i++) longPw.append("a");
        assertNotNull(PasswordUtil.hashPassword(longPw.toString(), salt, pepper));
    }

    @Test
    @DisplayName("nullのペッパーが渡されても、実行時例外が発生せずに処理を継続できること")
    void testNullPepperHandling() {
        String salt = PasswordUtil.generateSalt();
        assertDoesNotThrow(() -> {
            PasswordUtil.hashPassword("password", salt, null);
        }, "ペッパーがnullでもNullPointerExceptionを投げてはならない");
    }
}
