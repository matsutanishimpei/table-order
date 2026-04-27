package service;

import java.util.List;
import java.util.Optional;
import model.Product;

/**
 * 商品情報のビジネスロジックを定義するインターフェースです。
 */
public interface ProductService {
    List<Product> findAll();
    List<Product> findByCategory(int categoryId);
    Optional<Product> findById(int productId);
    boolean insert(Product p);
    boolean update(Product p);
    boolean updateAvailability(int productId, boolean isAvailable);
}
