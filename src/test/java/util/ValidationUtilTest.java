package util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

@DisplayName("ValidationUtil のバリデーションテスト")
public class ValidationUtilTest {

    @ParameterizedTest(name = "入力: \"{0}\", デフォルト: {1} => 期待値: {2}")
    @CsvSource({
        "10, 0, 10",
        " -5 , 0, -5",
        "abc, 0, 0",
        ", -1, -1",
        "'', 100, 100"
    })
    @DisplayName("parseIntSafe: 文字列を整数に安全に変換できること")
    void testParseIntSafe(String input, int defaultValue, int expected) {
        assertEquals(expected, ValidationUtil.parseIntSafe(input, defaultValue));
    }

    @ParameterizedTest(name = "入力: \"{0}\"")
    @NullAndEmptySource
    @ValueSource(strings = {"   ", "\t", "\n"})
    @DisplayName("isNullOrEmpty: null、空文字、空白のみの文字列を正しく判定できること")
    void testIsNullOrEmpty_True(String input) {
        assertTrue(ValidationUtil.isNullOrEmpty(input));
    }

    @Test
    @DisplayName("isNullOrEmpty: 有効な文字列を正しく判定できること")
    void testIsNullOrEmpty_False() {
        assertFalse(ValidationUtil.isNullOrEmpty("valid"));
    }

    @ParameterizedTest(name = "Content-Type: {0}")
    @ValueSource(strings = {"image/jpeg", "image/png", "image/webp", "image/gif", "IMAGE/PNG"})
    @DisplayName("isValidImage: 許可された画像形式をパスすること")
    void testIsValidImage_Success(String contentType) {
        Part mockPart = mock(Part.class);
        when(mockPart.getSize()).thenReturn(100L);
        when(mockPart.getContentType()).thenReturn(contentType);
        
        assertTrue(ValidationUtil.isValidImage(mockPart));
    }

    @Test
    @DisplayName("isValidImage: サイズ0の場合はエラーになること")
    void testIsValidImage_ZeroSize() {
        Part mockPart = mock(Part.class);
        when(mockPart.getSize()).thenReturn(0L);
        assertFalse(ValidationUtil.isValidImage(mockPart));
    }

    @ParameterizedTest(name = "不正な形式: {0}")
    @ValueSource(strings = {"text/plain", "application/pdf", "image/bmp"})
    @DisplayName("isValidImage: 許可されていない形式はエラーになること")
    void testIsValidImage_InvalidFormat(String contentType) {
        Part mockPart = mock(Part.class);
        when(mockPart.getSize()).thenReturn(100L);
        when(mockPart.getContentType()).thenReturn(contentType);
        assertFalse(ValidationUtil.isValidImage(mockPart));
    }

    @Test
    @DisplayName("isValidImage: Partがnullの場合はエラーになること")
    void testIsValidImage_Null() {
        assertFalse(ValidationUtil.isValidImage(null));
    }
}
