package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import database.ProductDAO;
import model.Product;
import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productDAO, categoryDAO);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Product p1 = new Product(1, 1, "Product 1", 100, null, null, null, true);
        Product p2 = new Product(2, 1, "Product 2", 200, null, null, null, true);
        when(productDAO.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).name());
        verify(productDAO).findAll();
    }

    @Test
    public void testFindByCategory() {
        // Arrange
        int categoryId = 101;
        Product p = new Product(1, categoryId, "Product", 100, null, null, null, true);
        when(productDAO.findByCategory(categoryId)).thenReturn(Arrays.asList(p));

        // Act
        List<Product> result = productService.findByCategory(categoryId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(categoryId, result.get(0).categoryId());
        verify(productDAO).findByCategory(categoryId);
    }

    @Test
    public void testFindById_Existing() {
        // Arrange
        int productId = 1;
        Product p = new Product(productId, 1, "Product", 100, null, null, null, true);
        when(productDAO.findById(productId)).thenReturn(Optional.of(p));

        // Act
        Optional<Product> result = productService.findById(productId);

        // Assert
        assertTrue(result.isPresent());
        assertEquals(productId, result.get().id());
        verify(productDAO).findById(productId);
    }

    @Test
    public void testUpdateAvailability() {
        // Arrange
        int productId = 1;
        boolean isAvailable = false;
        when(productDAO.updateAvailability(productId, isAvailable)).thenReturn(true);

        // Act
        boolean result = productService.updateAvailability(productId, isAvailable);

        // Assert
        assertTrue(result);
        verify(productDAO).updateAvailability(productId, isAvailable);
    }

    @Test
    public void testInsert_Success() {
        // Arrange
        Product p = new Product(0, 1, "New Product", 1000, "Desc", null, null, true);
        // カテゴリ1が存在する状態
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food")));
        when(productDAO.insert(p)).thenReturn(true);

        // Act
        boolean result = productService.insert(p);

        // Assert
        assertTrue(result);
        verify(categoryDAO).findAll();
        verify(productDAO).insert(p);
    }

    @Test
    public void testInsert_Failure_InvalidCategory() {
        // Arrange
        Product p = new Product(0, 999, "Bad Product", 1000, "Desc", null, null, true);
        // カテゴリ999は存在しない（空リストを返す）
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food")));

        // Act & Assert
        exception.BusinessException ex = assertThrows(exception.BusinessException.class, () -> {
            productService.insert(p);
        });
        
        assertTrue(ex.getMessage().contains("存在しません"));
        verify(productDAO, never()).insert(any());
    }

    @Test
    public void testInsert_Failure_EmptyName() {
        Product p = new Product(0, 1, "", 1000, "Desc", null, null, true);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p));
    }

    @Test
    public void testInsert_Failure_LongName() {
        String longName = "a".repeat(util.AppConstants.MAX_PRODUCT_NAME_LENGTH + 1);
        Product p = new Product(0, 1, longName, 1000, "Desc", null, null, true);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p));
    }

    @Test
    public void testInsert_Failure_InvalidCategoryId() {
        Product p = new Product(0, -1, "Product", 1000, "Desc", null, null, true);
        assertThrows(exception.BusinessException.class, () -> productService.insert(p));
    }

    @Test
    public void testInsert_Failure_InvalidPrice() {
        Product p = new Product(0, 1, "Product", 0, "Desc", null, null, true);
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food")));
        assertThrows(exception.BusinessException.class, () -> productService.insert(p));
    }

    @Test
    public void testUpdate_Success() {
        Product p = new Product(1, 1, "Updated", 1000, "Desc", null, null, true);
        when(categoryDAO.findAll()).thenReturn(Arrays.asList(new model.Category(1, "Food")));
        when(productDAO.update(p)).thenReturn(true);

        assertTrue(productService.update(p));
        verify(productDAO).update(p);
    }
}
