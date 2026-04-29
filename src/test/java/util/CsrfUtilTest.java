package util;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CsrfUtilTest {

    @Test
    @DisplayName("トークンが正しく生成され、毎回異なる値になること")
    void generateToken_CreatesUniqueTokens() {
        String token1 = CsrfUtil.generateToken();
        String token2 = CsrfUtil.generateToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2);
        assertTrue(token1.length() > 0);
    }

    @Test
    @DisplayName("有効なトークンのペアを正しく検証できること")
    void validateToken_ValidTokens() {
        String token = CsrfUtil.generateToken();
        assertTrue(CsrfUtil.validateToken(token, token));
    }

    @Test
    @DisplayName("不一致または無効なトークンの場合は検証に失敗すること")
    void validateToken_InvalidTokens() {
        assertFalse(CsrfUtil.validateToken("token1", "token2"));
        assertFalse(CsrfUtil.validateToken(null, "token"));
        assertFalse(CsrfUtil.validateToken("token", null));
        assertFalse(CsrfUtil.validateToken(null, null));
    }
}
