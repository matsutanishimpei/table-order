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
    @DisplayName("パスワード、ソルト、ペッパーが一致すれば同じハッシュが生成されること")
    void testHashConsistency() {
        String password = "mypassword";
        String salt = "D2gNQacpqZV448SjgNuLWA==";
        String pepper = "secret-key";
        
        String hash1 = PasswordUtil.hashPassword(password, salt, pepper);
        String hash2 = PasswordUtil.hashPassword(password, salt, pepper);
        
        assertEquals(hash1, hash2, "同じ入力値からは同じハッシュが生成されるべき");
    }
}
