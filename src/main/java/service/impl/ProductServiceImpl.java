package service.impl;

import java.util.List;
import java.util.Optional;
import database.ProductDAO;
import database.impl.ProductDAOImpl;
import model.Product;
import service.ProductService;
import exception.BusinessException;
import util.AppConstants;
import util.ValidationUtil;
import util.ValidationResult;

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
    public Optional<Product> findById(int productId) {
        return productDAO.findById(productId);
    }

    @Override
    public boolean insert(Product p) {
        validate(p);
        return productDAO.insert(p);
    }

    @Override
    public boolean update(Product p) {
        validate(p);
        return productDAO.update(p);
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable) {
        return productDAO.updateAvailability(productId, isAvailable);
    }

    /**
     * 商品情報の業務バリデーションを行います。
     * @param p 商品情報
     * @throws BusinessException 業務ルール違反がある場合
     */
    private void validate(Product p) {
        ValidationResult res = ValidationUtil.validateRequired(p.name(), "商品名");
        if (res.isInvalid()) throw new BusinessException(res.message());

        res = ValidationUtil.validateMaxLength(p.name(), AppConstants.MAX_PRODUCT_NAME_LENGTH, "商品名");
        if (res.isInvalid()) throw new BusinessException(res.message());

        res = ValidationUtil.validatePositive(p.categoryId(), "カテゴリ");
        if (res.isInvalid()) throw new BusinessException(res.message());

        res = ValidationUtil.validatePositive(p.price(), "価格");
        if (res.isInvalid()) throw new BusinessException(res.message());
    }
}
