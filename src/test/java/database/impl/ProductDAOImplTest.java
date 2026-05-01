package database.impl;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import database.BaseIntegrationTest;
import database.ProductDAO;
import model.Product;

/**
 * ProductDAOImpl の統合テストクラスです。
 */
public class ProductDAOImplTest extends BaseIntegrationTest {

    private ProductDAO productDAO;

    @BeforeEach
    void setUp() {
        productDAO = new ProductDAOImpl();
    }

    @Test
    void testFindAll() {
        List<Product> products = productDAO.findAll();
        assertNotNull(products);
        assertFalse(products.isEmpty());
    }

    @Test
    void testFindByCategory() {
        // ID=1 のカテゴリ（フード等）に紐づく商品がある想定
        List<Product> products = productDAO.findByCategory(1);
        assertNotNull(products);
        // seed.sql で ID=1 のカテゴリに商品が登録されていることを期待
    }

    @Test
    void testFindById() {
        Optional<Product> p = productDAO.findById(1);
        assertTrue(p.isPresent());
        assertEquals(1, p.get().id());
    }

    @Test
    void testInsertUpdateDelete() {
        Product p = new Product(0, 1, "Test Product", 1000, "Desc", "None", null, true, false);

        // Insert
        assertTrue(productDAO.insert(p, "admin"));
        
        // 最新のものを取得（findAll の最後にあるはず）
        List<Product> list = productDAO.findAll();
        Product inserted = list.get(list.size() - 1);
        assertEquals("Test Product", inserted.name());

        // Update
        Product updateP = new Product(inserted.id(), 1, "Updated Product", 1200, "New Desc", "Peanut", "img.png", false, false);
        assertTrue(productDAO.update(updateP, "admin"));
        
        Product updated = productDAO.findById(inserted.id()).get();
        assertEquals("Updated Product", updated.name());
        assertEquals(1200, updated.price());
        assertFalse(updated.isAvailable());

        // Update Availability
        assertTrue(productDAO.updateAvailability(inserted.id(), true, "admin"));
        assertTrue(productDAO.findById(inserted.id()).get().isAvailable());
    }

    @Test
    void testSoftDelete_Visibility() {
        Product p = new Product(0, 1, "Deleted Product", 1000, "Desc", "None", null, true, false);
        assertTrue(productDAO.insert(p, "admin"));

        // 最新のものを取得
        List<Product> list = productDAO.findAll();
        Product inserted = list.stream()
                .filter(item -> "Deleted Product".equals(item.name()))
                .findFirst().get();

        // 論理削除
        assertTrue(productDAO.softDelete(inserted.id(), "admin"));

        // findAll から除外されていること
        List<Product> allProducts = productDAO.findAll();
        assertFalse(allProducts.stream().anyMatch(item -> item.id() == inserted.id()));

        // findByCategory からも除外されていること
        List<Product> categoryProducts = productDAO.findByCategory(1);
        assertFalse(categoryProducts.stream().anyMatch(item -> item.id() == inserted.id()));
    }
}
