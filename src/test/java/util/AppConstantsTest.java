package util;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.*;

class AppConstantsTest {

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<AppConstants> constructor = AppConstants.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Should not happen if setAccessible is used, but we catch it just in case
        }
    }

    @Test
    void testConstantsAccess() {
        // Just access some constants to ensure they are loaded
        assertNotNull(AppConstants.VIEW_LOGIN);
        assertEquals("Login", AppConstants.REDIRECT_LOGIN);
    }
}
