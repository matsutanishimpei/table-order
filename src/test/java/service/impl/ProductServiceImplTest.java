package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import service.AuditLogService;
import database.ProductDAO;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class ProductServiceImplTest {

    private ProductServiceImpl productService;

    @Mock
    private ProductDAO productDAO;

    @Mock
    private database.CategoryDAO categoryDAO;

    @Mock
    private AuditLogService auditLogService;

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productDAO, categoryDAO, auditLogService);
    }

    @Test
    public void testFindAll() {
        Product p1 = new Product(1, 1, "Product 1", 100, null, null, null, true, false);
        Product p2 = new Product(2, 1, "Product 2", 200, null, null, null, true, false);
        when(productDAO.findAll()).thenReturn(Arrays.asList(p1, p2));

        List<Product> result = productService.findAll();

        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).name());
        verify(productDAO).findAll();
    }

    @Test
    public void testFindByCategory() {
        int categoryId = 101;
        Product p = new Product(1, categoryId, "Product", 100, null, null, null, true, false);
        when(productDAO.findByCategory(categoryId)).thenReturn(Arrays.asList(p));

        List<Product> result = productService.findByCategory(categoryId);

        assertFalse(result.isEmpty());
        assertEquals(categoryId, result.get(0).categoryId());
        verify(productDAO).findByCategory(categoryId);
    }

    @Test
    public void testFindById_Existing() {
        int productId = 1;
        Product p = new Product(productId, 1, "Product", 100, null, null, null, true, false);
        when(productDAO.findById(productId)).thenReturn(Optional.of(p));

        Optional<Product> result = productService.findById(productId);

        assertTrue(result.isPresent());
        assertEquals(productId, result.get().id());
        verify(productDAO).findById(productId);
    }

    @Test
    public void testUpdateAvailability() {
        int productId = 1;
        boolean isAvailable = false;
        when(productDAO.updateAvailability(productId, isAvailable, "test-user")).thenReturn(true);

        boolean result = productService.updateAvailability(productId, isAvailable, "test-user");

        assertTrue(result);
        verify(productDAO).updateAvailability(productId, isAvailable, "test-user");
        verify(auditLogService).log(eq("products"), eq("1"), eq("UPDATE_AVAILABILITY"),
                isNull(), eq("false"), eq("test-user"));
    }

    @Test
    public void testInsert_Success() {
        Product p = new Product(0, 1, "New Product", 1000, "Desc", null, null, true, false);
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food", false)));
        when(productDAO.insert(p, "test-user")).thenReturn(true);

        boolean result = productService.insert(p, "test-user");

        assertTrue(result);
        verify(categoryDAO).findAll();
        verify(productDAO).insert(p, "test-user");
        verify(auditLogService).log(eq("products"), anyString(), eq("INSERT"),
                isNull(), eq("New Product"), eq("test-user"));
    }

    @Test
    public void testInsert_Failure_InvalidCategory() {
        Product p = new Product(0, 999, "Bad Product", 1000, "Desc", null, null, true, false);
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food", false)));

        exception.BusinessException ex = assertThrows(exception.BusinessException.class, () -> {
            productService.insert(p, "test-user");
        });

        assertTrue(ex.getMessage().contains("存在しません"));
        verify(productDAO, never()).insert(any(), anyString());
    }

    @Test
    public void testInsert_Failure_EmptyName() {
        Product p = new Product(0, 1, "", 1000, "Desc", null, null, true, false);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p, "test-user"));
    }

    @Test
    public void testInsert_Failure_LongName() {
        String longName = "a".repeat(util.AppConstants.MAX_PRODUCT_NAME_LENGTH + 1);
        Product p = new Product(0, 1, longName, 1000, "Desc", null, null, true, false);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p, "test-user"));
    }

    @Test
    public void testInsert_Failure_InvalidCategoryId() {
        Product p = new Product(0, -1, "Product", 1000, "Desc", null, null, true, false);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p, "test-user"));
    }

    @Test
    public void testInsert_Failure_InvalidPrice() {
        Product p = new Product(0, 1, "Product", 0, "Desc", null, null, true, false);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p, "test-user"));
    }

    @Test
    public void testUpdate_Success() {
        Product p = new Product(1, 1, "Updated", 1000, "Desc", null, null, true, false);
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food", false)));
        when(productDAO.findById(1)).thenReturn(Optional.of(
                new Product(1, 1, "Old", 800, null, null, null, true, false)));
        when(productDAO.update(p, "test-user")).thenReturn(true);

        assertTrue(productService.update(p, "test-user"));
        verify(productDAO).update(p, "test-user");
        verify(auditLogService).log(eq("products"), eq("1"), eq("UPDATE"),
                eq("Old (price=800)"), eq("Updated (price=1000)"), eq("test-user"));
    }

    @Test
    @DisplayName("論理削除: 正常に削除でき監査ログが記録されること")
    public void testDelete_Success() {
        when(productDAO.findById(1)).thenReturn(Optional.of(
                new Product(1, 1, "Target", 500, null, null, null, true, false)));
        when(productDAO.softDelete(1, "admin")).thenReturn(true);

        boolean result = productService.delete(1, "admin");

        assertTrue(result);
        verify(productDAO).softDelete(1, "admin");
        verify(auditLogService).log(eq("products"), eq("1"), eq("SOFT_DELETE"),
                eq("Target (price=500)"), isNull(), eq("admin"));
    }

    @Test
    @DisplayName("論理削除: 存在しない商品の場合は false を返すこと")
    public void testDelete_NotFound() {
        when(productDAO.findById(999)).thenReturn(Optional.empty());
        when(productDAO.softDelete(999, "admin")).thenReturn(false);

        assertFalse(productService.delete(999, "admin"));
        verify(auditLogService, never()).log(anyString(), anyString(), eq("SOFT_DELETE"),
                anyString(), any(), anyString());
    }
}
