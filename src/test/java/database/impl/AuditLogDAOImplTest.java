package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import database.AuditLogDAO;
import database.BaseIntegrationTest;

/**
 * AuditLogDAOImpl の統合テストクラスです。
 */
class AuditLogDAOImplTest extends BaseIntegrationTest {

    private AuditLogDAO auditLogDAO;

    @BeforeEach
    void setUp() {
        auditLogDAO = new AuditLogDAOImpl();
    }

    @Test
    @DisplayName("監査ログが正常に記録されること")
    void testLog_Insert() {
        // 例外が発生しないことを確認
        assertDoesNotThrow(() -> {
            auditLogDAO.log("products", "1", "INSERT", null, "新商品テスト", "admin");
        });
    }

    @Test
    @DisplayName("変更前後の値を含む監査ログが記録されること")
    void testLog_Update() {
        assertDoesNotThrow(() -> {
            auditLogDAO.log("products", "1", "UPDATE", "旧商品名", "新商品名", "admin");
        });
    }

    @Test
    @DisplayName("論理削除の監査ログが記録されること")
    void testLog_SoftDelete() {
        assertDoesNotThrow(() -> {
            auditLogDAO.log("categories", "5", "SOFT_DELETE", "カテゴリ名", null, "admin");
        });
    }
}
