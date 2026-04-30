package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import database.AuditLogDAO;
import database.CategoryDAO;
import exception.BusinessException;
import model.Category;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryDAO categoryDAO;

    @Mock
    private AuditLogDAO auditLogDAO;

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryDAO, auditLogDAO);
    }

    @Test
    @DisplayName("カテゴリ一覧取得: DAOの結果がそのまま返ること")
    void findAll_ReturnsList() {
        List<Category> expected = List.of(new Category(1, "Food", false));
        when(categoryDAO.findAll()).thenReturn(expected);
        assertEquals(expected, categoryService.findAll());
    }

    @Test
    @DisplayName("カテゴリ登録: 正常な名前の場合は成功し監査ログが記録されること")
    void insert_Success() {
        when(categoryDAO.insert("Drink", "test-user")).thenReturn(true);
        assertTrue(categoryService.insert(" Drink ", "test-user")); // Trim check
        verify(categoryDAO).insert("Drink", "test-user");
        verify(auditLogDAO).log(eq("categories"), eq("-"), eq("INSERT"),
                isNull(), eq(" Drink "), eq("test-user"));
    }

    @Test
    @DisplayName("カテゴリ登録エラー: 名前が空の場合は BusinessException を投げること")
    void insert_EmptyName_ThrowsException() {
        assertThrows(BusinessException.class, () -> {
            categoryService.insert("", "test-user");
        });
    }

    @Test
    @DisplayName("カテゴリ更新: 正常なデータの場合は成功し監査ログが記録されること")
    void update_Success() {
        Category c = new Category(1, "Dessert", false);
        when(categoryDAO.findById(1)).thenReturn(Optional.of(new Category(1, "Food", false)));
        when(categoryDAO.update(c, "test-user")).thenReturn(true);
        assertTrue(categoryService.update(c, "test-user"));
        verify(auditLogDAO).log(eq("categories"), eq("1"), eq("UPDATE"),
                eq("Food"), eq("Dessert"), eq("test-user"));
    }

    @Test
    @DisplayName("論理削除: 正常に削除でき監査ログが記録されること")
    void delete_Success() {
        when(categoryDAO.findById(1)).thenReturn(Optional.of(new Category(1, "Food", false)));
        when(categoryDAO.softDelete(1, "admin")).thenReturn(true);

        assertTrue(categoryService.delete(1, "admin"));
        verify(categoryDAO).softDelete(1, "admin");
        verify(auditLogDAO).log(eq("categories"), eq("1"), eq("SOFT_DELETE"),
                eq("Food"), isNull(), eq("admin"));
    }

    @Test
    @DisplayName("論理削除: 存在しない場合は false を返すこと")
    void delete_NotFound() {
        when(categoryDAO.findById(999)).thenReturn(Optional.empty());
        when(categoryDAO.softDelete(999, "admin")).thenReturn(false);

        assertFalse(categoryService.delete(999, "admin"));
        verify(auditLogDAO, never()).log(anyString(), anyString(), eq("SOFT_DELETE"),
                anyString(), any(), anyString());
    }
}
