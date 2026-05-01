package service.impl;

import java.util.List;
import java.util.Optional;
import database.ProductDAO;
import database.impl.ProductDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import model.Product;
import service.ProductService;
import exception.BusinessException;
import util.AppConstants;

/**
 * 商品情報のビジネスロジックを管理するService実装クラスです。
 */
@Slf4j
public class ProductServiceImpl implements ProductService {
    private final ProductDAO productDAO;
    private final database.CategoryDAO categoryDAO;
    private final service.AuditLogService auditLogService;

    // プロダクション用コンストラクタ
    public ProductServiceImpl() {
        this(new ProductDAOImpl(), new database.impl.CategoryDAOImpl(), service.ServiceFactory.getAuditLogService());
    }

    // テスト・DI用コンストラクタ
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public ProductServiceImpl(ProductDAO productDAO, database.CategoryDAO categoryDAO,
            service.AuditLogService auditLogService) {
        this.productDAO = productDAO;
        this.categoryDAO = categoryDAO;
        this.auditLogService = auditLogService;
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
        boolean success = productDAO.insert(p, operatorId);
        if (success) {
            auditLogService.log("products", String.valueOf(p.id()), "INSERT",
                    null, p.name(), operatorId);
        }
        return success;
    }

    @Override
    public boolean update(Product p, String operatorId) {
        validate(p);
        // 変更前の値を取得
        String oldValue = productDAO.findById(p.id())
                .map(old -> old.name() + " (price=" + old.price() + ")")
                .orElse(null);

        boolean success = productDAO.update(p, operatorId);
        if (success) {
            String newValue = p.name() + " (price=" + p.price() + ")";
            auditLogService.log("products", String.valueOf(p.id()), "UPDATE",
                    oldValue, newValue, operatorId);
        }
        return success;
    }

    @Override
    public boolean updateAvailability(int productId, boolean isAvailable, String operatorId) {
        boolean success = productDAO.updateAvailability(productId, isAvailable, operatorId);
        if (success) {
            auditLogService.log("products", String.valueOf(productId), "UPDATE_AVAILABILITY",
                    null, String.valueOf(isAvailable), operatorId);
        }
        return success;
    }

    @Override
    public boolean delete(int productId, String operatorId) {
        // 変更前の値を取得
        String oldValue = productDAO.findById(productId)
                .map(p -> p.name() + " (price=" + p.price() + ")")
                .orElse(null);

        boolean success = productDAO.softDelete(productId, operatorId);
        if (success) {
            auditLogService.log("products", String.valueOf(productId), "SOFT_DELETE",
                    oldValue, null, operatorId);
            log.info("商品を論理削除しました: id={}, operatorId={}", productId, operatorId);
        }
        return success;
    }

    /**
     * 商品情報の業務バリデーションを行います。
     *
     * @param p 商品情報
     * @throws BusinessException 業務ルール違反がある場合
     */
    private void validate(Product p) {
        util.Validator.create()
                .required(p.name(), "商品名は必須です。")
                .maxLength(p.name(), AppConstants.MAX_PRODUCT_NAME_LENGTH, 
                        "商品名は" + AppConstants.MAX_PRODUCT_NAME_LENGTH + "文字以内で入力してください。")
                .greaterThan(p.categoryId(), 0, "カテゴリを選択してください。")
                .greaterThan(p.price(), 0, "価格は1以上の数値を入力してください。")
                .throwOnErrors();

        // カテゴリの存在チェック
        if (categoryDAO.findAll().stream().noneMatch(c -> c.id() == p.categoryId())) {
            throw new BusinessException("指定されたカテゴリ（ID: " + p.categoryId() + "）は存在しません。");
        }
    }
}
