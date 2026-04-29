package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import database.UserDAO;
import model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {

    private UserServiceImpl userService;

    @Mock
    private UserDAO userDAO;

    @BeforeEach
    public void setUp() {
        userService = new UserServiceImpl(userDAO);
    }

    @Test
    public void testLogin_Success() {
        // Arrange
        String userId = "admin";
        String password = "password123";
        User mockUser = new User(userId, null, 1, null);

        when(userDAO.login(userId, password)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.login(userId, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().id());
        verify(userDAO).login(userId, password);
    }

    @Test
    public void testLogin_Failure() {
        // Arrange
        String userId = "wronguser";
        String password = "wrongpassword";

        when(userDAO.login(userId, password)).thenReturn(Optional.empty());

        // Act
        Optional<User> result = userService.login(userId, password);

        // Assert
        assertTrue(result.isEmpty());
        verify(userDAO).login(userId, password);
    }

    @Test
    public void testRegister_Success() {
        // Arrange
        User user = new User("newuser", "pass", 1, null);
        when(userDAO.findById("newuser")).thenReturn(Optional.empty()); // 重複なし
        when(userDAO.insert(user, "test-user")).thenReturn(true);
 
        // Act
        boolean result = userService.register(user, "test-user");
 
        // Assert
        assertTrue(result);
        verify(userDAO).findById("newuser");
        verify(userDAO).insert(user, "test-user");
    }

    @Test
    public void testRegister_Failure_DuplicateId() {
        // Arrange
        User user = new User("existinguser", "pass", 1, null);
        when(userDAO.findById("existinguser")).thenReturn(Optional.of(user)); // 重複あり

        // Act & Assert
        assertThrows(exception.BusinessException.class, () -> {
            userService.register(user, "test-user");
        }, "既に存在するIDの場合はBusinessExceptionがスローされるべき");
        
        verify(userDAO, never()).insert(any(), anyString());
    }

    @Test
    public void testUpdate_WithPassword() {
        // Arrange
        User user = new User("user1", null, 1, null);
        String newPassword = "newsecret";

        when(userDAO.update(user, "test-user")).thenReturn(true);
        when(userDAO.updatePassword("user1", newPassword, "test-user")).thenReturn(true);

        // Act
        boolean result = userService.update(user, newPassword, "test-user");

        // Assert
        assertTrue(result);
        verify(userDAO).update(user, "test-user");
        verify(userDAO).updatePassword("user1", newPassword, "test-user");
    }

    @Test
    public void testUpdate_NoPassword() {
        // Arrange
        User user = new User("user1", null, 1, null);
        when(userDAO.update(user, "test-user")).thenReturn(true);

        // Act
        boolean result = userService.update(user, null, "test-user");

        // Assert
        assertTrue(result);
        verify(userDAO).update(user, "test-user");
        verify(userDAO, never()).updatePassword(anyString(), anyString(), anyString());
    }

    @Test
    public void testUpdate_Failure() {
        // Arrange
        User user = new User("user1", null, 1, null);
        when(userDAO.update(user, "test-user")).thenThrow(new RuntimeException("DB Error"));

        // Act
        boolean result = userService.update(user, "pass", "test-user");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testRegister_Failure_Exception() {
        // Arrange
        User user = new User("newuser", "pass", 1, null);
        when(userDAO.findById(anyString())).thenThrow(new RuntimeException("DB Error"));

        // Act
        boolean result = userService.register(user, "test-user");

        // Assert
        assertFalse(result);
    }

    @Test
    public void testDelete_Success() {
        // Arrange
        String id = "user1";
        when(userDAO.delete(id)).thenReturn(true);

        // Act
        boolean result = userService.delete(id);

        // Assert
        assertTrue(result);
        verify(userDAO).delete(id);
    }

    @Test
    public void testDelete_Failure_Exception() {
        // Arrange
        String id = "user1";
        when(userDAO.delete(id)).thenThrow(new RuntimeException("DB Error"));

        // Act
        boolean result = userService.delete(id);

        // Assert
        assertFalse(result);
    }

    @Test
    public void testFindAll() {
        userService.findAll();
        verify(userDAO).findAll();
    }

    @Test
    public void testFindById() {
        userService.findById("user1");
        verify(userDAO).findById("user1");
    }
}
