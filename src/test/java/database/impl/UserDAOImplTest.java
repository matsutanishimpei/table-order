package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.BaseIntegrationTest;
import database.UserDAO;
import model.User;

/**
 * UserDAOImpl の統合テストクラスです。
 */
public class UserDAOImplTest extends BaseIntegrationTest {

    private UserDAO userDAO;

    @BeforeEach
    void setUp() {
        userDAO = new UserDAOImpl();
    }

    @Test
    void testFindAll() {
        List<User> users = userDAO.findAll();
        assertNotNull(users);
        assertFalse(users.isEmpty());
    }

    @Test
    void testFindById() {
        Optional<User> user = userDAO.findById("admin");
        assertTrue(user.isPresent());
        assertEquals("admin", user.get().id());
    }

    @Test
    void testLogin_Success_Bcrypt() {
        // 新しくBCryptユーザーを作成
        String userId = "test_bcrypt";
        User user = new User(userId, "password123", 1, null, false);
        userDAO.insert(user, "admin");

        // ログイン検証
        Optional<User> loggedInUser = userDAO.login(userId, "password123");
        assertTrue(loggedInUser.isPresent());
        assertEquals(userId, loggedInUser.get().id());
    }

    @Test
    void testLogin_Failure_WrongPassword() {
        Optional<User> loggedInUser = userDAO.login("admin", "wrongpassword");
        assertFalse(loggedInUser.isPresent());
    }

    @Test
    void testInsertUpdateDelete() {
        String userId = "newuser";
        User user = new User(userId, "pass", 1, null, false);

        // Insert
        assertTrue(userDAO.insert(user, "admin"));
        assertTrue(userDAO.findById(userId).isPresent());

        // Update
        User updatedUser = new User(userId, null, 2, 1, false);
        assertTrue(userDAO.update(updatedUser, "admin"));
        User result = userDAO.findById(userId).get();
        assertEquals(2, result.role());
        assertEquals(1, result.tableId());

        // Update Password
        assertTrue(userDAO.updatePassword(userId, "newpass", "admin"));
        assertTrue(userDAO.login(userId, "newpass").isPresent());

        // Delete
        assertTrue(userDAO.delete(userId));
        assertFalse(userDAO.findById(userId).isPresent());
    }
}
