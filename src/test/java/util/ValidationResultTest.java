package util;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ValidationResultTest {

    @Test
    void testSuccess() {
        ValidationResult result = ValidationResult.success();
        assertTrue(result.valid());
        assertTrue(result.isValid());
        assertFalse(result.isInvalid());
        assertNull(result.message());
    }

    @Test
    void testFailure() {
        String msg = "error message";
        ValidationResult result = ValidationResult.failure(msg);
        assertFalse(result.valid());
        assertFalse(result.isValid());
        assertTrue(result.isInvalid());
        assertEquals(msg, result.message());
    }

    @Test
    void testRecordProperties() {
        ValidationResult result = new ValidationResult(true, "test");
        assertTrue(result.valid());
        assertEquals("test", result.message());
    }
}
