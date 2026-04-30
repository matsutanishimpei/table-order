package database;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.SQLException;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import exception.DatabaseException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class TransactionManagerTest {

    @Test
    void testConstructorIsPrivate() throws NoSuchMethodException {
        Constructor<TransactionManager> constructor = TransactionManager.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        try {
            constructor.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            // Should not happen
        }
    }

    @Test
    void testExecute_Success() {
        String result = TransactionManager.execute(con -> {
            assertNotNull(con);
            assertFalse(con.getAutoCommit());
            return "ok";
        });
        assertEquals("ok", result);
    }

    @Test
    void testExecute_RollbackOnException() {
        assertThrows(DatabaseException.class, () -> {
            TransactionManager.execute(con -> {
                throw new SQLException("test sql error");
            });
        });
    }

    @Test
    void testExecute_RollbackOnRuntimeException() {
        assertThrows(DatabaseException.class, () -> {
            TransactionManager.execute(con -> {
                throw new RuntimeException("test runtime error");
            });
        });
    }

    @Test
    void testExecute_RethrowsDatabaseException() {
        assertThrows(DatabaseException.class, () -> {
            TransactionManager.execute(con -> {
                throw new DatabaseException("original database exception");
            });
        });
    }

    @Test
    void testExecute_RollbackFailure() throws SQLException {
        try (var mockedDb = org.mockito.Mockito.mockStatic(DBManager.class)) {
            Connection con = mock(Connection.class);
            mockedDb.when(DBManager::getConnection).thenReturn(con);
            doThrow(new SQLException("rollback failed")).when(con).rollback();

            assertThrows(DatabaseException.class, () -> {
                TransactionManager.execute(c -> {
                    throw new RuntimeException("trigger rollback");
                });
            });
            verify(con).rollback();
        }
    }

    @Test
    void testExecute_ConnectionFailure() throws SQLException {
        try (var mockedDb = org.mockito.Mockito.mockStatic(DBManager.class)) {
            mockedDb.when(DBManager::getConnection).thenThrow(new SQLException("connection failed"));

            assertThrows(DatabaseException.class, () -> {
                TransactionManager.execute(con -> "ok");
            });
        }
    }
}
