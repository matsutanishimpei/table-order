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
        User mockUser = new User();
        mockUser.setId(userId);

        when(userDAO.login(userId, password)).thenReturn(Optional.of(mockUser));

        // Act
        Optional<User> result = userService.login(userId, password);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(userId, result.get().getId());
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
        User user = new User();
        user.setId("newuser");
        when(userDAO.insert(user)).thenReturn(true);

        // Act
        boolean result = userService.register(user);

        // Assert
        assertTrue(result);
        verify(userDAO).insert(user);
    }

    @Test
    public void testUpdate_WithPassword() {
        // Arrange
        User user = new User();
        user.setId("user1");
        String newPassword = "newsecret";

        when(userDAO.update(user)).thenReturn(true);
        when(userDAO.updatePassword("user1", newPassword)).thenReturn(true);

        // Act
        boolean result = userService.update(user, newPassword);

        // Assert
        assertTrue(result);
        verify(userDAO).update(user);
        verify(userDAO).updatePassword("user1", newPassword);
    }
}
