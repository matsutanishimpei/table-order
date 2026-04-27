package service.impl;

import java.util.List;
import java.util.Optional;
import database.CategoryDAO;
import database.impl.CategoryDAOImpl;
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
    public CategoryServiceImpl(CategoryDAO categoryDAO) {
        this.categoryDAO = categoryDAO;
    }

    @Override
    public List<Category> findAll() {
        return categoryDAO.findAll();
    }

    @Override
    public boolean insert(String name) {
        validateName(name);
        return categoryDAO.insert(name != null ? name.trim() : null);
    }

    @Override
    public Optional<Category> findById(int id) {
        return categoryDAO.findById(id);
    }

    @Override
    public boolean update(Category category) {
        validateName(category.name());
        return categoryDAO.update(category);
    }

    private void validateName(String name) {
        ValidationResult res = ValidationUtil.validateRequired(name, "カテゴリ名");
        if (res.isInvalid()) throw new BusinessException(res.message());
    }
}
