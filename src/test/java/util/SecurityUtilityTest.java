package util;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;

/**
 * セキュリティ機能（XSS対策, CSRF対策）のユーティリティテストです。
 */
class SecurityUtilityTest {

    @Test
    @DisplayName("HTMLサニタイズ（XSS対策）が正しく機能すること")
    void testSanitize() {
        String input = "<script>alert('xss');</script>";
        String expected = "&lt;script&gt;alert(&#39;xss&#39;);&lt;/script&gt;";
        assertEquals(expected, ValidationUtil.sanitize(input), "HTML特殊文字が正しくエスケープされていません");

        assertEquals("&amp;lt;", ValidationUtil.sanitize("&lt;"), "アンパサンドが二重にエスケープ（またはそのまま）されていないか確認");
        assertNull(ValidationUtil.sanitize(null), "null入力時はnullを返すこと");
        assertEquals("", ValidationUtil.sanitize(""), "空文字入力時は空文字を返すこと");
    }

    @Test
    @DisplayName("CSRFトークンの生成と検証が正しく機能すること")
    void testCsrfToken() {
        String token1 = CsrfUtil.generateToken();
        String token2 = CsrfUtil.generateToken();

        assertNotNull(token1);
        assertNotNull(token2);
        assertNotEquals(token1, token2, "生成されるトークンが一意であることを確認");

        assertTrue(CsrfUtil.validateToken(token1, token1), "同じトークンなら検証成功すること");
        assertFalse(CsrfUtil.validateToken(token1, token2), "異なるトークンなら検証失敗すること");
        assertFalse(CsrfUtil.validateToken(null, token1), "nullリクエストトークンなら検証失敗すること");
        assertFalse(CsrfUtil.validateToken(token1, null), "nullセッショントークンなら検証失敗すること");
    }
}
