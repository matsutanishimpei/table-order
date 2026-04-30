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

    @Test
    void testInitForTest_CloseExistingDataSource() throws Exception {
        // Save the original dataSource
        java.lang.reflect.Field dsField = DBManager.class.getDeclaredField("dataSource");
        dsField.setAccessible(true);
        com.zaxxer.hikari.HikariDataSource originalDs = (com.zaxxer.hikari.HikariDataSource) dsField.get(null);
        
        try {
            // Create a mock HikariDataSource that can be closed
            com.zaxxer.hikari.HikariDataSource dummyDs = org.mockito.Mockito.mock(com.zaxxer.hikari.HikariDataSource.class);
            org.mockito.Mockito.when(dummyDs.isClosed()).thenReturn(false);
            
            // Inject dummy
            dsField.set(null, dummyDs);
            
            // Call initForTest (this should close dummyDs and create a new broken one)
            try {
                DBManager.initForTest("jdbc:mysql://localhost:3306/dummy", "dummy", "dummy");
            } catch (Exception e) {
                // Ignore pool init exception
            }
            
            org.mockito.Mockito.verify(dummyDs).close();
        } finally {
            // Restore the original data source so other tests aren't broken!
            dsField.set(null, originalDs);
        }
    }
}
