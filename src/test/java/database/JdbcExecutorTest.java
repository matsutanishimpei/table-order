package database;

import org.junit.jupiter.api.Test;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import exception.DatabaseException;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class JdbcExecutorTest {

    @Test
    void testQuery_Success() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        
        RowMapper<String> mapper = r -> "test";
        List<String> result = JdbcExecutor.query(con, "SELECT * FROM test", mapper);
        
        assertEquals(1, result.size());
        assertEquals("test", result.get(0));
    }

    @Test
    void testQuery_ThrowsSQLException() throws SQLException {
        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Query failed"));
        
        assertThrows(DatabaseException.class, () -> {
            JdbcExecutor.query(con, "SELECT * FROM test", rs -> "test");
        });
    }

    @Test
    void testQueryOne_Found() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(true, false);
        
        RowMapper<String> mapper = r -> "test";
        Optional<String> result = JdbcExecutor.queryOne(con, "SELECT * FROM test", mapper);
        
        assertTrue(result.isPresent());
        assertEquals("test", result.get());
    }

    @Test
    void testQueryOne_NotFound() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeQuery()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        
        RowMapper<String> mapper = r -> "test";
        Optional<String> result = JdbcExecutor.queryOne(con, "SELECT * FROM test", mapper);
        
        assertFalse(result.isPresent());
    }

    @Test
    void testUpdate_Success() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeUpdate()).thenReturn(1);
        
        int rows = JdbcExecutor.update(con, "UPDATE test SET col = 1", "param");
        assertEquals(1, rows);
        verify(ps).setObject(1, "param");
    }

    @Test
    void testUpdate_ThrowsSQLException() throws SQLException {
        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Update failed"));
        
        assertThrows(DatabaseException.class, () -> {
            JdbcExecutor.update(con, "UPDATE test SET col = 1");
        });
    }

    @Test
    void testInsertAndReturnId_Success() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        when(con.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(true);
        when(rs.getInt(1)).thenReturn(42);
        
        int id = JdbcExecutor.insertAndReturnId(con, "INSERT INTO test VALUES(1)");
        assertEquals(42, id);
    }

    @Test
    void testInsertAndReturnId_NoKeyGenerated() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        ResultSet rs = mock(ResultSet.class);
        
        when(con.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenReturn(ps);
        when(ps.getGeneratedKeys()).thenReturn(rs);
        when(rs.next()).thenReturn(false);
        
        int id = JdbcExecutor.insertAndReturnId(con, "INSERT INTO test VALUES(1)");
        assertEquals(-1, id);
    }

    @Test
    void testInsertAndReturnId_ThrowsSQLException() throws SQLException {
        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString(), eq(Statement.RETURN_GENERATED_KEYS))).thenThrow(new SQLException("Insert failed"));
        
        assertThrows(DatabaseException.class, () -> {
            JdbcExecutor.insertAndReturnId(con, "INSERT INTO test VALUES(1)");
        });
    }

    @Test
    void testBatchUpdate_Success() throws SQLException {
        Connection con = mock(Connection.class);
        PreparedStatement ps = mock(PreparedStatement.class);
        
        when(con.prepareStatement(anyString())).thenReturn(ps);
        when(ps.executeBatch()).thenReturn(new int[]{1, 1});
        
        List<Object[]> params = Arrays.asList(new Object[]{"param1"}, new Object[]{"param2"});
        int[] rows = JdbcExecutor.batchUpdate(con, "UPDATE test SET col = ?", params);
        
        assertEquals(2, rows.length);
        assertEquals(1, rows[0]);
        verify(ps, times(2)).addBatch();
    }

    @Test
    void testBatchUpdate_ThrowsSQLException() throws SQLException {
        Connection con = mock(Connection.class);
        when(con.prepareStatement(anyString())).thenThrow(new SQLException("Batch update failed"));
        
        List<Object[]> params = Arrays.asList(new Object[]{"param1"});
        assertThrows(DatabaseException.class, () -> {
            JdbcExecutor.batchUpdate(con, "UPDATE test SET col = ?", params);
        });
    }

    // Auto-connection variants
    @Test
    void testQueryWithAutoConnection() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            Connection con = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);
            
            mockedDb.when(DBManager::getConnection).thenReturn(con);
            when(con.prepareStatement(anyString())).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true, false);
            
            RowMapper<String> mapper = r -> "test";
            List<String> result = JdbcExecutor.query("SELECT * FROM test", mapper);
            
            assertEquals(1, result.size());
        }
    }

    @Test
    void testQueryWithAutoConnection_ThrowsException() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            mockedDb.when(DBManager::getConnection).thenThrow(new SQLException("Connection error"));
            
            assertThrows(DatabaseException.class, () -> {
                JdbcExecutor.query("SELECT * FROM test", rs -> "test");
            });
        }
    }

    @Test
    void testQueryOneWithAutoConnection() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            Connection con = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            ResultSet rs = mock(ResultSet.class);
            
            mockedDb.when(DBManager::getConnection).thenReturn(con);
            when(con.prepareStatement(anyString())).thenReturn(ps);
            when(ps.executeQuery()).thenReturn(rs);
            when(rs.next()).thenReturn(true, false);
            
            RowMapper<String> mapper = r -> "test";
            Optional<String> result = JdbcExecutor.queryOne("SELECT * FROM test", mapper);
            
            assertTrue(result.isPresent());
        }
    }

    @Test
    void testQueryOneWithAutoConnection_ThrowsException() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            mockedDb.when(DBManager::getConnection).thenThrow(new SQLException("Connection error"));
            
            assertThrows(DatabaseException.class, () -> {
                JdbcExecutor.queryOne("SELECT * FROM test", rs -> "test");
            });
        }
    }

    @Test
    void testUpdateWithAutoConnection() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            Connection con = mock(Connection.class);
            PreparedStatement ps = mock(PreparedStatement.class);
            
            mockedDb.when(DBManager::getConnection).thenReturn(con);
            when(con.prepareStatement(anyString())).thenReturn(ps);
            when(ps.executeUpdate()).thenReturn(1);
            
            int rows = JdbcExecutor.update("UPDATE test SET col = 1");
            assertEquals(1, rows);
        }
    }

    @Test
    void testUpdateWithAutoConnection_ThrowsException() throws SQLException {
        try (var mockedDb = mockStatic(DBManager.class)) {
            mockedDb.when(DBManager::getConnection).thenThrow(new SQLException("Connection error"));
            
            assertThrows(DatabaseException.class, () -> {
                JdbcExecutor.update("UPDATE test SET col = 1");
            });
        }
    }

    @Test
    void testPrivateConstructor() throws Exception {
        java.lang.reflect.Constructor<JdbcExecutor> constructor = JdbcExecutor.class.getDeclaredConstructor();
        assertTrue(java.lang.reflect.Modifier.isPrivate(constructor.getModifiers()));
        constructor.setAccessible(true);
        constructor.newInstance();
    }
}
