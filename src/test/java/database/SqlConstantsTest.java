package database;

import org.junit.jupiter.api.Test;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import static org.junit.jupiter.api.Assertions.*;

class SqlConstantsTest {

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<SqlConstants> constructor = SqlConstants.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Should not happen
        }
    }

    @Test
    void testConstantsAccess() {
        assertNotNull(SqlConstants.USER_SELECT_ALL);
        assertNotNull(SqlConstants.PRODUCT_SELECT_ALL);
    }
}
