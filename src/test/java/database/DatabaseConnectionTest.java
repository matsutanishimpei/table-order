package database;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import database.impl.UserDAOImpl;
import database.impl.ProductDAOImpl;
import model.User;
import model.Product;
import java.util.List;
import java.util.Optional;

/**
 * データベース層の統合テストクラスです。
 * Testcontainersを利用し、本物のMySQLコンテナに対してDAOのロジックとデータの整合性を検証します。
 */
public class DatabaseConnectionTest extends BaseIntegrationTest {

    private final UserDAOImpl userDAO = new UserDAOImpl();
    private final ProductDAOImpl productDAO = new ProductDAOImpl();

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
                    assertNotNull(p.name(), "商品名が取得できていること");
                    assertTrue(p.price() >= 0, "価格が正数であること");
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
            
            User newUser = new User(testId, testPass, 3, null);

            try {
                // 1. Create (Insert)
                assertTrue(userDAO.insert(newUser, "test-system"), "新規ユーザーの作成に成功すること");

                // 2. Login (Authentication)
                // insert直後はBCryptで保存されているため、ログインが成功するはず
                Optional<User> loggedInOpt = userDAO.login(testId, testPass);
                assertTrue(loggedInOpt.isPresent(), "作成したユーザーでログインできること");
                User loggedIn = loggedInOpt.get();
                assertEquals(testId, loggedIn.id());

                // 3. Update (Role変更)
                // record は不変なため、新しいインスタンスを作成する
                User updatedUser = new User(loggedIn.id(), null, 2, loggedIn.tableId());
                assertTrue(userDAO.update(updatedUser, "test-system"), "ユーザー情報の更新に成功すること");
                Optional<User> updatedOpt = userDAO.findById(testId);
                assertTrue(updatedOpt.isPresent());
                assertEquals(2, updatedOpt.get().role(), "更新内容が反映されていること");

                // 4. Login Failure (Wrong password)
                assertTrue(userDAO.login(testId, "wrong_pass").isEmpty(), "誤ったパスワードではログインできないこと");

            } finally {
                // 5. Cleanup (Delete)
                userDAO.delete(testId);
            }
        }
    }
}
