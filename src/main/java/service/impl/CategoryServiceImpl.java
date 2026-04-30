package service.impl;

import java.util.List;
import java.util.Optional;
import database.AuditLogDAO;
import database.CategoryDAO;
import database.impl.AuditLogDAOImpl;
import database.impl.CategoryDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import lombok.extern.slf4j.Slf4j;
import model.Category;
import service.CategoryService;

/**
 * カテゴリ情報のビジネスロジックを管理するService実装クラスです。
 */
@Slf4j
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;
    private final AuditLogDAO auditLogDAO;

    // プロダクション用コンストラクタ
    public CategoryServiceImpl() {
        this(new CategoryDAOImpl(), new AuditLogDAOImpl());
    }

    // テスト・DI用コンストラクタ（後方互換性）
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this(categoryDAO, new AuditLogDAOImpl());
    }

    // テスト・DI用コンストラクタ（AuditLogDAO 含む）
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CategoryServiceImpl(CategoryDAO categoryDAO, AuditLogDAO auditLogDAO) {
        this.categoryDAO = categoryDAO;
        this.auditLogDAO = auditLogDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public boolean insert(String name, String operatorId) {
        validateName(name);
        boolean success = categoryDAO.insert(name != null ? name.trim() : null, operatorId);
        if (success) {
            auditLogDAO.log("categories", "-", "INSERT", null, name, operatorId);
        }
        return success;
    }

    @Override
    public Optional<Category> findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean update(Category category, String operatorId) {
        validateName(category.name());
        // 変更前の値を取得
        String oldValue = categoryDAO.findById(category.id())
                .map(Category::name)
                .orElse(null);

        boolean success = categoryDAO.update(category, operatorId);
        if (success) {
            auditLogDAO.log("categories", String.valueOf(category.id()), "UPDATE",
                    oldValue, category.name(), operatorId);
        }
        return success;
    }

    @Override
    public boolean delete(int id, String operatorId) {
        // 変更前の値を取得
        String oldValue = categoryDAO.findById(id)
                .map(Category::name)
                .orElse(null);

        boolean success = categoryDAO.softDelete(id, operatorId);
        if (success) {
            auditLogDAO.log("categories", String.valueOf(id), "SOFT_DELETE",
                    oldValue, null, operatorId);
            log.info("カテゴリを論理削除しました: id={}, operatorId={}", id, operatorId);
        }
        return success;
    }

    private void validateName(String name) {
        util.Validator.create()
                .required(name, "カテゴリ名は必須入力です。")
                .throwOnErrors();
    }
}
