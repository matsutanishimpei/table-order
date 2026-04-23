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
    @DisplayName("認証・操作系テスト (Integration)")
    class IntegrationTests {
        @Test
        @DisplayName("ユーザーの作成・ログイン・更新・削除のサイクルが正常に動作すること")
        void testUserFullLifecycle() {
            // IDは20文字以内 (VARCHAR(20)制限)
            String testId = "jt_" + (System.currentTimeMillis() % 1000000000L);
            String testPass = "test_password_123";
            
            User newUser = new User();
            newUser.setId(testId);
            newUser.setPassword(testPass);
            newUser.setRole(3); // Hall

            try {
                // 1. Create (Insert)
                assertTrue(userDAO.insert(newUser), "新規ユーザーの作成に成功すること");

                // 2. Login (Authentication)
                // insert直後はBCryptで保存されているため、ログインが成功するはず
                User loggedIn = userDAO.login(testId, testPass);
                assertNotNull(loggedIn, "作成したユーザーでログインできること");
                assertEquals(testId, loggedIn.getId());

                // 3. Update (Role変更)
                loggedIn.setRole(2); // Kitchen
                assertTrue(userDAO.update(loggedIn), "ユーザー情報の更新に成功すること");
                User updated = userDAO.findById(testId);
                assertEquals(2, updated.getRole(), "更新内容が反映されていること");

                // 4. Login Failure (Wrong password)
                assertNull(userDAO.login(testId, "wrong_pass"), "誤ったパスワードではログインできないこと");

            } finally {
                // 5. Cleanup (Delete)
                userDAO.delete(testId);
            }
        }
    }
}
