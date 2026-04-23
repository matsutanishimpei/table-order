package service.impl;

import java.util.List;

import database.ProductDAO;
import database.impl.ProductDAOImpl;
import model.Product;
import service.ProductService;

/**
 * 商品情報のビジネスロジックを管理するService実装クラスです。
 */
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;

    // プロダクション用コンストラクタ
    public ProductServiceImpl() {
        this(new ProductDAOImpl());
    }

    // テスト・DI用コンストラクタ
    public ProductServiceImpl(ProductDAO productDAO) {
        this.productDAO = productDAO;
    }

    @Override
    public List<Product> findAll() {
        return productDAO.findAll();
    }

    @Override
    public List<Product> findByCategory(int categoryId) {
        return productDAO.findByCategory(categoryId);
    }

    @Override
    public Product findById(int productId) {
        return productDAO.findById(productId);
    }

    @Override
    public boolean insert(Product p) {
        return productDAO.insert(p);
    }

    @Override
    public boolean update(Product p) {
        return productDAO.update(p);
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable) {
        return productDAO.updateAvailability(productId, isAvailable);
    }
}
