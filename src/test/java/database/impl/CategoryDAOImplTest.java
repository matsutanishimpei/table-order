package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import database.BaseIntegrationTest;
import model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class CategoryDAOImplTest extends BaseIntegrationTest {

    private CategoryDAOImpl dao;

    @BeforeEach
    void setUp() {
        dao = new CategoryDAOImpl();
    }

    @Test
    @DisplayName("全カテゴリが取得できること")
    void testFindAll() {
        List<Category> categories = dao.findAll();
        assertNotNull(categories);
        // seed.sql で投入されたデータ（ドリンク, フード, デザート）があるはず
        assertTrue(categories.size() >= 3);
    }

    @Test
    @DisplayName("新規カテゴリが登録・取得できること")
    void testInsertAndFindById() {
        String name = "期間限定メニュー";
        boolean success = dao.insert(name, "test-user");
        assertTrue(success);

        List<Category> categories = dao.findAll();
        Category inserted = categories.stream()
                .filter(c -> name.equals(c.name()))
                .findFirst()
                .orElseThrow();

        Optional<Category> found = dao.findById(inserted.id());
        assertTrue(found.isPresent());
        assertEquals(name, found.get().name());
    }

    @Test
    @DisplayName("カテゴリ情報が更新できること")
    void testUpdate() {
        // ID:1 は 'ドリンク' (seed.sql)
        Category category = new Category(1, "飲み物", false);
        boolean success = dao.update(category, "test-admin");
        assertTrue(success);

        Optional<Category> updated = dao.findById(1);
        assertTrue(updated.isPresent());
        assertEquals("飲み物", updated.get().name());
    }

    @Test
    @DisplayName("存在しないIDで検索した場合は空のOptionalが返ること")
    void testFindById_NotFound() {
        Optional<Category> found = dao.findById(999);
        assertFalse(found.isPresent());
    }

    @Test
    @DisplayName("論理削除されたカテゴリが一覧に含まれないこと")
    void testSoftDelete_Visibility() {
        String name = "削除用カテゴリ";
        dao.insert(name, "test-user");

        Category inserted = dao.findAll().stream()
                .filter(c -> name.equals(c.name()))
                .findFirst()
                .orElseThrow();

        // 論理削除
        assertTrue(dao.softDelete(inserted.id(), "test-user"));

        // findAll から除外されていること
        List<Category> allCategories = dao.findAll();
        assertFalse(allCategories.stream().anyMatch(c -> c.id() == inserted.id()));
    }
}
