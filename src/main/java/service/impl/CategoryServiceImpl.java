package service.impl;

import java.util.List;
import java.util.Optional;
import database.CategoryDAO;
import database.impl.CategoryDAOImpl;
import edu.umd.cs.findbugs.annotations.SuppressFBWarnings;
import model.Category;
import service.CategoryService;
import exception.BusinessException;
import util.ValidationUtil;
import util.ValidationResult;

/**
 * カテゴリ情報のビジネスロジックを管理するService実装クラスです。
 */
public class CategoryServiceImpl implements CategoryService {
    private final CategoryDAO categoryDAO;

    // プロダクション用コンストラクタ
    public CategoryServiceImpl() {
        this(new CategoryDAOImpl());
    }

    // テスト・DI用コンストラクタ
    @SuppressFBWarnings("EI_EXPOSE_REP2")
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public boolean insert(String name, String operatorId) {
        validateName(name);
        return categoryDAO.insert(name != null ? name.trim() : null, operatorId);
    }

    @Override
    public Optional<Category> findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean update(Category category, String operatorId) {
        validateName(category.name());
        return categoryDAO.update(category, operatorId);
    }

    private void validateName(String name) {
        ValidationResult res = ValidationUtil.validateRequired(name, "カテゴリ名");
        if (res.isInvalid()) {
            throw new BusinessException(res.message());
        }
    }
}
