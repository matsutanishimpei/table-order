package database;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DBManagerTest {

    @Test
    void testGetPepper() {
        String pepper = DBManager.getPepper();
        assertNotNull(pepper);
    }

    @Test
    void testGetConnection() throws SQLException {
        try (Connection con = DBManager.getConnection()) {
            assertNotNull(con);
            assertFalse(con.isClosed());
        }
    }

    @Test
    void testCloseConnection_Null() {
        // Should not throw exception
        DBManager.closeConnection(null);
    }

    @Test
    void testCloseConnection_Success() throws SQLException {
        Connection con = mock(Connection.class);
        DBManager.closeConnection(con);
        verify(con).close();
    }

    @Test
    void testCloseConnection_HandlesSQLException() throws SQLException {
        Connection con = mock(Connection.class);
        doThrow(new SQLException("test error")).when(con).close();
        
        // Should catch and log, not rethrow
        DBManager.closeConnection(con);
        verify(con).close();
    }

    @Test
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<DBManager> constructor = DBManager.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
