package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import database.CategoryDAO;
import exception.BusinessException;
import model.Category;

@ExtendWith(MockitoExtension.class)
class CategoryServiceImplTest {

    @Mock
    private CategoryDAO categoryDAO;

    private CategoryServiceImpl categoryService;

    @BeforeEach
    void setUp() {
        categoryService = new CategoryServiceImpl(categoryDAO);
    }

    @Test
    @DisplayName("カテゴリ一覧取得: DAOの結果がそのまま返ること")
    void findAll_ReturnsList() {
        List<Category> expected = List.of(new Category(1, "Food"));
        when(categoryDAO.findAll()).thenReturn(expected);
        assertEquals(expected, categoryService.findAll());
    }

    @Test
    @DisplayName("カテゴリ登録: 正常な名前の場合は成功すること")
    void insert_Success() {
        when(categoryDAO.insert("Drink")).thenReturn(true);
        assertTrue(categoryService.insert(" Drink ")); // Trim check
        verify(categoryDAO).insert("Drink");
    }

    @Test
    @DisplayName("カテゴリ登録エラー: 名前が空の場合は BusinessException を投げること")
    void insert_EmptyName_ThrowsException() {
        assertThrows(BusinessException.class, () -> {
            categoryService.insert("");
        });
    }

    @Test
    @DisplayName("カテゴリ更新: 正常なデータの場合は成功すること")
    void update_Success() {
        Category c = new Category(1, "Dessert");
        when(categoryDAO.update(c)).thenReturn(true);
        assertTrue(categoryService.update(c));
    }
}
