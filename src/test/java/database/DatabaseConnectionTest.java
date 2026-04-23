package database;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import database.impl.UserDAOImpl;
import database.impl.ProductDAOImpl;
import database.impl.CategoryDAOImpl;
import model.User;
import model.Product;
import model.Category;
import java.util.List;

/**
 * データベース層の統合テストクラスです。
 * 実際のDB接続を使用し、DAOのロジックとデータの整合性を検証します。
 */
public class DatabaseConnectionTest {

    private final UserDAOImpl userDAO = new UserDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();
    private final CategoryDAOImpl categoryDAO = new CategoryDAOImpl();

    @Nested
    @DisplayName("参照系テスト")
    class ReadTests {
        @Test
        @DisplayName("全件取得がエラーなく実行でき、データがマッピングされていること")
        void testFindAll() {
            assertDoesNotThrow(() -> {
                List<Product> products = productDAO.findAll();
                assertNotNull(products);
                if (!products.isEmpty()) {
                    Product p = products.get(0);
                    assertNotNull(p.getName(), "商品名が取得できていること");
                    assertTrue(p.getPrice() >= 0, "価格が正数であること");
                }
            });
        }
    }

    @Nested
    @DisplayName("認証系テスト")
    class AuthTests {
        @Test
        @DisplayName("既存ユーザー(admin)でのログイン認証が正しく機能すること")
        void testAdminLogin() {
            // seed.sql で投入された admin / pass を使用
            User user = userDAO.login("admin", "pass");
            assertNotNull(user, "正しいIDとパスワードでログインできること");
            assertEquals("admin", user.getId());

            User failUser = userDAO.login("admin", "wrong_password");
            assertNull(failUser, "誤ったパスワードではログインできないこと");
        }
    }

    @Nested
    @DisplayName("データ操作系テスト (CRUD)")
    class CrudTests {
        @Test
        @DisplayName("ユーザーの作成・検索・更新・削除のサイクルが正常に動作すること")
        void testUserLifecycle() {
            String testId = "junit_test_" + System.currentTimeMillis();
            User newUser = new User();
            newUser.setId(testId);
            newUser.setPassword("temp_pass");
            newUser.setRole(3); // Hall

            // 1. Create (Insert)
            assertTrue(userDAO.insert(newUser), "新規ユーザーの作成に成功すること");

            // 2. Read (FindById)
            User found = userDAO.findById(testId);
            assertNotNull(found, "作成したユーザーが取得できること");
            assertEquals(3, found.getRole());

            // 3. Update (Role変更)
            found.setRole(2); // Kitchen
            assertTrue(userDAO.update(found), "ユーザー情報の更新に成功すること");
            User updated = userDAO.findById(testId);
            assertEquals(2, updated.getRole(), "更新内容が反映されていること");

            // 4. Delete
            assertTrue(userDAO.delete(testId), "ユーザーの削除に成功すること");
            assertNull(userDAO.findById(testId), "削除後はユーザーが取得できないこと");
        }
    }
}
