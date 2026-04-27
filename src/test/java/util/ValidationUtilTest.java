package util;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import jakarta.servlet.http.Part;
import org.junit.jupiter.api.Test;

public class ValidationUtilTest {

    @Test
    public void testParseIntSafe_Valid() {
        assertEquals(10, ValidationUtil.parseIntSafe("10", 0));
        assertEquals(-5, ValidationUtil.parseIntSafe(" -5 ", 0));
    }

    @Test
    public void testParseIntSafe_Invalid() {
        assertEquals(0, ValidationUtil.parseIntSafe("abc", 0));
        assertEquals(-1, ValidationUtil.parseIntSafe(null, -1));
        assertEquals(100, ValidationUtil.parseIntSafe("", 100));
    }

    @Test
    public void testIsNullOrEmpty() {
        assertTrue(ValidationUtil.isNullOrEmpty(null));
        assertTrue(ValidationUtil.isNullOrEmpty(""));
        assertTrue(ValidationUtil.isNullOrEmpty("   "));
        assertFalse(ValidationUtil.isNullOrEmpty("a"));
    }

    @Test
    public void testIsValidImage_Success() {
        Part mockPart = mock(Part.class);
        when(mockPart.getSize()).thenReturn(100L);
        when(mockPart.getContentType()).thenReturn("image/jpeg");
        
        assertTrue(ValidationUtil.isValidImage(mockPart));
    }

    @Test
    public void testIsValidImage_Failure() {
        Part mockPart = mock(Part.class);
        
        // サイズ0
        when(mockPart.getSize()).thenReturn(0L);
        assertFalse(ValidationUtil.isValidImage(mockPart));
        
        // 許可されていない形式
        when(mockPart.getSize()).thenReturn(100L);
        when(mockPart.getContentType()).thenReturn("text/plain");
        assertFalse(ValidationUtil.isValidImage(mockPart));
        
        // null
        assertFalse(ValidationUtil.isValidImage(null));
    }
}
