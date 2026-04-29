package service.impl;

import java.util.List;
import java.util.Optional;
import database.ProductDAO;
import database.impl.ProductDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
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
    private final database.CategoryDAO categoryDAO;

    // プロダクション用コンストラクタ
    public ProductServiceImpl() {
        this(new ProductDAOImpl(), new database.impl.CategoryDAOImpl());
    }

    // テスト・DI用コンストラクタ
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ProductServiceImpl(ProductDAO productDAO, database.CategoryDAO categoryDAO) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
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
    public boolean insert(Product p, String operatorId) {
        validate(p);
        return productDAO.insert(p, operatorId);
    }

    @Override
    public boolean update(Product p, String operatorId) {
        validate(p);
        return productDAO.update(p, operatorId);
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable, String operatorId) {
        return productDAO.updateAvailability(productId, isAvailable, operatorId);
    }

    /**
     * 商品情報の業務バリデーションを行います。
     *
     * @param p 商品情報
     * @throws BusinessException 業務ルール違反がある場合
     */
    private void validate(Product p) {
        ValidationResult res = ValidationUtil.validateRequired(p.name(), "商品名");
        if (res.isInvalid()) {
            throw new BusinessException(res.message());
        }

        res = ValidationUtil.validateMaxLength(p.name(), AppConstants.MAX_PRODUCT_NAME_LENGTH, "商品名");
        if (res.isInvalid()) {
            throw new BusinessException(res.message());
        }

        res = ValidationUtil.validatePositive(p.categoryId(), "カテゴリ");
        if (res.isInvalid()) {
            throw new BusinessException(res.message());
        }

        // カテゴリの存在チェック
        if (categoryDAO.findAll().stream().noneMatch(c -> c.id() == p.categoryId())) {
            throw new BusinessException("指定されたカテゴリ（ID: " + p.categoryId() + "）は存在しません。");
        }

        res = ValidationUtil.validatePositive(p.price(), "価格");
        if (res.isInvalid()) {
            throw new BusinessException(res.message());
        }
    }
}
