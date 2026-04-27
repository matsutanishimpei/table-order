package service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Arrays;
import java.util.List;

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

    @BeforeEach
    public void setUp() {
        productService = new ProductServiceImpl(productDAO);
    }

    @Test
    public void testFindAll() {
        // Arrange
        Product p1 = new Product();
        p1.setName("Product 1");
        Product p2 = new Product();
        p2.setName("Product 2");
        when(productDAO.findAll()).thenReturn(Arrays.asList(p1, p2));

        // Act
        List<Product> result = productService.findAll();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Product 1", result.get(0).getName());
        verify(productDAO).findAll();
    }

    @Test
    public void testFindByCategory() {
        // Arrange
        int categoryId = 101;
        Product p = new Product();
        p.setCategoryId(categoryId);
        when(productDAO.findByCategory(categoryId)).thenReturn(Arrays.asList(p));

        // Act
        List<Product> result = productService.findByCategory(categoryId);

        // Assert
        assertFalse(result.isEmpty());
        assertEquals(categoryId, result.get(0).getCategoryId());
        verify(productDAO).findByCategory(categoryId);
    }

    @Test
    public void testFindById_Existing() {
        // Arrange
        int productId = 1;
        Product p = new Product();
        p.setId(productId);
        when(productDAO.findById(productId)).thenReturn(p);

        // Act
        Product result = productService.findById(productId);

        // Assert
        assertNotNull(result);
        assertEquals(productId, result.getId());
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
}
